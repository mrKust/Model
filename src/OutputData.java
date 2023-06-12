public class OutputData {

    private double lambdaIn;
    private double lambdaOut;
    private double mediumSizeOfWork;
    private double transfersPerTime;
    private double mD;
    private double mDTheoretical;

    public OutputData(double lambdaIn, double lambdaOut, double mediumSizeOfWork, double transfersPerTime) {
        this.lambdaIn = lambdaIn;
        this.lambdaOut = lambdaOut;
        this.mediumSizeOfWork = mediumSizeOfWork;
        this.transfersPerTime = transfersPerTime;
    }

    public OutputData(double lambdaIn, double lambdaOut, double mediumSizeOfWork, double transfersPerTime, double mD, double mDTheoretical) {
        this.lambdaIn = lambdaIn;
        this.lambdaOut = lambdaOut;
        this.mediumSizeOfWork = mediumSizeOfWork;
        this.transfersPerTime = transfersPerTime;
        this.mD = mD;
        this.mDTheoretical = mDTheoretical;
    }

    public String fullToString() {
        return lambdaIn +
                " " + lambdaOut +
                " " + mediumSizeOfWork +
                " " + transfersPerTime +
                " " + mD +
                " " + mDTheoretical +
                "\n";
    }

    public String shortToString() {
        return lambdaIn +
                " " + lambdaOut +
                " " + mediumSizeOfWork +
                " " + transfersPerTime +
                "\n";
    }
}
