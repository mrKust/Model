package org.guap;

import java.util.*;
import java.util.concurrent.Callable;

import static org.guap.Main.*;

/**
 * В данном классе создаются объекты областей, а так же происходит управление перемещениями
 * пользователей и подсчёт среднего значения задержки, выходной интенсвности, среднего размера работы,
 * по всем областям
 */
public class Model implements Callable<OutputData> {
    /**
     * В данном листе храняться объекты отвечающие за области
     */
    public ArrayList<Location> locations;
    /**
     * Данное поле хранит среднее значение задержки задачи в системе
     */
    public double mD;
    /**
     * Данное поле хранит теоретическое значение, верное для случаев с a = 0.
     */
    public double mDTheoretical;
    /**
     * Данное поле хранит среднее значение выходной интенсивности в системе
     */
    public double lambda_out;
    /**
     * Данное поле хранит среднее значение размера задачи в системе
     */
    public double mediumSizeOfWork;

    /**
     * Данное поле хранит количество завершённых работ
     */
    public int numberOfExitedWorks;
    /**
     * Данное поле хранит суммарную задержку всех завершённых работ
     */
    public double summaryDelay;

    public double summaryAgeOfInformation;
    /**
     * Данное поле хранит суммарный объём всех завершённых работ
     */
    public double summaryLengthOfWorks;
    /**
     * Данное поле хранит суммарное количество трансферов, всех завершённых задач
     */
    public int allNumberOfTransfersOfEachFinishedWork;
    /**
     * Данное поле хранит количество задач, выполнение которых было завершенно до того момента,
     * как пользователь переместился из исходной области
     */
    public int allNumberOfWorksWhichCompleteBeforeUserMoves;
    /**
     * Данное поле хранит в себе суммарное количество трансферов пользователей, чьи задачи были
     * выполненны
     */
    public Long allNumberOfTransfersOfUsersWithCompletedWork = 0L;
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
    /**
     * Данное поле хранит значение входной интенсивности для модели
     */
    public double lambda;
    public double mAgeOfInfTheor;
    public double mAgeOfInfModel;
    public double dop; //todo change name

    /**
     * В данном конструкторе задаются все параметры необходимые для работы модели
     *
     * @param lambda            Текущее значение входной интенсивности
     */
    public Model(double lambda) {

        locations = new ArrayList<>();
        for (int i = 0; i < numberOfLocations; i++) {
            locations.add(new Location(lambda, i));
        }
        this.lambda = lambda;
        this.mD = 0;
        this.lambda_out = 0;
        this.mediumSizeOfWork = 0;
        this.mAgeOfInfModel = 0;
        this.mAgeOfInfTheor = 0;
        this.dop = 0;

        allNumberOfTransfersOfEachFinishedWork = 0;
        allNumberOfTransfersOfUsersWithCompletedWork = 0L;
        allNumberOfWorksWhichCompleteBeforeUserMoves = 0;

        numberOfExitedWorks = 0;
        summaryDelay = 0;
        summaryAgeOfInformation = 0;
        summaryLengthOfWorks = 0;

    }

    /**
     * Данный метод контролирует процесс работы модели и сбор статистики, по итогам её работы.
     * Он запускает метод моделирования системы с заданной входной интенсивностью
     *
     * @return Возвращает объект содержащий основной набор выходных параметров системы
     * @throws Exception
     */
    @Override
    public OutputData call() throws Exception {
        this.getModeling();
        this.countModelStatistics();
        return this.outputSummary();
    }

    /**
     * В данном методе организуется запуск метода, производящего вычисления в каждой области, и метода отвечающего
     * за перемещение пользователей, в каждый рассматриваемый момент времени
     */
    public void getModeling() {

        for (double t = 0; t < T; t += sizeOfQuant) {
            for (Location location : locations) {
                location.processingAtLocation(t);
            }
            this.userSwitchLocation();
        }

    }

    /**
     * В данном методе организиуется рассмотрение всех задач и пользователей созданных системой.
     * Происходит подсчёт значений, необходимых для вычисления выходных параметров системы
     */
    public void countModelStatistics() {
        for (Location currentLocation : locations) {
            for (WorkUser currentWorkUser : currentLocation.inputStream) {
                if ((currentWorkUser.statusWorkFinished) && (currentWorkUser.delay != 0.0)) {
                    numberOfExitedWorks++;
                    summaryDelay += currentWorkUser.delay;
                    summaryAgeOfInformation += currentWorkUser.ageOfInformation;
                    summaryLengthOfWorks += currentWorkUser.workInfo.workSize;
                    allNumberOfTransfersOfEachFinishedWork += currentWorkUser.numberOfWorkTransfers;
                    allNumberOfTransfersOfUsersWithCompletedWork += currentWorkUser.numberOfUserTransfers;

                    if (currentWorkUser.numberOfUserTransfers == 0)
                        allNumberOfWorksWhichCompleteBeforeUserMoves++;

                    if (numberOfTransfersOfUsersWithCompletedWorks.get(currentWorkUser.numberOfUserTransfers) == null) {
                        numberOfTransfersOfUsersWithCompletedWorks.put(currentWorkUser.numberOfUserTransfers, 1L);
                    } else {
                        long val = numberOfTransfersOfUsersWithCompletedWorks.get(currentWorkUser.numberOfUserTransfers);
                        val++;
                        numberOfTransfersOfUsersWithCompletedWorks.put(currentWorkUser.numberOfUserTransfers, val);
                    }

                    if (numberOfTransfersOfCompletedWorks.get(currentWorkUser.numberOfWorkTransfers) == null) {
                        numberOfTransfersOfCompletedWorks.put(currentWorkUser.numberOfWorkTransfers, 1L);
                    } else {
                        long val = numberOfTransfersOfCompletedWorks.get(currentWorkUser.numberOfWorkTransfers);
                        val++;
                        numberOfTransfersOfCompletedWorks.put(currentWorkUser.numberOfWorkTransfers, val);
                    }

                }

                if (Main.SHOW_LOCATION_SUMMARY) {
                    System.out.println("User  do " + currentWorkUser.workProcessingValue +
                            " from his job. Full size = " + currentWorkUser.workInfo.workSize);
                }
            }
        }

        if (MODELING_SYSTEM_TYPE == SystemType.MM1)
            serviceRate = 1.0;

        this.lambda_out = (double) numberOfExitedWorks / (T * numberOfLocations);
        this.mediumSizeOfWork = summaryLengthOfWorks / numberOfExitedWorks;
        double p = lambda / serviceRate;
        this.mDTheoretical = switch (MODELING_SYSTEM_TYPE) {
            case KR -> 1 / (1 - lambda);
            case MM1 -> (p / (1 - p)) + (1 / serviceRate);
            case MD1 -> (3 - 2 * p) / (2 * (1 - p));
        };
        this.mD = summaryDelay / numberOfExitedWorks;
        this.mAgeOfInfTheor = switch (MODELING_SYSTEM_TYPE) {
            case MD1 -> (1 / serviceRate) * ( (1/(2*(1-p))) + 0.5 + (((1-p)*Math.exp(p))/p) );
            case MM1 -> 1 / Main.serviceRate * (1 + (1 / p) + (Math.pow(p, 2) / (1 - p)) );
            case KR -> -1;
        };
        this.mAgeOfInfModel = summaryAgeOfInformation / (T * numberOfLocations);
        this.dop = this.mD + (1 / lambda);
    }

    /**
     * Данный метод отвечает за выбор пользователем новой области. Так же в данном методе
     * пользователь принимает решение о том необходимо ему переносить свою задачу на сервера новой
     * области или же нет. В случае, когда пользователь решает перенести свою задачу на сервера новой
     * области, в данном методе осуществляет удаление задачи со старого сервера и добавление её в
     * список задач нового сервера
     */
    public void userSwitchLocation() {
        for (Location tmpLocation: locations) {
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
                    int timeToTmpLocation = (int) Math.ceil(-(Math.log(Math.random()) / q) / sizeOfQuant);
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
                    if (probabilityToSwitchWorkServer < a) {
                        tmpServer.removeWorkToSwitchServer(tmpWorkUser);
                        locations.get(currentClosestLocation).server.addNewTransferWork(tmpWorkUser);
                    } else tmpWorkUser.isEverAbandoned = true;
                }
            }
        }
    }

    /**
     * Данный метод выводит на экран необходимые выходные параметры и возвращает список параметров необходимых
     * для записи в файл
     *
     * @return Список выходных параметров, необходимых для построения части графиков
     */
    public OutputData outputSummary() {
        StringBuilder textData = new StringBuilder();
        textData.append("lambda = ")
                .append(lambda)
                .append(" M[D] = ")
                .append(mD)
                .append(" lambda_out = ")
                .append(lambda_out)
                .append("\n");

        double partOfWorksCompletedBeforeUserMoves = ((double) allNumberOfWorksWhichCompleteBeforeUserMoves / numberOfExitedWorks);
        textData.append("Part of works which were completed before user moves from start location ")
                .append(partOfWorksCompletedBeforeUserMoves)
                .append("\n");
        double averageNumberOfWorkTransfers = ((double) allNumberOfTransfersOfEachFinishedWork / numberOfExitedWorks);
        textData.append("Average number of transfers for completed works ")
                .append(averageNumberOfWorkTransfers)
                .append("\n");
        double averageNumberOfUserTransfers = ((double) allNumberOfTransfersOfUsersWithCompletedWork / numberOfExitedWorks);
        textData.append("Average number of transfers for users with completed tasks ")
                .append(averageNumberOfUserTransfers)
                .append("\n");
        double transfersPerTime = (double) allNumberOfTransfersOfEachFinishedWork / T;
        textData.append("Average transfers number in one window ")
                .append(transfersPerTime)
                .append("\n");

        textData.append("Average age of information theoretical ")
                .append(mAgeOfInfTheor)
                .append("\n")
                .append("Average age of information modeling ")
                .append(mAgeOfInfModel)
                .append("\n");

        if (Main.SHOW_WORKS_TRANSFER_PROBABILITY) {
            textData.append("Number of works with transfer numbers\n");
            List<Integer> numberTransfersOfWorks = numberOfTransfersOfCompletedWorks.keySet().stream().sorted().toList();
            for (Integer currentKey : numberTransfersOfWorks) {
                long currentValue = numberOfTransfersOfCompletedWorks.get(currentKey);
                double probabilityToNTransfers = (double) currentValue / numberOfExitedWorks;
                textData.append("With transfer num equals ")
                        .append(currentKey)
                        .append(" was ")
                        .append(currentValue)
                        .append(" works. Probability to make n transfers is ")
                        .append(probabilityToNTransfers)
                        .append("\n");
            }
            textData.append("\n");
        }

        if (Main.SHOW_USERS_TRANSFER_PROBABILITY) {
            textData.append("Number of users with transfer numbers\n");
            List<Integer> numberTransfersOfUsers = numberOfTransfersOfUsersWithCompletedWorks.keySet().stream().sorted().toList();
            for (Integer currentKey : numberTransfersOfUsers) {
                long currentValue = numberOfTransfersOfUsersWithCompletedWorks.get(currentKey);
                double probabilityToNTransfers = (double) currentValue / numberOfExitedWorks;
                textData.append("With transfer num equals ")
                        .append(currentKey)
                        .append(" was ")
                        .append(currentValue)
                        .append(" users. Probability to make n transfers is ")
                        .append(probabilityToNTransfers)
                        .append("\n");
            }
            textData.append("\n");
        }

        System.out.println(textData);

        if (lambda < 1)
            return new OutputData(lambda, lambda_out, mediumSizeOfWork, transfersPerTime, mDTheoretical, mD,
                    mAgeOfInfTheor, mAgeOfInfModel, dop);
        else return new OutputData(lambda, lambda_out, mediumSizeOfWork, transfersPerTime);
    }
}