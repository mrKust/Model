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
    public static int numberOfLocations = 48;
    /** Данный параметр означает какое условное количество единиц времени производится
     * моделирование*/
    public static float T = 300;
    /** Данный параметр означает, с какой интенсивностью серевер обрабатывает задачи пользователей */
    public static double serviceRate = 1.0;

    /**
     * В данном методе производиться заупкск моделирования с заданным значениями параметров, а так
     * же изменение параметра входной интенсивности. Так же данный метод осуществляет запись полученных
     * данных в выходной файл
     */
    public static void main(String[] args) {

        FileWork fileUbuntu = new FileWork("D:\\Pereezd\\Labs\\Научка\\Model\\model.txt", false);
        //FileWork fileMac = new FileWork("/Users/andreyvasilyev/Desktop/Model/model.txt", false);

        Model modelLowIntensity = new Model(a, q, d, quant, numberOfLocations, T, serviceRate);
        modelLowIntensity.getModelingForLowIntensity();
        fileUbuntu.write(0, modelLowIntensity.lyambda_out, modelLowIntensity.mediumSizeOfWork, modelLowIntensity.mD,
                    modelLowIntensity.mDTheoretical);
        System.out.println("lambda = low" + " M[D] = " + modelLowIntensity.mD + " lambda_out = " +
                modelLowIntensity.lyambda_out + "\n");

        //for (float lyambda = (float) 0.2; lyambda < 0.3; lyambda += 0.1) {
        for (float lyambda = (float) 0.1; lyambda < 1.5; lyambda += 0.1) {
            System.out.println("lyambda = " + lyambda);

            Model model = new Model(lyambda, a, q, d, quant, numberOfLocations, T, serviceRate);
            model.getModeling();
            //double dPoLittle = model.averageNumberOfWorks / lyambda;
            if (lyambda < 1.0) {
                fileUbuntu.write(lyambda, model.lyambda_out, model.mediumSizeOfWork, model.mD,
                        model.mDTheoretical);
                //fileMac.write(lyambda, model.lyambda_out, model.mediumSizeOfWork, model.mD,
                //                        model.mDTheoretical);
            } else {
                fileUbuntu.write(lyambda, model.lyambda_out, model.mediumSizeOfWork);
                //fileMac.write(lyambda, model.lyambda_out, model.mediumSizeOfWork);
            }
            System.out.println("lambda = " + lyambda + " M[D] = " + model.mD + " lambda_out = " +
                    model.lyambda_out + "\n");
        }

    }
}