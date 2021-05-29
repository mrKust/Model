/**
 * Данный класс представляет собой пользователя и его задачу
 */
public class WorkUser {

    /** Номер пользователя в системе*/
    public int userNumber;
    /** Данное поле показывает, текущий прогресс выполнения данной задачи*/
    public double workProcessingValue;
    /** В данном классе храняться данные о данной паре пользователь - задача.
     * Первое значение пары - момент, когда пользователь поступает в систему
     * Второе значение пары - объём данной задачи
     * */
    public Pair workInfo;
    /** Данное поле показывает выполнена ли данная задача или нет
     * Значение true обозначает, что работа выполнена до конца
     * Значение false обозначает, что работа не выполнена до конца*/
    public boolean statusFinishedOrUnfinished;
    /** Данное поле показывает номер области, в которой располагается сам пользователь в текущий
     * момент*/
    public int userLocation;
    /** Данное поле показывает номер области, на серверах которой сейчас располагается задача
     * пользователя в текущий момент*/
    public int workLocation;
    /** Данное поле показывает данная пара пользователь - задача в текущий момент получает обслуживание
     * или же она переноситься/готова и обслуживание данной пары не производится
     * Значение true обозначает, что данная пара обслуживется системой в текущий момент
     * Значение false обозначает, что даннвя пара не обслуживается системой в текущий момент*/
    public boolean statusOfProcessing;//показывает данную работу вообще начинали обслуживать(true) или
    // её ещё вообще не обрабатывали(false)
    /** Данное поле показывает данная пара пользователь - задача уже попала в систему или ещё нет
     * Значение true обозначает, что данная пара уже начала обслуживаться системой
     * Значение false обозначает, что даннвя пара ещё не начинала обслуживаться системой*/
    public boolean statusOfBeginingCount;//true работа уже начала выполняться
    //на сервере , false ещё не начала
    /** Данное поле показывает данная пара пользователь - задача в данный момент переносит
     * данные на другой сервер и не получает обслуживание, или получает обслуживание в штатном режиме
     * Значение true обозначает, что данная пара нахожится в состоянии переноса данных и не получает
     * обслуживание
     * Значение false обозначает, что даннвя пара получает обслуживание в штатном режиме */
    public boolean transferStatus; //показывает данная работа сейчас в состоянии переноса(true) или
    //данная работа уже находится на сервере и готова к обработке(false)
    /** Данное поле показывает сколько ещё времени пользователь проведёт в текущей локации*/
    public int timeInCurrentLocation; //показывает сколько времени проведёт в данной области
    //если время достигло 0, то пользователь выбирает новую область для перехода
    /** Данное поле показывает какое время потребуется для переноса пары пользователь - задача
     * на другой сервер в случае, если пользователь решит перенести свою задачу на сервера другой
     * области*/
    public int timeToTransfer;//показывает сколько времени данная работа будет переноситься
    // с одних серверов на другие
    public double d;
    public double sizeOfQuant;
    public double delay;
    /** Данное поле показывает должна ли в данный момент времени обслуживаться задача сервером
     * области или же нет
     * Значение true обозначает, что данная пара обслуживается сервером в текущий момент
     * Значение false обозначает, что даннвя пара не обслуживается сервером в текущий момент*/
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
