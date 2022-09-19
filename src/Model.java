import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * В данном классе создаются объекты областей, а так же происходит управление перемещениями
 * пользователей и подсчёт среднего значения задержки, выходной интенсвности, среднего размера работы,
 * по всем областям
 */
public class Model {

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
    private float T;
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
    public static int numberOfExitedWorks;
    /** Данное поле хранит суммарную задержку всех завершённых работ*/
    public static double summaryDelay;
    /** Данное поле хранит суммарный объём всех завершённых работ*/
    public static double summaryLengthOfWorks;

    /**
     * В данном конструкторе задаются все параметры необходимые для работы модели
     * @param lyambda Текущее значение входной интенсивности
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
    public Model(float lyambda, double a, double q, double d, double quant, int numberOfLocations,
                 float T, double serviceRate) {

        locations = new ArrayList<>();
        random = new Random();
        this.sizeOfQuant = quant;
        for (int i = 0; i < numberOfLocations; i++) {
            locations.add(new Location(lyambda, T, q, i, quant, d, serviceRate));
        }
        this.a = a;
        this.q = q;
        this.d = d;
        this.numberOfLocations = numberOfLocations;
        this.T = T;
        this.mD = 0;
        this.mDTheoretical = 1 / (1 - lyambda);
        this.lyambda_out = 0;
        this.mediumSizeOfWork = 0;

        numberOfExitedWorks = 0;
        summaryDelay = 0;
        summaryLengthOfWorks = 0;

    }

    /**
     * Данный конструктор используется для моделирования работы случаев, когда входная интенсивность меньше 1
     */
    public Model(double a, double q, double d, double quant, int numberOfLocations,
                 float T, double serviceRate) {

        locations = new ArrayList<>();
        random = new Random();
        this.sizeOfQuant = quant;
        for (int i = 0; i < numberOfLocations; i++) {
            locations.add(new Location(T, q, i, quant, d, serviceRate));
        }
        this.a = a;
        this.q = q;
        this.d = d;
        this.numberOfLocations = numberOfLocations;
        this.T = T;
        this.lyambda_out = 0;
        this.mediumSizeOfWork = 0;

        numberOfExitedWorks = 0;
        summaryDelay = 0;
        summaryLengthOfWorks = 0;

    }

    /**
     * В данном методе организуется перемещение программы по линии времени в каждой локации, а
     * так же расчёт итоговых средних значений задержки, выходной интенсвности, среднего размера работы,
     *  по всем областям
     */
    public void getModeling() {
        //System.out.println("Start of modeling");

        for (double t = 0; t < this.T; t += sizeOfQuant) {
            //System.out.println("In t = " + t);

            for (int i = 0; i < locations.size(); i++) {
                locations.get(i).processingAtLocation(t);
                this.userSwitchLocation();
            }
        }

        this.lyambda_out = (double) numberOfExitedWorks / (T * numberOfLocations) ;
        this.mediumSizeOfWork = summaryLengthOfWorks / numberOfExitedWorks;
        this.mD = summaryDelay / numberOfExitedWorks;

        System.out.println("Summary");
        //по каждому пользователю
        for (int i = 0; i < locations.size(); i++) {
            ArrayList<WorkUser> tmpLocationInputStream = locations.get(i).inputStream;
            for (int k = 0; k < tmpLocationInputStream.size(); k++) {
                System.out.println("User  do " + tmpLocationInputStream.get(k).workProcessingValue +
                        " from his job. Full size = " + tmpLocationInputStream.get(k).workInfo.workSize);
            }
        }

    }

    public void getModelingForLowIntensity() {

        for (double t = 0; t < this.T; t += sizeOfQuant) {

            for (int i = 0; i < locations.size(); i++) {
                locations.get(i).processingAtLocation(t);
                this.userSwitchLocation();
                int numberOfLastElement = locations.get(i).inputStream.size() - 1;
                if ((locations.get(i).numberOfWorksInLocation != 0) &&
                (locations.get(i).inputStream.get(numberOfLastElement).statusFinishedOrUnfinished)) {
                    locations.get(0).createInputStream(t);
                }
            }


        }

        this.lyambda_out = (double) numberOfExitedWorks / T;
        this.mediumSizeOfWork = summaryLengthOfWorks / numberOfExitedWorks;
        this.mD = summaryDelay / numberOfExitedWorks;
        this.mDTheoretical = 1 / (1 - this.lyambda_out);

        System.out.println("Summary");
        //по каждому пользователю
        for (int i = 0; i < locations.size(); i++) {
            ArrayList<WorkUser> tmpLocationInputStream = locations.get(i).inputStream;
            for (int k = 0; k < tmpLocationInputStream.size(); k++) {
                System.out.println("User  do " + tmpLocationInputStream.get(k).workProcessingValue +
                        " from his job. Full size = " + tmpLocationInputStream.get(k).workInfo.workSize);
            }
        }
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
                if (tmpWorkUser.timeInCurrentLocation == 0) {
                    double probabilityToSwitch = Math.random();
                    List<Pair> timeToNextLocation = new ArrayList<>();
                    for (int j = 0; j < locations.size(); j++) {
                        if (j == tmpWorkUser.userLocation)
                            continue;
                        //добавляем пару с значениями
                        //1 - Номер локации
                        //2 - Путь до данной локации (значение распределённое по экспоненциальному
                        // закону с параметром q)
                        timeToNextLocation.add(new Pair(j,
                                (int) Math.ceil(- (Math.log(Math.random()) / this.q) / this.sizeOfQuant)));
                    }

                    int nextLocation = (int) timeToNextLocation.get(0).windowIn;
                    double tmpClosetPath = timeToNextLocation.get(0).workSize;
                    for (int j = 0; j < timeToNextLocation.size(); j++) {
                        if (tmpClosetPath > timeToNextLocation.get(j).workSize) {
                            tmpClosetPath = timeToNextLocation.get(j).workSize;
                            nextLocation = (int) timeToNextLocation.get(j).windowIn;
                        }
                    }

                    //случай, когда задача пользователя уже находится отдельно от пользователя
                    //в случае симметричной системы, мы не переносим задачу, а только перемещаем
                    //пользователя
                    if (tmpWorkUser.userLocation != tmpWorkUser.workLocation) {
                        tmpWorkUser.userLocation = nextLocation;

                    } else {
                        //когда пользователь и задача находятся в одной области
                        //в этом случае меняем положение пользователя
                        tmpWorkUser.changeUserLocation(nextLocation);
                        //в случае, если случайное значение больше заданного значения
                        //вероятности, тогда пользователь решает перенести свою работу с одних серверов на другие
                        if (probabilityToSwitch < this.a) {
                            tmpServer.removeJobToSwitchServer(tmpWorkUser);
                            locations.get(nextLocation).server.addNewTransferJob(tmpWorkUser);
                            tmpWorkUser.changeWorkLocation(nextLocation);
                            tmpWorkUser.transfer();

                        }
                    }
                }
            }
        }
    }
}