import java.util.ArrayList;

/**
 * Данный класс отвечает за обработку задач на той области, за которой закреплён сервер
 */
public class Server {

    /** Данное поле хранит номер локации, которую обслуживает сервер*/
    int numberOfLocation;
    /** Данное поле хранит значение интенсивности, с которой сервер обрабатывает задачи
     * пользователей*/
    double serviceRate;
    /** Данное поле показывает количество задач, готовых к обслуживанию, хранящихся на данном сервере*/
    int numberOfJobs;
    /** Данное поле хранит список пар пользователь - задача, которые, в текущий момент, храняться
     * на данном сервере и готовы к получению обслуживания*/
    ArrayList<WorkUser> workUsersOnServer;
    /** Данное поле хранит список пар пользователь - задача, которые, в текущий момент, переносятся
     * на данный сервер */
    ArrayList<WorkUser> transferWorks;

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
        if (workUsersOnServer.size() != 0) {
            for (int i = 0; i < workUsersOnServer.size(); i++) {
                WorkUser tmpWorkUser = workUsersOnServer.get(i);
                //tmpWorkUser.decreaseTimeInCurrentLocation();
                if ((tmpWorkUser.statusOfProcessing == true &&
                        (tmpWorkUser.currentProcessingWorkOnServer == true))) {
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
                if (tmpWorkUser.statusFinishedOrUnfinished == true) {
                    tmpWorkUser.statusOfProcessing = false;
                    this.removeJob(tmpWorkUser, currentTime);
                    continue;
                }
            }

            for (int i = 0; i < transferWorks.size(); i++) {
                WorkUser tmp = transferWorks.get(i);
                if (tmp.transferStatus == true) {
                    tmp.decreaseTransferTime();
                    if (tmp.timeToTransfer == 0) {
                        tmp.transferStatus = false;
                        this.workUsersOnServer.add(tmp);
                        this.transferWorks.remove(tmp);
                        this.numberOfJobs++;
                    }
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
        System.out.println("Work added to server. User number " + tmp.userNumber);
    }

    /**
     * Данный метод добавляет на сервер новую задачу с другого сервера. Данная задача помещается
     * в список переносящихся задач
     * @param tmp Новая задача, которая только переноситься на текущий сервер
     */
    public void addNewTransferJob(WorkUser tmp) {
        this.transferWorks.add(tmp);
        System.out.println("Work added to server transfer list. User number " + tmp.userNumber);
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
        System.out.println("Work removed from server. User number " + tmp.userNumber);
    }

    /**
     * Данный метод удаляет задачу с данного сервера для того, что бы перенести данную задачу на
     * другой сервер
     * @param tmp Задача, которая переносится на другой сервер
     */
    public void removeJobToSwitchServer(WorkUser tmp) {
        this.numberOfJobs--;
        this.workUsersOnServer.remove(tmp);
        System.out.println("Work removed to change server. User number " + tmp.userNumber);
    }

}
