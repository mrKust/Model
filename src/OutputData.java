/**
 * Данный класс содержит список входных и выходных параметров системы.
 * Данные параметры будут записаны в файл model.txt
 */
public class OutputData {

    /**
     * Данный параметр хранит значение входной интенсивности, с которым проводилось моделирование
     */
    private double lambdaIn;
    /**
     * Данный параметр хранит значение выходной интенсивности, полученное по итогам моделирования
     */
    private double lambdaOut;
    /**
     * Данный параметр хранит среднее значение размера задачи
     */
    private double mediumSizeOfWork;
    /**
     * Данный параметр хранит среднее кол-во переходов в один квант времени
     */
    private double transfersPerTime;
    /**
     * Данный параметр хранит среднее значение задержки, полученное по итогам моделирования
     */
    private double mD;
    /**
     * Данный параметр хранит среднее значение задержки, полученное при помощи формулы для систем пакетной обработки
     */
    private double mDTheoretical;

    private double mAgeOfInfTheor;
    private double mAgeOfInfModel;

    /**
     * Данный конструктор применяется для сохраненя выходных данных при значении входной интенсивности больше 1.
     * Так как формула, через которое получается mDTheoretical актуально для случаев с входной интенсивностью меньше 1.
     * @param lambdaIn Значение входной интенсивности
     * @param lambdaOut Значениы выходной интенсивности
     * @param mediumSizeOfWork Среднее значение размера задачи
     * @param transfersPerTime Среднее количество переходов в единицу времени
     */
    public OutputData(double lambdaIn, double lambdaOut, double mediumSizeOfWork, double transfersPerTime) {
        this.lambdaIn = lambdaIn;
        this.lambdaOut = lambdaOut;
        this.mediumSizeOfWork = mediumSizeOfWork;
        this.transfersPerTime = transfersPerTime;
    }

    /**
     * Данный конструктор применяется для сохраненя выходных данных при значении входной интенсивности меньше 1.
     * @param lambdaIn Значение входной интенсивности
     * @param lambdaOut Значениы выходной интенсивности
     * @param mediumSizeOfWork Среднее значение размера задачи
     * @param transfersPerTime Среднее количество переходов в единицу времени
     * @param mD Среднее значение задержки
     * @param mDTheoretical Средняя задержка, полученная при помощи формулы
     */
    public OutputData(double lambdaIn, double lambdaOut, double mediumSizeOfWork, double transfersPerTime,
                      double mD, double mDTheoretical, double mAgeOfInfTheor, double mAgeOfInfModel) {
        this.lambdaIn = lambdaIn;
        this.lambdaOut = lambdaOut;
        this.mediumSizeOfWork = mediumSizeOfWork;
        this.transfersPerTime = transfersPerTime;
        this.mD = mD;
        this.mDTheoretical = mDTheoretical;
        this.mAgeOfInfTheor = mAgeOfInfTheor;
        this.mAgeOfInfModel = mAgeOfInfModel;
    }

    @Override
    public String toString() {
        if (lambdaIn < 1.0) {
            return lambdaIn +
                    " " + lambdaOut +
                    " " + mediumSizeOfWork +
                    " " + transfersPerTime +
                    " " + mAgeOfInfTheor +
                    " " + mAgeOfInfModel +
                    " " + mDTheoretical +
                    " " + mD +
                    "\n";
        } else {
            return lambdaIn +
                    " " + lambdaOut +
                    " " + mediumSizeOfWork +
                    " " + transfersPerTime +
                    "\n";
        }
    }

    /**
     * Данный метод возвращает строку с параметрами для случаев с входной интенсивностью меньше 1
     * @return Строка с необходимыми параметрами
     */
    public String fullToString() {
        return lambdaIn +
                " " + lambdaOut +
                " " + mediumSizeOfWork +
                " " + transfersPerTime +
                " " + mD +
                " " + mDTheoretical +
                "\n";
    }

    /**
     * Данный метод возвращает строку с параметрами для случаев с входной интенсивностью больше 1
     * @return Строка с необходимыми параметрами
     */
    public String shortToString() {
        return lambdaIn +
                " " + lambdaOut +
                " " + mediumSizeOfWork +
                " " + transfersPerTime +
                "\n";
    }
}
