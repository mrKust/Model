import java.util.Random;

public class WorkUser {

    public int userNumber;
    public double workProcessingValue;
    public Pair workInfo;
    public boolean statusFinishedOrUnfinished;// true работа выполнена, false ещё не выполнена
    public int userLocation;
    public int workLocation;
    public boolean statusOfProcessing;//показывает получает ли работа обслуживание(true) или
    // она переносится и не обрабатывается(false)
    public boolean statusOfBeginingCount;//true работа уже начала выполняться
    //на сервере , false ещё не начала
    public int timeInCurrentLocation; //показывает сколько времени проведёт в данной области
    //если время достигло 0, то пользователь выбирает новую область для перехода
    public double q;
    public double a;
    public double d;
    public int delay;
    public boolean currentProcessingWorkOnServer;//показывает обрабатывается работа
    //сервером на данном кванте или нет false не обрабатывается, true обрабатывается

    public WorkUser(int i, int numberOfLocation, double windowIn, double workSize, double qIn,
                    double sizeOfQuant) {

        userNumber = i;
        userLocation = numberOfLocation;
        workLocation = numberOfLocation;
        workProcessingValue = 0;
        this.statusFinishedOrUnfinished = false;
        this.statusOfProcessing = true;
        this.statusOfBeginingCount = false;
        this.currentProcessingWorkOnServer = false;
        this.delay = 0;
        this.q = qIn;
        this.timeInCurrentLocation = (int) Math.ceil(- (Math.log(Math.random()) / this.q) / sizeOfQuant);//это надо спросить и пофиксить
        workInfo = new Pair(windowIn, workSize);
        System.out.println("User №" + this.userNumber + " from area " + this.userLocation +
                " have work size = " + this.workInfo.workSize + " windowIn = " + workInfo.windowIn);

    }

    public void increaseWorkProcessing(double serviceRate) {
        this.workProcessingValue += serviceRate;
        this.decreaseTimeInCurrentLocation();
        this.checkWorkStatus();
    }

    public void changeUserLocation(int newLocation) {
        this.userLocation = newLocation;
    }

    public void changeWorkLocation(int newLocation) {
        this.workLocation = newLocation;
    }

    public void decreaseTimeInCurrentLocation() {
        this.timeInCurrentLocation--;
    }

    public void checkWorkStatus() {
        if (workProcessingValue >= workInfo.workSize) {// если работа выполнена
            this.statusFinishedOrUnfinished = true;
            System.out.println("User's №" + this.userNumber + " from area " + this.workLocation +
                    " have done his job with rating " + this.workProcessingValue +
                    " from " + this.workInfo.workSize + " windowIn = " + workInfo.windowIn);
        } else {// если работа ещё не выполнена
            this.statusFinishedOrUnfinished = false;
            System.out.println("User's №" + this.userNumber + " from area " + this.workLocation +
                    " have progress " + this.workProcessingValue +
                    " from " + this.workInfo.workSize + " windowIn = " + workInfo.windowIn);
        }
    }

    public void setStatusOfBeginingCount(boolean status) {
        this.statusOfBeginingCount = status;
    }

    public void setStatusWorking() {
        this.statusOfProcessing = true;
    }

    public void setStatusTransfer(int currentWindow) {
        this.statusOfProcessing = false;
    }

    public void setCurrentProcessingWorkOnServer(boolean status) {
        this.currentProcessingWorkOnServer = status;
    }
}