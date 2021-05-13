
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
    public double d;
    public double sizeOfQuant;
    public double delay;
    public boolean currentProcessingWorkOnServer;//показывает обрабатывается работа
    //сервером на данном кванте или нет false не обрабатывается, true обрабатывается

    public WorkUser(int i, int numberOfLocation, double windowIn, double workSize, double sizeOfQuant,
                    double dIn) {

        userNumber = i;
        userLocation = numberOfLocation;
        workLocation = numberOfLocation;
        workProcessingValue = 0;
        this.statusFinishedOrUnfinished = false;
        this.statusOfProcessing = true;
        this.statusOfBeginingCount = false;
        this.currentProcessingWorkOnServer = false;
        this.delay = 0;
        this.d = dIn;
        this.sizeOfQuant = sizeOfQuant;
        this.timeInCurrentLocation = (int) Math.ceil(- (Math.log(Math.random()) / 1) /
                this.sizeOfQuant);
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
        this.countTimeInNewLocation();
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

    public void countTimeInNewLocation() {
        this.timeInCurrentLocation = (int) Math.ceil(- (Math.log(Math.random()) / 1) / sizeOfQuant);
    }

    public void setCurrentProcessingWorkOnServer(boolean status) {
        this.currentProcessingWorkOnServer = status;
    }
}
