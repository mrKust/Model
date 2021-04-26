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
                if ((workUsersOnServer.get(i).statusOfProcessing == true &&
                        (workUsersOnServer.get(i).currentProcessingWorkOnServer == true))) {
                    double coeffUdalennost = 1;
                    workUsersOnServer.get(i).increaseWorkProcessing(coeffUdalennost *
                            this.serviceRate);
                    workUsersOnServer.get(i).setCurrentProcessingWorkOnServer(false);
                    workUsersOnServer.get((i + 1) % this.numberOfJobs).setCurrentProcessingWorkOnServer(true);
                    //workUsersOnServer.get(i).delay++;
                    break;
                }
            }
            for (int i = 0; i < workUsersOnServer.size(); i++) {
                if (workUsersOnServer.get(i).statusFinishedOrUnfinished == true) {
                    workUsersOnServer.get(i).statusOfProcessing = false;
                    this.removeJob(workUsersOnServer.get(i), currentTime);
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
        this.countServiceRate();
        System.out.println("Work added to server. User number " + tmp.userNumber);
    }

    public void removeJob(WorkUser tmp, double currentTime) {
        this.numberOfJobs--;
        this.workUsersOnServer.remove(tmp);
        this.countServiceRate();
        tmp.delay = (int) (currentTime - tmp.workInfo.windowIn);
        System.out.println("Work removed from server. User number " + tmp.userNumber);
    }

    public void countServiceRate() {
        int numberOfActiveWorks = 0;
        for (int i = 0; i < this.workUsersOnServer.size(); i++) {
            if (this.workUsersOnServer.get(i).statusOfProcessing == true)
                numberOfActiveWorks++;
        }
    }
}
