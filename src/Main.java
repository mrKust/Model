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
    public static double a = 0.0;
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

    public static int usersLeaveUnfinished = 0;

    public static int allFinishedWorks = 0;

    public static int countOfWorkTransfers = 0;

    public static final boolean SHOW_LOCATION_SUMMARY = false;

    public static final boolean TRANSFER_MODE = true;

    /**
     * В данном методе производиться заупкск моделирования с заданным значениями параметров, а так
     * же изменение параметра входной интенсивности. Так же данный метод осуществляет запись полученных
     * данных в выходной файл
     */
    public static void main(String[] args) {

        FileWork fileUbuntu = new FileWork("D:\\Pereezd\\Labs\\Научка\\Model\\model.txt", false);

        Model modelLowIntensity = new Model(a, q, d, quant, numberOfLocations, T, serviceRate);
        //modelLowIntensity.getModelingForLowIntensity();
        double transfersPerTime = (double) countOfWorkTransfers / T;
        fileUbuntu.write(0, modelLowIntensity.lambda_out, modelLowIntensity.mediumSizeOfWork,
                transfersPerTime, modelLowIntensity.mD, 0.0);
        System.out.println("lambda = low" + " M[D] = " + modelLowIntensity.mD + " lambda_out = " +
                modelLowIntensity.lambda_out);
        System.out.println(usersLeaveUnfinished + " / " + allFinishedWorks + " = " +
                (double)usersLeaveUnfinished/ allFinishedWorks);
        System.out.println("Average transfers number " + transfersPerTime + "\n");

        for (float lyambda = (float) 0.1; lyambda < 1.5; lyambda += 0.1) {
            usersLeaveUnfinished = 0;
            allFinishedWorks = 0;
            countOfWorkTransfers = 0;
            System.out.println("lyambda = " + lyambda);

            Model model = new Model(lyambda, a, q, d, quant, numberOfLocations, T, serviceRate);
            model.getModeling();
            //double dPoLittle = model.averageNumberOfWorks / lyambda;
            if (lyambda < 1.0) {
                fileUbuntu.write(lyambda, model.lambda_out, model.mediumSizeOfWork, transfersPerTime, model.mD,
                        model.mDTheoretical);
            } else {
                fileUbuntu.write(lyambda, model.lambda_out, model.mediumSizeOfWork, transfersPerTime);
            }
            System.out.println("lambda = " + lyambda + " M[D] = " + model.mD + " lambda_out = " +
                    model.lambda_out);
            System.out.println("Part of transferred works from finished works " + usersLeaveUnfinished + " / " + allFinishedWorks + " = " +
                    (double)usersLeaveUnfinished/ allFinishedWorks);
            transfersPerTime = (double) countOfWorkTransfers / T;
            System.out.println("Average transfers number " + transfersPerTime + "\n");
        }

    }
}