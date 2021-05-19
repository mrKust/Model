import java.util.ArrayList;

public class Server {

    int numberOfLocation;
    double serviceRate;
    int numberOfJobs;
    int defaultServiceRate;
    ArrayList<WorkUser> workUsersOnServer;
    ArrayList<WorkUser> transferWorks;

    public Server(int i) {
        this.defaultServiceRate = 1;
        this.numberOfLocation = i;
        this.numberOfJobs = 0;
        this.serviceRate = this.defaultServiceRate;
        workUsersOnServer = new ArrayList<>();
        transferWorks = new ArrayList<>();
    }

    public void getService(double currentTime) {
        if (workUsersOnServer.size() != 0) {
            for (int i = 0; i < workUsersOnServer.size(); i++) {
                WorkUser tmpWorkUser = workUsersOnServer.get(i);
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

    public void addNewJob(WorkUser tmp) {
        if (this.numberOfJobs == 0)
            tmp.setCurrentProcessingWorkOnServer(true);
        tmp.setStatusOfBeginingCount(true);
        this.numberOfJobs++;
        this.workUsersOnServer.add(tmp);
        System.out.println("Work added to server. User number " + tmp.userNumber);
    }

    public void addNewTransferJob(WorkUser tmp) {
        this.transferWorks.add(tmp);
        System.out.println("Work added to server transfer list. User number " + tmp.userNumber);
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
