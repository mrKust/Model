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
    public static int numberOfLocations = 5;
    /** Данный параметр означает какое условное количество единиц времени производится
     * моделирование*/
    public static float T = 1000;
    /** Данный параметр означает, с какой интенсивностью серевер обрабатывает задачи пользователей */
    public static double serviceRate = 1.0;

    /**
     * В данном методе производиться заупкск моделирования с заданным значениями параметров, а так
     * же изменение параметра входной интенсивности
     */
    public static void main(String[] args) {

        FileWork fileUbuntu = new FileWork("/media/D/Pereezd/Labs/Димплом/Model/model.txt", false);
        //FileWork fileMac = new FileWork("/Users/andreyvasilyev/Desktop/Model/model.txt", false);

        for (float lyambda = (float) 0.1; lyambda < 2.0; lyambda += 0.1) {
            System.out.println("lyambda = " + lyambda);
            Model model = new Model(lyambda, a, q, d, quant, numberOfLocations, T, serviceRate);
            model.getModeling();
            if (lyambda < 1.0) {
                fileUbuntu.write(lyambda, model.lyambda_out, model.mediumSizeOfWork, model.mD,
                        model.mDTheoretical);
                //fileMac.write(lyambda, model.lyambda_out, model.mediumSizeOfWork, model.mD,
                //                        model.mDTheoretical);
            } else {
                fileUbuntu.write(lyambda, model.lyambda_out, model.mediumSizeOfWork);
                //fileMac.write(lyambda, model.lyambda_out, model.mediumSizeOfWork);
            }
            System.out.println("lyambda = " + lyambda + " M[D] = " + model.mD + " lyambda_out = " +
                    model.lyambda_out);
        }

    }
}
