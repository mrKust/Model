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

    /**
     * Данный конструктор создаёт объект, который обслуживает и перемщает задачи пользователей
     * @param i Номер текущего сервера
     * @param serviceRate Интенсивность обслуживания задач текущим сервером
     */
    public Server(int i, double serviceRate) {
        this.numberOfLocation = i;
        this.numberOfJobs = 0;
        this.serviceRate = serviceRate;
        workUsersOnServer = new ArrayList<>();
        transferWorks = new ArrayList<>();
    }

    /**
     * Данный метод обслуживает задачи в соответсвии с их очерёдностью, а так отвечает за перенос
     * задач с других серверов на текущий
     * @param currentTime Текущий момент времени
     */
    public void getService(double currentTime) {

        if (Main.currentLambda == Main.LAMBDA_TRACK_AVERAGE_NUMBER_OF_WORKS) {
            long currentVal2 = Main.averageNumberOfWorksInEachLocation.get(numberOfLocation);
            currentVal2 += workUsersOnServer.size();
            Main.averageNumberOfWorksInEachLocation.put(numberOfLocation, currentVal2);
        }

        if  ( (workUsersOnServer.size() != 0) || (transferWorks.size() != 0) ) {
            for (int i = 0; i < workUsersOnServer.size(); i++) {
                WorkUser tmpWorkUser = workUsersOnServer.get(i);
                if ((tmpWorkUser.statusOfProcessing && tmpWorkUser.currentProcessingWorkOnServer)) {
                     double coeffUdalennost = 1;
                     tmpWorkUser.increaseWorkProcessing(coeffUdalennost *
                             this.serviceRate);
                     tmpWorkUser.setCurrentProcessingWorkOnServer(false);
                     workUsersOnServer.get((i + 1) % this.numberOfJobs).setCurrentProcessingWorkOnServer(true);
                     break;
                }
            }
            for (int i = 0; i < workUsersOnServer.size(); i++) {
                WorkUser tmpWorkUser = workUsersOnServer.get(i);
                if (tmpWorkUser.statusFinishedOrUnfinished) {
                    tmpWorkUser.statusOfProcessing = false;
                    this.removeJob(tmpWorkUser, currentTime);
                }
            }

            for (int i = 0; i < transferWorks.size(); i++) {
                WorkUser tmp = transferWorks.get(i);
                if (tmp.transferStatus) {
                    if (tmp.timeToTransfer == 0) {
                        tmp.transferStatus = false;
                        this.transferWorks.remove(tmp);
                        addNewJob(tmp);
                        continue;
                    }
                    tmp.decreaseTransferTime();
                }
            }
        }
    }

    /**
     * Данный метод добавляет на сервер новую задачу
     * @param tmp Новая задача
     */
    public void addNewJob(WorkUser tmp) {
        if (this.numberOfJobs == 0)
            tmp.setCurrentProcessingWorkOnServer(true);
        tmp.setStatusOfBeginingCount(true);
        this.numberOfJobs++;
        this.workUsersOnServer.add(tmp);
    }

    /**
     * Данный метод добавляет на сервер новую задачу с другого сервера. Данная задача помещается
     * в список переносящихся задач
     * @param tmp Новая задача, которая только переноситься на текущий сервер
     */
    public void addNewTransferJob(WorkUser tmp) {
        this.transferWorks.add(tmp);
    }

    /**
     * Данный метод удаляет выполненную задачу с сервера, и записывает данной задаче значение задержки
     * @param tmp Выполненная задача
     * @param currentTime Текущий момент времени
     */
    public void removeJob(WorkUser tmp, double currentTime) {
        this.numberOfJobs--;
        this.workUsersOnServer.remove(tmp);
        tmp.delay = currentTime - tmp.workInfo.windowIn;
        Model.numberOfExitedWorks++;
        Model.summaryLengthOfWorks += tmp.workInfo.workSize;
        Model.summaryDelay += tmp.delay;
        Main.allFinishedWorks++;
        Main.allNumberOfTransfersOfEachFinishedWork += tmp.numberOfWorkTransfers;
        Main.numberOfUserWithCompletedTasksTransfers += tmp.numberOfUserTransfers;
        if (Main.numberOfTransfersOfCompletedWorks.get(tmp.numberOfWorkTransfers) == null) {
            Main.numberOfTransfersOfCompletedWorks.put(tmp.numberOfWorkTransfers, 1L);
        } else {
            long val = Main.numberOfTransfersOfCompletedWorks.get(tmp.numberOfWorkTransfers);
            val++;
            Main.numberOfTransfersOfCompletedWorks.put(tmp.numberOfWorkTransfers, val);
        }
    }

    /**
     * Данный метод удаляет задачу с данного сервера для того, что бы перенести данную задачу на
     * другой сервер
     * @param tmp Задача, которая переносится на другой сервер
     */
    public void removeJobToSwitchServer(WorkUser tmp) {
        this.numberOfJobs--;
        this.workUsersOnServer.remove(tmp);
    }

}
