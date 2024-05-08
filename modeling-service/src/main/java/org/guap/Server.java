package org.guap;

import java.util.ArrayList;

/**
 * Данный класс отвечает за обработку задач на той области, за которой закреплён сервер
 */
public class Server {

    /** Данное поле хранит номер локации, которую обслуживает сервер*/
    public int numberOfLocation;
    /** Данное поле хранит значение интенсивности, с которой сервер обрабатывает задачи
     * пользователей*/
    public double serviceRate;
    /** Данное поле показывает количество задач, готовых к обслуживанию, хранящихся на данном сервере*/
    public int numberOfJobs;
    /** Данное поле хранит список пар пользователь - задача, которые, в текущий момент, храняться
     * на данном сервере и готовы к получению обслуживания*/
    public ArrayList<WorkUser> workUsersOnServer;
    /** Данное поле хранит список пар пользователь - задача, которые, в текущий момент, переносятся
     * на данный сервер */
    public ArrayList<WorkUser> transferWorks;
    public double windowInOfPreviousWork;

    /**
     * Данный конструктор создаёт объект, который обслуживает и перемщает задачи пользователей
     * @param i Номер текущего сервера
     * @param serviceRate Интенсивность обслуживания задач текущим сервером
     */
    public Server(int i, double serviceRate) {
        this.numberOfLocation = i;
        this.numberOfJobs = 0;
        this.serviceRate = serviceRate;
        this.windowInOfPreviousWork = 0;
        workUsersOnServer = new ArrayList<>();
        transferWorks = new ArrayList<>();
    }

    /**
     * Данный метод обслуживает задачи в соответсвии с их очерёдностью, а так отвечает за перенос
     * задач с других серверов на текущий
     * @param currentTime Текущий момент времени
     */
    public void getService(double currentTime) {

        if  (workUsersOnServer.size() != 0) {
            serviceWorksOnServer(currentTime);
        }
        if (transferWorks.size() != 0)
            serviceTransferWorks();
    }

    /**
     * Данный метод отвечает за обслуживание текущего списка работ на сервере
     * @param currentTime
     */
    public void serviceWorksOnServer(double currentTime) {
        int i = 0;
        while (i < workUsersOnServer.size()) {
            WorkUser tmpWorkUser = workUsersOnServer.get(i);
            if (tmpWorkUser.currentProcessingWorkOnServer) {
                double coeffUdalennost = 1;
                tmpWorkUser.increaseWorkProcessing(coeffUdalennost *
                        this.serviceRate);
                tmpWorkUser.setCurrentProcessingWorkOnServer(false);
                if (tmpWorkUser.statusWorkFinished) {
                    this.removeWork(tmpWorkUser, currentTime);
                    if (workUsersOnServer.size() > 0)
                        workUsersOnServer.get(i % this.numberOfJobs).setCurrentProcessingWorkOnServer(true);
                } else {
                    workUsersOnServer.get((i + 1) % this.numberOfJobs).setCurrentProcessingWorkOnServer(true);
                }
                return;
            }
            i++;
        }
    }

    /**
     * Данный метод занимается процессом переноса задач на текущий сервер
     */
    public void serviceTransferWorks() {
        for (int i = 0; i < transferWorks.size(); i++) {
            transferWorks.get(i).decreaseTransferTime();
        }
        int i = 0;
        while (i < transferWorks.size()) {
            WorkUser tmp = transferWorks.get(i);
            if (tmp.timeToTransfer <= 0) {
                tmp.transferStatus = false;
                this.transferWorks.remove(tmp);
                addNewWork(tmp);
            } else i++;
        }
    }

    /**
     * Данный метод добавляет на сервер новую задачу
     * @param tmp Новая задача
     */
    public void addNewWork(WorkUser tmp) {
        if (this.numberOfJobs == 0)
            tmp.setCurrentProcessingWorkOnServer(true);
        tmp.setBeginCountedStatus();
        this.numberOfJobs++;
        this.workUsersOnServer.add(tmp);
    }

    /**
     * Данный метод добавляет на сервер новую задачу с другого сервера. Данная задача помещается
     * в список переносящихся задач
     * @param tmp Новая задача, которая только переноситься на текущий сервер
     */
    public void addNewTransferWork(WorkUser tmp) {
        if (Main.ADD_TRANSFER_TIME) {
            this.transferWorks.add(tmp);
            tmp.changeWorkLocation(this.numberOfLocation);
        } else {
            this.addNewWork(tmp);
            tmp.changeWorkLocation(this.numberOfLocation);
        }
    }

    /**
     * Данный метод удаляет выполненную задачу с сервера, и записывает данной задаче значение задержки
     * @param tmp Выполненная задача
     * @param currentTime Текущий момент времени
     */
    public void removeWork(WorkUser tmp, double currentTime) {
        tmp.statusOfProcessing = false;
        this.numberOfJobs--;
        this.workUsersOnServer.remove(tmp);
        tmp.delay = currentTime - tmp.workInfo.windowIn;
        tmp.ageOfInformation = (Math.pow(currentTime - this.windowInOfPreviousWork, 2) - Math.pow(currentTime - tmp.workInfo.windowIn, 2)) / 2;
        this.windowInOfPreviousWork = tmp.workInfo.windowIn;
    }

    /**
     * Данный метод удаляет задачу с данного сервера для того, что бы перенести данную задачу на
     * другой сервер
     * @param tmp Задача, которая переносится на другой сервер
     */
    public void removeWorkToSwitchServer(WorkUser tmp) {
        this.numberOfJobs--;
        this.workUsersOnServer.remove(tmp);
    }

}
