package org.guap;

import java.util.ArrayList;

import static org.guap.Main.*;

/**
 * Данные класс отвечает за области. В данном классе храниться сервер, закрепленной за данной
 * областью, временная шкала данной области. Так же в данном классе осуществляется подсчёт средних
 * значений задержки, выходной интенсивности, размера работы
 */
public class Location {

    /** Данное поле хранит номер данной локации*/
    public int numberOfThisLocation;
    /** Данное поле хранит сервер, который обслуживает задачи в данной области*/
    public Server server;
    /** Данное поле хранит размер задач и расписание того, когда они попадают в систему*/
    public ArrayList<WorkUser> inputStream;
    /** Данное поле хранит все себе такое вспомагательное значение, как суммарный объём задач всех
     * пользователей */
    public double lengthOfAllWorks;

    /** Данное поле хранит в себе такое вспомгательное значение, как общее количество заявок(задач)
     * пользователей в этой области */
    public int numberOfWorksInLocation;
    private int nextStartedWork;

    /**
     * В данном конструкторе формирются все сущности необходимые для корректного
     * функционирования области, а так же вызывается метод, который формирует входную очередь
     * @param lambda Текущее значение входной интенсивности
     * @param numberOfThisLocation Номер текущей области
     */
    public Location(double lambda, int numberOfThisLocation) {
        this.numberOfThisLocation = numberOfThisLocation;
        server = new Server(this.numberOfThisLocation);
        inputStream = new ArrayList<>();
        this.lengthOfAllWorks = 0;
        createInputStream(lambda, T);
        this.numberOfWorksInLocation = inputStream.size();
        nextStartedWork = 0;
    }

    /**
     * Данный метод формирует входную очередь с заданной интенсивностью
     * @param lambda значение входной интенсивности
     * @param time длина временной линии
     */
    public void createInputStream(double lambda, double time) {
        int tmpSize = Utils.generateExponentialDistributedNumberOfQuants(LAMBDA_FOR_TASK_SIZE); //экспоненциальное распределение
//        int tmpSize = (int)(1 / this.sizeOfQuant); //постоянная
//        int tmpSize = (int) Math.ceil( (0.8 + 0.4*Math.random()) / this.sizeOfQuant); //равномерное распределение

        double tmpWindowIn = Utils.generateExponentialValue(lambda);
        this.lengthOfAllWorks += tmpSize;
        int userNumber = 0;

        inputStream.add(new WorkUser(userNumber, numberOfThisLocation, tmpWindowIn, tmpSize));
        userNumber++;

        while (inputStream.get(userNumber - 1).workInfo.windowIn <= time) {

            tmpSize = Utils.generateExponentialDistributedNumberOfQuants(LAMBDA_FOR_TASK_SIZE); //экспоненциальное распределение
//            tmpSize = (int) Math.ceil( (0.8 + 0.4*Math.random()) / this.sizeOfQuant); //равномерное распределение

            tmpWindowIn = Utils.generateExponentialValue(lambda);
            inputStream.add(new WorkUser(userNumber, numberOfThisLocation,
                    inputStream.get(userNumber - 1).workInfo.windowIn + tmpWindowIn, tmpSize));
            this.lengthOfAllWorks += tmpSize;
            userNumber++;
        }
    }

    /**
     * Данный метод проверяет наличие новых заявок пользователь - задача в системе. Если,
     * обнаруживается новая заявка, готовая к поступленю в систему, то тогда задача данного
     * пользователя добавляется на сервер текущей области
     * @param time текущее значение времени
     */
    public void processingAtLocation(double time) {
        this.server.getService(time);
        while (nextStartedWork < inputStream.size()) {
            if ((time >= inputStream.get(nextStartedWork).workInfo.windowIn) &&
                    (!inputStream.get(nextStartedWork).statusBeginCount)) {
                server.addNewWork(inputStream.get(nextStartedWork));
                nextStartedWork++;
            } else break;
        }
    }
}