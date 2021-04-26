import java.util.ArrayList;

public class Location {

    public int numberOfThisLocation;
    public Server server;
    public ArrayList<WorkUser> inputStream;
    public double q;
    public double sizeOfQuant;
    public double lengthOfAllWorks;
    public int numberOfRequests;

    public Location(float lyambda, double time, double q, int numberOfThisLocation, double quant) {
        this.numberOfThisLocation = numberOfThisLocation;
        server = new Server(this.numberOfThisLocation);
        inputStream = new ArrayList<>();
        this.q = q;
        this.sizeOfQuant = quant;
        this.lengthOfAllWorks = 0;
        this.numberOfRequests = 0;
        createInputStream(lyambda, time);
    }

    public void createInputStream(float lyambda, double time) {
        /*inputStream.add(new WorkUser(1, numberOfThisLocation, 0.0289, 14));
        inputStream.add(new WorkUser(2, numberOfThisLocation, 0.03, 2));
        inputStream.add(new WorkUser(3, numberOfThisLocation, 0.05, 5));*/

        int tmpSize = (int) Math.ceil(- (Math.log(Math.random()) / 1) / this.sizeOfQuant);
        double tmpWindowIn = - (Math.log(Math.random()) / lyambda);
        this.lengthOfAllWorks += tmpSize;
        int userNumber = 0;

        inputStream.add(new WorkUser(userNumber, numberOfThisLocation, tmpWindowIn, tmpSize, this.q));
        userNumber++;

        while (inputStream.get(userNumber - 1).workInfo.windowIn <= time) {
            tmpSize = (int) Math.ceil(- (Math.log(Math.random()) / 1) / this.sizeOfQuant);
            tmpWindowIn = - (Math.log(Math.random()) / lyambda);
            inputStream.add(new WorkUser(userNumber, numberOfThisLocation,
                    inputStream.get(userNumber - 1).workInfo.windowIn + tmpWindowIn, tmpSize,
                    this.q));
            this.lengthOfAllWorks += tmpSize;
            userNumber++;
        }
        this.numberOfRequests = userNumber;

    }


    public void processingAtLocation(double time) {
        this.server.getService(time);
        for (int i = 0; i < inputStream.size(); i++) {
            if ((time >= inputStream.get(i).workInfo.windowIn) &&
                    (inputStream.get(i).statusOfBeginingCount == false)) {
                server.addNewJob(inputStream.get(i));
            }
        }
        System.out.println("Out t = " + time);
    }

    public double countMediumLengthOfWork() {
        return (double) this.lengthOfAllWorks / this.inputStream.size();
    }

    public int countN() {
        int finishedWorks = 0;
        for (int k = 0; k < inputStream.size(); k++) {
            if (inputStream.get(k).statusFinishedOrUnfinished == true)
                finishedWorks++;
        }
        System.out.println("N = " + finishedWorks);
        int N = finishedWorks;
        return N;
    }

    public double countMd(int N) {
        int commonDelay = 0;
        for (int k = 0; k < inputStream.size(); k++) {
            if (inputStream.get(k).statusFinishedOrUnfinished == true) {
                commonDelay += inputStream.get(k).delay;
                continue;
            }
        }
        if (N == 0) {
            System.out.println("M[D] can't be counted because N = 0");
            return 0;
        } else {
            System.out.println("M[D] = " + commonDelay / N);
            return commonDelay / N;
        }
    }

    public double countLyambda_out(int N, double time) {
        double lyambda_out = (double)N / time;
        return lyambda_out;
    }

}
