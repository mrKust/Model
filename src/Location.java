import java.util.ArrayList;

/**
 * Данные класс отвечает за области. В данном классе храниться сервер, закрепленной за данной
 * областью, временная шкала данной области. Так же в данном классе осуществляется подсчёт средних
 * значений задержки, выходной интенсивности, размера работы
 */
public class Location {

    /** Данное поле хранит номер данной локации*/
    public int numberOfThisLocation;
    /** Данное поле хранит сервер, который обслуживает задачи в данной области*/
    public Server server;
    /** Данное поле хранит размер задач и расписание того, когда они попадают в систему*/
    public ArrayList<WorkUser> inputStream;
    /** Данное поле хранит значение коэффициента необходимого для рассчёта времени перемещения
     * пользователя до следующей локации*/
    public double q;
    /** Данное поле хранит значение коэффициента необходимого при рассчёте времени необходимого
     * для перемещения задачи пользователя с серверов одной области на сервера другой области */
    public double d;
    /** Данное поле хранит в себе размер одного кванта времени*/
    public double sizeOfQuant;
    /** Данное поле хранит все себе такое вспомагательное значение, как суммарный объём задач всех
     * пользователей */
    public double lengthOfAllWorks;

    /** Данное поле хранит в себе такое вспомгательное значение, как общее количество заявок(задач)
     * пользователей в этой области */
    public int numberOfWorksInLocation;

    /**
     * В данном конструкторе формирются все сущности необходимые для корректного
     * функционирования области, а так же вызывается метод, который формирует входную очередь
     * @param lyambda Текущее значение входной интенсивности
     * @param time Данный параметр означает какое условное количество единиц времени производится
     *             моделирование
     * @param q Коэффициент для рассчёта времени перемещения пользователя
     *          до следующей локации
     * @param numberOfThisLocation Номер текущей области
     * @param quant Данный параметр означает размер кванта, то есть размер шага с которым мы
     *              двигаемся по временной шкале каждой локации
     * @param d Коэффициент для рассчёта времени необходимого для перемещения задачи
     *          пользователя с серверов одной области на сервера другой области
     * @param serviceRate Данный параметр означает, с какой интенсивностью серевер обрабатывает
     *                    задачи пользователей
     */
    public Location(float lyambda, double time, double q, int numberOfThisLocation, double quant,
                    double d, double serviceRate) {
        this.numberOfThisLocation = numberOfThisLocation;
        server = new Server(this.numberOfThisLocation, serviceRate);
        inputStream = new ArrayList<>();
        this.q = q;
        this.d = d;
        this.sizeOfQuant = quant;
        this.lengthOfAllWorks = 0;
        createInputStream(lyambda, time);
        this.numberOfWorksInLocation = inputStream.size();
    }

    /**
     * Коструктор используется при моделировании, когда значение входной интенсивности потока меньше 1
     */
    public Location(double time, double q, int numberOfThisLocation, double quant,
                    double d, double serviceRate) {
        this.numberOfThisLocation = numberOfThisLocation;
        server = new Server(this.numberOfThisLocation, serviceRate);
        inputStream = new ArrayList<>();
        this.q = q;
        this.d = d;
        this.sizeOfQuant = quant;
        this.lengthOfAllWorks = 0;
        createInputStream(0);
        this.numberOfWorksInLocation = inputStream.size();
    }

    /**
     * Данный метод формирует входную очередь с заданной интенсивностью
     * @param lyambda значение входной интенсивности
     * @param time длина временной линии
     */
    public void createInputStream(float lyambda, double time) {

        /*if (numberOfThisLocation == 0)
            inputStream.add(new WorkUser(1, numberOfThisLocation, 0.00, 6, this.sizeOfQuant, this.d));
        inputStream.add(new WorkUser(2, numberOfThisLocation, 0.01, 3,
              this.sizeOfQuant, this.d));
*/
        //int tmpSize = (int) Math.ceil(1 / this.sizeOfQuant);
        int tmpSize = (int) Math.ceil(- (Math.log(Math.random()) / 1) / this.sizeOfQuant);

        /*int tmpSize = (int) Math.ceil(- (Math.log(Math.random()) / 2) / this.sizeOfQuant) +
                (int) Math.ceil(- (Math.log(Math.random()) / 2) / this.sizeOfQuant);*/

        double tmpWindowIn = - (Math.log(Math.random()) / lyambda);
        this.lengthOfAllWorks += tmpSize;
        int userNumber = 0;

        inputStream.add(new WorkUser(userNumber, numberOfThisLocation, tmpWindowIn, tmpSize,
                this.sizeOfQuant, this.d));
        userNumber++;

        while (inputStream.get(userNumber - 1).workInfo.windowIn <= time) {

            //tmpSize = (int) Math.ceil(1 / this.sizeOfQuant);
            tmpSize = (int) Math.ceil(- (Math.log(Math.random()) / 1) / this.sizeOfQuant);

            /*int val1 = (int) Math.ceil(- (Math.log(Math.random()) / 2) / this.sizeOfQuant);
            int val2 = (int) Math.ceil(- (Math.log(Math.random()) / 2) / this.sizeOfQuant);
            tmpSize = val1 + val2;*/


            tmpWindowIn = - (Math.log(Math.random()) / lyambda);
            inputStream.add(new WorkUser(userNumber, numberOfThisLocation,
                    inputStream.get(userNumber - 1).workInfo.windowIn + tmpWindowIn, tmpSize,
                    this.sizeOfQuant, this.d));
            this.lengthOfAllWorks += tmpSize;
            userNumber++;
        }

    }

    /**
     * Данный метод формирует входную очередь для случаев с очень низкой интенсивностью
     * @param timeIn момент добавления заявки в систему
     */
    public void createInputStream(double timeIn) {

        if (this.numberOfThisLocation != 0)
            return;

        //int tmpSize = (int) Math.ceil(1 / this.sizeOfQuant);
        int tmpSize = (int) Math.ceil(- (Math.log(Math.random()) / 1) / this.sizeOfQuant);
        //int tmpSize = (int) Math.ceil(- (Math.log(Math.random()) / 2) / this.sizeOfQuant) +
        //        (int) Math.ceil(- (Math.log(Math.random()) / 2) / this.sizeOfQuant);

        double tmpWindowIn = timeIn + this.sizeOfQuant;
        this.lengthOfAllWorks += tmpSize;
        int userNumber = 0;

        inputStream.add(new WorkUser(userNumber, numberOfThisLocation, tmpWindowIn, tmpSize,
                this.sizeOfQuant, this.d));
        userNumber++;

    }

    /**
     * Данный метод проверяет наличие новых заявок пользователь - задача в системе. Если,
     * обнаруживается новая заявка, готовая к поступленю в систему, то тогда задача данного
     * пользователя добавляется на сервер текущей области
     * @param time текущее значение времени
     */
    public void processingAtLocation(double time) {
        this.server.getService(time);
        for (int i = 0; i < inputStream.size(); i++) {
            if ((time >= inputStream.get(i).workInfo.windowIn) &&
                    (inputStream.get(i).statusOfBeginingCount == false)) {
                server.addNewJob(inputStream.get(i));
            }
        }
    }

}
