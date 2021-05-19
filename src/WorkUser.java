
public class WorkUser {

    public int userNumber;
    public double workProcessingValue;
    public Pair workInfo;
    public boolean statusFinishedOrUnfinished;// true работа выполнена, false ещё не выполнена
    public int userLocation;
    public int workLocation;
    public boolean statusOfProcessing;//показывает данную работу вообще начинали обслуживать(true) или
    // её ещё вообще не обрабатывали(false)
    public boolean statusOfBeginingCount;//true работа уже начала выполняться
    //на сервере , false ещё не начала
    public boolean transferStatus; //показывает данная работа сейчас в состоянии переноса(true) или
    //данная работа уже находится на сервере и готова к обработке(false)
    public int timeInCurrentLocation; //показывает сколько времени проведёт в данной области
    //если время достигло 0, то пользователь выбирает новую область для перехода
    public int timeToTransfer;//показывает сколько времени данная работа будет переноситься
    // с одних серверов на другие
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
        this.transferStatus = false;
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

    public void transfer() {
        this.transferStatus = true;
        //this.timeToTransfer = 0;
        this.timeToTransfer = (int) Math.ceil(- (Math.log(Math.random()) / this.d) / this.sizeOfQuant);
    }

    public void decreaseTransferTime() {
        this.timeToTransfer--;
    }

    public void countTimeInNewLocation() {
        this.timeInCurrentLocation = (int) Math.ceil(- (Math.log(Math.random()) / 1) / sizeOfQuant);
    }

    public void setCurrentProcessingWorkOnServer(boolean status) {
        this.currentProcessingWorkOnServer = status;
    }
}
