import java.util.*;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;

/**
 * В данном классе создаются объекты областей, а так же происходит управление перемещениями
 * пользователей и подсчёт среднего значения задержки, выходной интенсвности, среднего размера работы,
 * по всем областям
 */
public class Model implements Callable<OutputData> {

    /** В данном поле храниться количество областей в системе */
    public int numberOfLocations;
    /** В данном листе храняться объекты отвечающие за области*/
    public ArrayList<Location> locations;
    Random random;
    /** Данное поле хранит вероятность, с которой пользователь, при перемещении в
     * следующую область, решит перенести свою задачу на сервера следующей области */
    public double a;
    /** Данное поле хранит значение для рассчёта времени перемещения пользователя
     * до следующей локации. Для симметричной модели значение этого параметра всегда равно 1*/
    public double q;
    /** Данное поле хранит значение для рассчёта времени необходимого для перемещения
     * задачи пользователя с серверов одной области на сервера другой области*/
    public double d;
    /** Данное поле хранит условное количество единиц времени производится
     * моделирование*/
    private final float T;
    /** Данное поле хранит среднее значение задержки задачи в системе*/
    public double mD;
    /***/
    public double mDTheoretical;
    /** Данное поле хранит среднее значение выходной интенсивности в системе*/
    public double lyambda_out;
    /** Данное поле хранит размер кванта, то есть размер шага с которым мы двигаемся по
     * временной шкале каждой локации*/
    public double sizeOfQuant;
    /** Данное поле хранит среднее значение размера задачи в системе*/
    public double mediumSizeOfWork;

    /** Данное поле хранит количество завершённых работ*/
    public int numberOfExitedWorks;
    /** Данное поле хранит суммарную задержку всех завершённых работ*/
    public double summaryDelay;
    /** Данное поле хранит суммарный объём всех завершённых работ*/
    public double summaryLengthOfWorks;
    /** Данное поле хранит количество пользователей, которые покидают область до того, как завершился рассчёт их
     * задачи */
    public int finishedWorksWhichWereTransferedMoreThanOneTime = 0;
    /** Данное поле хранит количество пользователей, которые покидают систему после того, как было завершено
     * вычисление их задачи */
    public int allFinishedWorks = 0;
    public int allNumberOfTransfersOfEachFinishedWork;
    /**
     * Данное поле хранит в себе суммарное количество трансферов пользователей, чьи задачи были
     * выполненны
     */
    public Long allNumberOfUserWithCompletedTasksTransfers = 0L;
    /** Данная коллекция хранит среднее кол-во задач в каждый квант времени для каждой области */
    public Map<Integer, Long> averageNumberOfWorksInEachLocation = new HashMap<>();
    /**
     * Данная коллекция хранит в себе значения пары
     * Кол-во раз (n), котораое перемещалась задача - Кол-во завершённых задач, перемещавшихся n раз
     */
    public Map<Integer, Long> numberOfTransfersOfCompletedWorks = new HashMap<>();
    /**
     * Данная коллекция хранит в себе значения пары
     * Кол-во раз (n), котораое перемешялся пользователь - Кол-во пользователей,
     * обладающих завершёнными задачами, и перемещавшихся (сам пользователь) n раз
     */
    public Map<Integer, Long> numberOfTransfersOfUsersWithCompletedWorks = new HashMap<>();
    public float lambda;

    /**
     * В данном конструкторе задаются все параметры необходимые для работы модели
     * @param lambda Текущее значение входной интенсивности
     * @param a  Вероятность, с которой пользователь, при перемещении в
     *           следующую область, решит перенести свою задачу на сервера следующей области
     * @param q  Коэффициент для рассчёта времени перемещения пользователя
     *           до следующей локации
     * @param d  Коэффициент для рассчёта времени необходимого для перемещения
     *           задачи пользователя с серверов одной области на сервера другой области
     * @param quant Данный параметр означает размер кванта, то есть размер шага с которым мы двигаемся по
     *              временной шкале каждой локации
     * @param numberOfLocations Данный параметр означает количество областей с которыми производиться
     *                          моделирование
     * @param T Данный параметр означает какое условное количество единиц времени производится
     *          моделирование
     * @param serviceRate Данный параметр означает, с какой интенсивностью серевер обрабатывает
     *                    задачи пользователей
     */
    public Model(float lambda, double a, double q, double d, double quant, int numberOfLocations,
                 float T, double serviceRate) {

        locations = new ArrayList<>();
        random = new Random();
        this.sizeOfQuant = quant;
        for (int i = 0; i < numberOfLocations; i++) {
            locations.add(new Location(lambda, T, q, i, quant, d, serviceRate));
        }
        this.a = a;
        this.q = q;
        this.d = d;
        this.lambda = lambda;
        this.numberOfLocations = numberOfLocations;
        this.T = T;
        this.mD = 0;
        this.mDTheoretical = 1 / (1 - lambda);
        this.lyambda_out = 0;
        this.mediumSizeOfWork = 0;

        finishedWorksWhichWereTransferedMoreThanOneTime = 0;
        allFinishedWorks = 0;
        allNumberOfTransfersOfEachFinishedWork = 0;
        allNumberOfUserWithCompletedTasksTransfers = 0L;

        numberOfExitedWorks = 0;
        summaryDelay = 0;
        summaryLengthOfWorks = 0;

    }

    @Override
    public OutputData call() throws Exception {
        this.getModeling();
        this.countModelStatistics();
        return this.outputSummary();
    }

    /**
     * В данном методе организуется перемещение программы по линии времени в каждой локации, а
     * так же расчёт итоговых средних значений задержки, выходной интенсвности, среднего размера работы,
     *  по всем областям
     */
    public void getModeling() {

        for (double t = 0; t < this.T; t += sizeOfQuant) {
            for (int i = 0; i < locations.size(); i++) {
                locations.get(i).processingAtLocation(t);
                this.userSwitchLocation();
            }
        }

    }

    public void countModelStatistics() {

        for (Location currentLocation: locations) {
            for (WorkUser currentWorkUser: currentLocation.inputStream) {
                if ( (currentWorkUser.statusWorkFinished) && (currentWorkUser.delay != 0.0) ) {
                    numberOfExitedWorks++;
                    summaryDelay += currentWorkUser.delay;
                    summaryLengthOfWorks += currentWorkUser.workInfo.workSize;
                }

                if (Main.SHOW_LOCATION_SUMMARY) {
                    System.out.println("User  do " + currentWorkUser.workProcessingValue +
                            " from his job. Full size = " + currentWorkUser.workInfo.workSize);
                }
            }
        }

        this.lyambda_out = (double) numberOfExitedWorks / (T * numberOfLocations) ;
        this.mediumSizeOfWork = summaryLengthOfWorks / numberOfExitedWorks;
        this.mD = summaryDelay / numberOfExitedWorks;
    }

    /**
     * Данный метод отвечает за выбор пользователем новой области. Так же в данном методе
     * пользователь принимает решение о том необходимо ему переносить свою задачу на сервера новой
     * области или же нет. В случае, когда пользователь решает перенести свою задачу на сервера новой
     * области, в данном методе осуществляет удаление задачи со старого сервера и добавление её в
     * список задач нового сервера
     */
    public void userSwitchLocation() {
        for (int i = 0; i < locations.size(); i++) {
            Location tmpLocation = locations.get(i);
            Server tmpServer = tmpLocation.server;
            for (int k = 0; k < tmpServer.workUsersOnServer.size(); k++) {
                WorkUser tmpWorkUser = tmpServer.workUsersOnServer.get(k);
                if (tmpWorkUser.timeInCurrentLocation != 0) {
                    continue;
                }
                if (numberOfLocations == 1) {
                    tmpWorkUser.changeUserLocation(0);
                    tmpWorkUser.numberOfUserTransfers = 0;
                    continue;
                }
                /*
                    В эту map добавляем пару значений о необходимых для выбора следующей области для пользователя
                    Ключ - номер области
                    Значение - Время необходимое на путь до данной области (время это
                    значение распределённое по экспоненциальному закону с параметром q)
                 */
                Map<Integer, Integer> nextLocationsData = new HashMap<>();
                int currentClosestLocation = 0;
                int timeToCurrentClosestLocation = 0;
                for (int locationTmpNumber = 0; locationTmpNumber < locations.size(); locationTmpNumber++) {
                    if (locationTmpNumber == tmpWorkUser.userLocation)
                        continue;
                    int timeToTmpLocation = (int) Math.ceil(- (Math.log(Math.random()) / this.q) / this.sizeOfQuant);
                    nextLocationsData.put(locationTmpNumber, timeToTmpLocation);
                    if (nextLocationsData.size() == 1) {
                        currentClosestLocation = locationTmpNumber;
                        timeToCurrentClosestLocation = timeToTmpLocation;
                    }
                }

                for (Map.Entry<Integer, Integer> entry : nextLocationsData.entrySet()) {
                    if (entry.getValue() < timeToCurrentClosestLocation) {
                        currentClosestLocation = entry.getKey();
                        timeToCurrentClosestLocation = entry.getValue();
                    }
                }

                /*
                    Для симметричной модели
                    В случае, если пользователь и его задача уже находится отдельно друг от друга
                    то, мы не переносим задачу (оставляем её в той же области в которой она уже находится).
                    Мы только перемещаем пользователя в новую (ближайшую область)
                */
                if (tmpWorkUser.isEverAbandoned) {
                    tmpWorkUser.changeUserLocation(currentClosestLocation);
                } else {
                    /*
                    Для симметричной модели
                    В случае, если пользователь и его задача находятся вместе в текущей области
                    то, мы перемещаем пользователя в новую область.
                    Задачу пользователя мы перемещаем с вероятностью a.
                    В случае перемещения задачи пользователя, для задачи вычисляется время
                    необходимое для переноса с одного сервера на другой (на трансфер)
                 */
                    tmpWorkUser.changeUserLocation(currentClosestLocation);
                    double probabilityToSwitchWorkServer = Math.random();
                    if (probabilityToSwitchWorkServer < this.a) {
                        tmpServer.removeWorkToSwitchServer(tmpWorkUser);
                        locations.get(currentClosestLocation).server.addNewTransferWork(tmpWorkUser);
                        tmpWorkUser.changeWorkLocation(currentClosestLocation);
                    } else tmpWorkUser.isEverAbandoned = true;
                }
            }
        }
    }

    public OutputData outputSummary() {
        System.out.println("lambda = " + lambda + " M[D] = " + mD + " lambda_out = " + lyambda_out);

        double averageNumberOfWorkTransfers = ((double)allNumberOfTransfersOfEachFinishedWork / allFinishedWorks);
        System.out.println("Average number of transfers for each work " + averageNumberOfWorkTransfers);
        double averageNumberOfUserTransfers = ((double)allNumberOfUserWithCompletedTasksTransfers / allFinishedWorks);
        System.out.println("Average transfers number of users with completed tasks " + averageNumberOfUserTransfers);
        System.out.println("Part of transferred works from finished works " + finishedWorksWhichWereTransferedMoreThanOneTime + " / " + allFinishedWorks + " = " +
                (double) finishedWorksWhichWereTransferedMoreThanOneTime / allFinishedWorks);
        double transfersPerTime = (double) allNumberOfTransfersOfEachFinishedWork / T;
        System.out.println("Average transfers number in one window " + transfersPerTime);

        double dPoLittle = 0;
        double avarageNumberOfTasksInOneQuant = 0;

        avarageNumberOfTasksInOneQuant /= Main.numberOfLocations;
        dPoLittle = avarageNumberOfTasksInOneQuant / lambda;
        System.out.println("M[D] by Little = " + dPoLittle);
        System.out.println("Number of tasks with transfer numbers");


        List<Integer> numberTransfersOfWorks = numberOfTransfersOfCompletedWorks.keySet().stream().sorted().collect(Collectors.toList());
        for (Integer currentKey : numberTransfersOfWorks) {
            long currentValue = numberOfTransfersOfCompletedWorks.get(currentKey);
            double probabilityToNTransfers = (double) currentValue / allFinishedWorks;
            if (Main.SHOW_WORKS_TRANSFER_PROBABILITY) {
                System.out.println("With transfer num equals " + currentKey + " was " + currentValue + " works. Probability to make n transfers is "
                        + probabilityToNTransfers);
            }
        }
        System.out.println();

        List<Integer> numberTransfersOfUsers = numberOfTransfersOfUsersWithCompletedWorks.keySet().stream().sorted().collect(Collectors.toList());
        System.out.println("Number of users with transfer numbers");
        for (Integer currentKey : numberTransfersOfUsers) {
            long currentValue = numberOfTransfersOfUsersWithCompletedWorks.get(currentKey);
            double probabilityToNTransfers = (double) currentValue / allFinishedWorks;
            if (Main.SHOW_USERS_TRANSFER_PROBABILITY) {
                System.out.println("With transfer num equals " + currentKey + " was " + currentValue + " users. Probability to make n transfers is "
                        + probabilityToNTransfers);
            }
        }
        System.out.println();

        OutputData result = null;
        if (lambda < 1)
            result = new OutputData(lambda, lyambda_out, mediumSizeOfWork, transfersPerTime, mD, mDTheoretical);
        else result = new OutputData(lambda, lyambda_out, mediumSizeOfWork, transfersPerTime);

        return result;
    }
}