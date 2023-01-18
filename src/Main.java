import java.util.HashMap;
import java.util.Map;

/**
 * В данном классе задаются все параметры необходимые для моделирования системы. Так же
 * из этого класса запускается моделирование систем
 * */

public class Main {

    /** Данный параметр используется для рассчёта времени перемещения пользователя
     * до следующей локации. Для симметричной модели значение этого параметра всегда равно 1*/
    public static double q = 1;
    /** Данный параметр используется для рассчёта времени необходимого для перемещения
     * задачи пользователя с серверов одной области на сервера другой области*/
    public static double d = 1;
    /** Данный параметр означает вероятность, с которой пользователь, при перемещении в
     * следующую область, решит перенести свою задачу на сервера следующей области */
    public static double a = 0.5;
    /** Данный параметр означает размер кванта, то есть размер шага с которым мы двигаемся по
     * временной шкале каждой локации*/
    public static double quant = 0.01;
    /** Данный параметр означает количество областей с которыми производиться моделирование*/
    public static int numberOfLocations = 24;
    /** Данный параметр означает какое условное количество единиц времени производится
     * моделирование*/
    public static float T = 2000;
    /** Данный параметр означает, с какой интенсивностью серевер обрабатывает задачи пользователей */
    public static double serviceRate = 1.0;
    /** Данный параметр задаёт начальную входную интенсивность, с которой начинается моделирование */
    public static final float LAMBDA_IN_START = 0.1F;
    /** Данный параметр задаёт финальную входную интенсивность, при достижении которой моделирование заканчивается */
    public static final float LAMBDA_IN_FINISH = 1.3F;
    /** Данный параметр определяет входную интенсивность, для которой будет посчитано среднее значение кол-ва задач
     * в один квант времени для каждой области */
    public static final float LAMBDA_TRACK_AVERAGE_NUMBER_OF_WORKS = 0.5F;

    /** Данное поле отображает текущую входную интенсивность для которой сейчас происходит моделирование */
    public static float currentLambda = 0.0F;
    /** Данное поле хранит количество пользователей, которые покидают область до того, как завершился рассчёт их
     * задачи */
    public static int usersLeaveUnfinished = 0;
    /** Данное поле хранит количество пользователей, которые покидают систему после того, как было завершено
     * вычисление их задачи */
    public static int allFinishedWorks = 0;
    /** Данное поле хранит колчество переносов пользователями своих задач */
    public static int countOfWorkTransfers = 0;
    /** Данный параметр хранит значение необходимое для отображения итоговых данных по каждой области.
     * true - выводятся данные в формате номер области - номер задачи - объём выполненной работы - общий объём задачи
     * false - данные о задачах в области не выводятся*/
    public static final boolean SHOW_LOCATION_SUMMARY = false;
    /** Данный параметр хранит значение необходимое для изменения работы с временем переноса
     * true - при переносе задачи с одного сервера на другой требуется дополнительное время, на перенос с одного
     * сервера на другой. Время вычисляется, как exp(1).
     * false - при переносе задачи с одного сервера на другой дополнительное время не требуется, оно приравнивается к 0
     */
    public static final boolean ADD_TRANSFER_TIME = false;
    /** Данное поле хранит среднее значение задач в каждый квант времени для каждой области */
    public static Map<Integer, Integer> averageNumberOfWorksInEachLocation = new HashMap<>();

    /**
     * В данном методе производиться заупкск моделирования с заданным значениями параметров, а так
     * же изменение параметра входной интенсивности. Так же данный метод осуществляет запись полученных
     * данных в выходной файл
     */
    public static void main(String[] args) {

        FileWork fileUbuntu = new FileWork("D:\\Pereezd\\Labs\\Научка\\Model\\model.txt", false);
        FileWork fileLoc2D = new FileWork("D:\\Pereezd\\Labs\\Научка\\Model\\LocationsAverangeNumber2D.txt", false);

        Model modelLowIntensity = new Model(a, q, d, quant, numberOfLocations, T, serviceRate);
        //modelLowIntensity.getModelingForLowIntensity();
        double transfersPerTime = (double) countOfWorkTransfers / T;
        fileUbuntu.write(0, modelLowIntensity.lyambda_out, modelLowIntensity.mediumSizeOfWork,
                transfersPerTime, modelLowIntensity.mD, 0.0);
        System.out.println("lambda = low" + " M[D] = " + modelLowIntensity.mD + " lambda_out = " +
                modelLowIntensity.lyambda_out);
        System.out.println(usersLeaveUnfinished + " / " + allFinishedWorks + " = " +
                (double)usersLeaveUnfinished/ allFinishedWorks);
        System.out.println("Average transfers number " + transfersPerTime + "\n");

        for (int i = 0; i < numberOfLocations; i++) {
            averageNumberOfWorksInEachLocation.put(i, 0);
        }

        for (float lyambda = LAMBDA_IN_START; lyambda <= LAMBDA_IN_FINISH; lyambda += 0.1) {
            currentLambda = lyambda;
            usersLeaveUnfinished = 0;
            allFinishedWorks = 0;
            countOfWorkTransfers = 0;
            System.out.println("lambda = " + lyambda);

            Model model = new Model(lyambda, a, q, d, quant, numberOfLocations, T, serviceRate);
            model.getModeling();
            //double dPoLittle = model.averageNumberOfWorks / lyambda;
            if (lyambda < 1.0) {
                fileUbuntu.write(lyambda, model.lyambda_out, model.mediumSizeOfWork, transfersPerTime, model.mD,
                        model.mDTheoretical);
            } else {
                fileUbuntu.write(lyambda, model.lyambda_out, model.mediumSizeOfWork, transfersPerTime);
            }
            System.out.println("lambda = " + lyambda + " M[D] = " + model.mD + " lambda_out = " +
                    model.lyambda_out);
            System.out.println("Part of transferred works from finished works " + usersLeaveUnfinished + " / " + allFinishedWorks + " = " +
                    (double)usersLeaveUnfinished/ allFinishedWorks);
            transfersPerTime = (double) countOfWorkTransfers / T;
            System.out.println("Average transfers number " + transfersPerTime + "\n");
        }
        for (Map.Entry<Integer, Integer> resLoc : averageNumberOfWorksInEachLocation.entrySet()) {
            double average = (double) resLoc.getValue() / (T / quant);
            fileLoc2D.write(resLoc.getKey(), average);
        }
    }
}