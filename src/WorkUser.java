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
    public boolean statusWorkFinished;
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
    public boolean statusOfProcessing;
    /** Данное поле показывает данная пара пользователь - задача уже попала в систему или ещё нет
     * Значение true обозначает, что данная пара уже начала обслуживаться системой
     * Значение false обозначает, что даннвя пара ещё не начинала обслуживаться системой*/
    public boolean statusBeginCount;
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
    /** Данное поле хранит значение коэффициента необходимого при рассчёте времени необходимого
     * для перемещения задачи пользователя с серверов одной области на сервера другой области */
    public double d;
    /** Данное поле хранит в себе размер одного кванта времени*/
    public double sizeOfQuant;
    /** В данное поле записывается то сколько времени заявка провела в системе, в том случае
     * если задача будет успешна выполнена системой */
    public double delay;
    /** Данное поле показывает должна ли в данный момент времени обслуживаться задача сервером
     * области или же нет
     * Значение true обозначает, что данная пара обслуживается сервером в текущий момент
     * Значение false обозначает, что даннвя пара не обслуживается сервером в текущий момент*/
    public boolean currentProcessingWorkOnServer;//показывает обрабатывается работа
    //сервером на данном кванте или нет false не обрабатывается, true обрабатывается

    /**
     * Данное поле хранит в себе количество перносов задачи
     */
    public int numberOfWorkTransfers;

    /**
     * Данное поле хранит в себе количество переходов пользователя
     */
    public int numberOfUserTransfers;
    /**
     * Данный флаг показывает принимал ли когда-нибудь пользователь решение о том, чтобы не переносить задачу
     * Значение false - устанавливается в конструкторе
     * Значение true - устанавливается в случае, когда пользователь решает оставить задачу и не переносить её в
     * новую область
     */
    public boolean isEverAbandoned;
    public double prevTimeUpdate;

    /**
     * Данный конструктор создаёт заявку и записывает в неё необходимые данные, ат так же
     * выставляет флаги в необходимые позиции
     * @param i Номер пользователя
     * @param numberOfLocation Номер начальной области пользователя и его задачи
     * @param windowIn Время появления заявки в системе
     * @param workSize Размер задачи
     * @param sizeOfQuant Размер кванта
     * @param dIn Коэффициент для рассчёта времени необходимого для перемещения задачи
     *            пользователя с серверов одной области на сервера другой области
     */
    public WorkUser(int i, int numberOfLocation, double windowIn, double workSize, double sizeOfQuant,
                    double dIn) {

        userNumber = i;
        userLocation = numberOfLocation;
        workLocation = numberOfLocation;
        workProcessingValue = 0;
        this.statusWorkFinished = false;
        this.statusOfProcessing = true;
        this.statusBeginCount = false;
        this.currentProcessingWorkOnServer = false;
        this.transferStatus = false;
        this.delay = 0;
        this.d = dIn;
        this.sizeOfQuant = sizeOfQuant;
        this.timeInCurrentLocation = (int) Math.ceil(- (Math.log(Math.random()) / 1) /
                this.sizeOfQuant);
        workInfo = new Pair(windowIn, workSize);
        this.numberOfWorkTransfers = 0;
        this.numberOfUserTransfers = 0;
        this.isEverAbandoned = false;

        prevTimeUpdate = 0.0;
    }

    /**
     * Данный метод увеличивает значение текущего прогресса задачи пользователя на значение
     * интенсивности обработки задач сервером
     * @param serviceRate Значение интенсивности, с которой сервер обрабатывает задачу
     */
    public void increaseWorkProcessing(double serviceRate) {
        this.workProcessingValue += serviceRate;
        this.timeInCurrentLocation--;
        this.checkWorkStatus();
    }

    /**
     * Данный метод изменяет текущее положение пользователя на новое
     * @param newLocation Номер новой области, в которую переместился пользователь
     */
    public void changeUserLocation(int newLocation) {
        this.numberOfUserTransfers++;
        this.userLocation = newLocation;
        this.countTimeInNewLocation();
    }

    /**
     * Данный метод изменяет текущее положение задачи пользователя на новое
     * @param newLocation Номер новой области, на сервера которой пользователь решил перенести
     *                    свою задачу
     */
    public void changeWorkLocation(int newLocation) {
        this.workLocation = newLocation;
        this.numberOfWorkTransfers++;
        if (Main.ADD_TRANSFER_TIME)
            this.transfer();
    }

    /**
     * Данный метод проверяет была ли задача полностью решена, после последнего обращения сервера к
     * ней или ей ещё требуется дополнительное обслуживание
     */
    public void checkWorkStatus() {
        if (workProcessingValue >= workInfo.workSize) {// если работа выполнена
            this.statusWorkFinished = true;
        } else {// если работа ещё не выполнена
            this.statusWorkFinished = false;
        }
    }

    /**
     * Данный метод обновляет специальное поле статуса заявки, в случае если система приступила к
     * её обработке
     */
    public void setBeginCountedStatus() {
        if (!statusBeginCount)
            this.statusBeginCount = true;
    }

    /**
     * Данный метод устанавливает для заявки специальный статус и рассчитывает время необходимое для
     * переноса заявки на сервера новой области, в случае, если пользователь решит перенести свою
     * задачу
     */
    public void transfer() {
        this.transferStatus = true;
        this.timeToTransfer = (int) Math.ceil(- (Math.log(Math.random()) / this.d) / this.sizeOfQuant);
    }

    /**
     * Данный метод уменьшает время необходимое на перемещение задачи с серверов одной области на
     * сервера другой области
     */
    public void decreaseTransferTime() {
        this.timeToTransfer--;
    }

    /**
     * Данный метод рассчитывает какое количество времени проведёт пользователь в новой области
     */
    public void countTimeInNewLocation() {
        this.timeInCurrentLocation = (int) Math.ceil(- (Math.log(Math.random()) / 1) / sizeOfQuant);
    }

    /**
     * Данный метод устанавливает статус, который показывает будет ли сервер в текущий момент
     * времени обслуживать данную заявку или же нет
     * @param status Значение статуса заявки в данный момент времени
     */
    public void setCurrentProcessingWorkOnServer(boolean status) {
        this.currentProcessingWorkOnServer = status;
    }
}
