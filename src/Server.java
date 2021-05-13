import java.util.ArrayList;

public class Server {

    int numberOfLocation;
    double serviceRate;
    int numberOfJobs;
    int defaultServiceRate;
    ArrayList<WorkUser> workUsersOnServer;

    public Server(int i) {
        this.defaultServiceRate = 1;
        this.numberOfLocation = i;
        this.numberOfJobs = 0;
        this.serviceRate = this.defaultServiceRate;
        workUsersOnServer = new ArrayList<>();
    }

    public void getService(double currentTime) {
        if (workUsersOnServer.size() != 0) {
            for (int i = 0; i < workUsersOnServer.size(); i++) {
                WorkUser tmpWorkUser = workUsersOnServer.get(i);
                if ((tmpWorkUser.statusOfProcessing == true &&
                        (tmpWorkUser.currentProcessingWorkOnServer == true))) {
                    //если работа не переносится и готова к обслуживанию
                    if (tmpWorkUser.transferStatus == false) {
                        double coeffUdalennost = 1;
                        tmpWorkUser.increaseWorkProcessing(coeffUdalennost *
                                this.serviceRate);
                        tmpWorkUser.setCurrentProcessingWorkOnServer(false);
                        workUsersOnServer.get((i + 1) % this.numberOfJobs).setCurrentProcessingWorkOnServer(true);
                        break;
                    } else { //если работа пока переносится
                        tmpWorkUser.decreaseTransferTime();
                        if (tmpWorkUser.timeToTransfer == 0)
                            tmpWorkUser.transferStatus = false;
                    }
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
        }
    }

    public void addNewJob(WorkUser tmp) {
        if (this.numberOfJobs == 0)
            tmp.setCurrentProcessingWorkOnServer(true);
        tmp.setStatusOfBeginingCount(true);
        this.numberOfJobs++;
        this.workUsersOnServer.add(tmp);
        System.out.println("Work added to server. User number " + tmp.userNumber);
    }

    public void removeJob(WorkUser tmp, double currentTime) {
        this.numberOfJobs--;
        this.workUsersOnServer.remove(tmp);
        tmp.delay = currentTime - tmp.workInfo.windowIn;
        System.out.println("Work removed from server. User number " + tmp.userNumber);
    }

    public void removeJobToSwitchServer(WorkUser tmp) {
        this.numberOfJobs--;
        this.workUsersOnServer.remove(tmp);
        System.out.println("Work removed to change server. User number " + tmp.userNumber);
    }

}
