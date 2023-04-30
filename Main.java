import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

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
    public static double a = 0.9;
    /** Данный параметр означает размер кванта, то есть размер шага с которым мы двигаемся по
     * временной шкале каждой локации*/
    public static double quant = 0.01;
    /** Данный параметр означает количество областей с которыми производиться моделирование*/
    public static int numberOfLocations = 2;
    /** Данный параметр означает какое условное количество единиц времени производится
     * моделирование*/
    public static float T = 100_000;
    /** Данный параметр означает, с какой интенсивностью серевер обрабатывает задачи пользователей */
    public static double serviceRate = 1.0;
    /** Данный параметр задаёт начальную входную интенсивность, с которой начинается моделирование */
    public static final float LAMBDA_IN_START = 0.1F;
    /** Данный параметр задаёт финальную входную интенсивность, при достижении которой моделирование заканчивается */
    public static final float LAMBDA_IN_FINISH = 0.15F;
    /** Данный параметр определяет входную интенсивность, для которой будет посчитано среднее значение кол-ва задач
     * в один квант времени для каждой области */
    public static float LAMBDA_TRACK_AVERAGE_NUMBER_OF_WORKS = 0.5F;

    /** Данное поле отображает текущую входную интенсивность для которой сейчас происходит моделирование */
    public static float currentLambda = 0.0F;
    /** Данное поле хранит количество пользователей, которые покидают область до того, как завершился рассчёт их
     * задачи */
    public static int finishedWorksWhichWereTransferedMoreThanOneTime = 0;
    /** Данное поле хранит количество пользователей, которые покидают систему после того, как было завершено
     * вычисление их задачи */
    public static int allFinishedWorks = 0;

    public static int allNumberOfTransfersOfEachFinishedWork;
    /** Данный параметр хранит значение необходимое для отображения итоговых данных по каждой области.
     * true - выводятся данные в формате номер области - номер задачи - объём выполненной работы - общий объём задачи
     * false - данные о задачах в области не выводятся*/
    public static final boolean SHOW_LOCATION_SUMMARY = false;
    /** Данная коллекция хранит среднее кол-во задач в каждый квант времени для каждой области */
    public static Map<Integer, Long> averageNumberOfWorksInEachLocation = new HashMap<>();
    /**
     * Данная коллекция хранит в себе значения пары
     * Кол-во раз (n), котораое перемещалась задача - Кол-во завершённых задач, перемещавшихся n раз
     */
    public static Map<Integer, Long> numberOfTransfersOfCompletedWorks = new HashMap<>();
    /**
     * Данная коллекция хранит в себе значения пары
     * Кол-во раз (n), котораое перемешялся пользователь - Кол-во пользователей,
     * обладающих завершёнными задачами, и перемещавшихся (сам пользователь) n раз
     */
    public static Map<Integer, Long> numberOfTransfersOfUsersWithCompletedWorks = new HashMap<>();

    /**
     * Данное поле хранит в себе суммарное количество трансферов пользователей, чьи задачи были
     * выполненны
     */
    public static Long allNumberOfUserWithCompletedTasksTransfers = 0L;
    /**
     * Данный флаг устанавливает такой параметр системы, как добавления трансферного времени,
     * при переносе задачи с одного сервера на другой
     * Значение true - означает что, при переносе задачи на её перенос потребуется exp(d) времени,
     * в течение которого задача не будет выполняться
     * Значение false - означает, что при переносе задачи на её перенос не потребуется дополнительного
     * времени
     */
    public static final boolean ADD_TRANSFER_TIME = false;
    /**
     * Данный флаг устанавливает такой параметр системы, как вывод в консоль данных о вероятности
     * n-ого переноса задачи
     * Значение true - означает что, в консоль выведется кол-во переходов задачи и вероятнсоть такого
     * события
     * Значение false - означает, что в консоль не выведется кол-во переходов задачи и вероятнсоть
     * такого события
     */
    public static final boolean SHOW_WORKS_TRANSFER_PROBABILITY = true;
    /**
     * Данный флаг устанавливает такой параметр системы, как вывод в консоль данных о вероятности
     * n-ого перехода пользователя
     * Значение true - означает что, в консоль выведется кол-во переходов пользователя и вероятнсоть
     * такого события
     * Значение false - означает, что в консоль не выведется кол-во переходов пользователя и вероятнсоть
     * такого события
     */
    public static final boolean SHOW_USERS_TRANSFER_PROBABILITY = false;
    /**
     * В данном методе производиться заупкск моделирования с заданным значениями параметров, а так
     * же изменение параметра входной интенсивности. Так же данный метод осуществляет запись полученных
     * данных в выходной файл
     */
    public static void main(String[] args) {

        BufferedWriter outputFile = null;
        BufferedWriter fileLoc = null;
        try {
            outputFile = new BufferedWriter(new FileWriter("model.txt"));
            fileLoc = new BufferedWriter(new FileWriter("LocationsAverangeNumber2D.txt"));
        } catch (IOException e) {
            System.err.println("Problems with output files");
            throw new RuntimeException(e);
        }

        Model modelLowIntensity = new Model(a, q, d, quant, numberOfLocations, T, serviceRate);
        //modelLowIntensity.getModelingForLowIntensity();
        printOutSummary(0, modelLowIntensity, outputFile, fileLoc);

        for (float lyambda = LAMBDA_IN_START; lyambda <= LAMBDA_IN_FINISH; lyambda += 0.1) {

            for (int i = 0; i < numberOfLocations; i++) {
                averageNumberOfWorksInEachLocation.put(i, 0L);
            }

            Main.LAMBDA_TRACK_AVERAGE_NUMBER_OF_WORKS = lyambda;
            currentLambda = lyambda;
            finishedWorksWhichWereTransferedMoreThanOneTime = 0;
            allFinishedWorks = 0;
            allNumberOfTransfersOfEachFinishedWork = 0;
            allNumberOfUserWithCompletedTasksTransfers = 0L;

            Model model = new Model(lyambda, a, q, d, quant, numberOfLocations, T, serviceRate);
            model.getModeling();
            printOutSummary(lyambda, model, outputFile, fileLoc);

            averageNumberOfWorksInEachLocation.clear();
            numberOfTransfersOfCompletedWorks.clear();
        }
    }

    public static void printOutSummary(float lambda, Model model, BufferedWriter outputFile, BufferedWriter fileLoc) {
        System.out.println("lambda = " + lambda);
        System.out.println("lambda = " + lambda + " M[D] = " + model.mD + " lambda_out = " + model.lyambda_out);
        double averageNumberOfWorkTransfers = ((double)allNumberOfTransfersOfEachFinishedWork / allFinishedWorks);
        System.out.println("Average number of transfers for each work " + averageNumberOfWorkTransfers);

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        double averageNumberOfUserTransfers = ((double)allNumberOfUserWithCompletedTasksTransfers / allFinishedWorks);
        //double averageNumberOfUserTransfers = ((double)allNumberOfUserWithCompletedTasksTransfers / (T / model.sizeOfQuant));

        //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        System.out.println("Average transfers number of users with completed tasks " + averageNumberOfUserTransfers);
        System.out.println(allNumberOfUserWithCompletedTasksTransfers);
        System.out.println(allFinishedWorks);

        System.out.println("Part of transferred works from finished works " + finishedWorksWhichWereTransferedMoreThanOneTime + " / " + allFinishedWorks + " = " +
                (double) finishedWorksWhichWereTransferedMoreThanOneTime / allFinishedWorks);
        double transfersPerTime = (double) allNumberOfTransfersOfEachFinishedWork / T;
        System.out.println("Average transfers number in one window " + transfersPerTime);

        double dPoLittle = 0;
        double avarageNumberOfTasksInOneQuant = 0;

        try {
            for (Map.Entry<Integer, Long> resLoc : averageNumberOfWorksInEachLocation.entrySet()) {
                double tmp = (double) resLoc.getValue() / (T / quant);
                System.out.println("Location " + resLoc.getKey() + " average number of works = " + tmp);
                fileLoc.write(Integer.toString(resLoc.getKey()));
                fileLoc.write(" ");
                fileLoc.write(Double.toString(tmp));
                fileLoc.write("\n");
                avarageNumberOfTasksInOneQuant += tmp;
            }
            fileLoc.flush();
        } catch (IOException e) {
            System.err.println("Can't write in file LocationsAverangeNumber2D.txt");
            throw new RuntimeException(e);
        }


        avarageNumberOfTasksInOneQuant /= Main.numberOfLocations;
        dPoLittle = avarageNumberOfTasksInOneQuant / lambda;
        System.out.println("M[D] by Little = " + dPoLittle);
        System.out.println("Number of tasks with transfer numbers");
        Map<Integer, Double> outputData = new LinkedHashMap<>();
        List<Integer> numberTransfersOfWorks = numberOfTransfersOfCompletedWorks.keySet().stream().sorted().collect(Collectors.toList());
        for (Integer currentKey : numberTransfersOfWorks) {
            long currentValue = numberOfTransfersOfCompletedWorks.get(currentKey);
            double probabilityToNTransfers = (double) currentValue / allFinishedWorks;
            outputData.put(currentKey, probabilityToNTransfers);
            if (SHOW_WORKS_TRANSFER_PROBABILITY) {
                System.out.println("With transfer num equals " + currentKey + " was " + currentValue + " works. Probability to make n transfers is "
                        + probabilityToNTransfers);
            }
        }
        System.out.println();

        List<Integer> numberTransfersOfUsers = numberOfTransfersOfUsersWithCompletedWorks.keySet().stream().sorted().collect(Collectors.toList());
        System.out.println("Number of users with transfer numbers");
        for (Integer currentKey : numberTransfersOfUsers) {
            long currentValue = numberOfTransfersOfUsersWithCompletedWorks.get(currentKey);
            double probabilityToNTransfers = (double) currentValue / allFinishedWorks;
            if (SHOW_USERS_TRANSFER_PROBABILITY) {
                System.out.println("With transfer num equals " + currentKey + " was " + currentValue + " users. Probability to make n transfers is "
                        + probabilityToNTransfers);
            }
        }
        System.out.println();

        try {
            StringBuilder outputText = new StringBuilder();

            outputText.append("Start lambda ").append(lambda).append("\n");
            for (Map.Entry<Integer, Double> probabilityToTransfer : outputData.entrySet()) {
                outputText.append(probabilityToTransfer.getKey()).append(" ").append(probabilityToTransfer.getValue()).append("\n");
            }
            outputText.append(averageNumberOfWorkTransfers).append("\n");
            outputText.append(averageNumberOfUserTransfers).append("\n");
            outputText.append(model.mD).append("\n");
            outputText.append(avarageNumberOfTasksInOneQuant).append("\n");
            outputText.append("End lambda ").append(lambda).append("\n");

        /*if (lambda < 1.0) {
            outputText.append(lambda).append(" ").append(model.lyambda_out).append(" ").append(model.mediumSizeOfWork)
                    .append(" ").append(transfersPerTime).append(" ").append(model.mD).append(" ")
                    .append(dPoLittle).append("\n");
            *//*outputText.append(lambda).append(" ").append(model.lyambda_out).append(" ").append(model.mediumSizeOfWork)
                    .append(" ").append(transfersPerTime).append(" ").append(model.mD).append(" ")
                    .append(model.mDTheoretical).append("\n");*//*
        } else {
            outputText.append(lambda).append(" ").append(model.lyambda_out).append(" ").append(model.mediumSizeOfWork)
                    .append(" ").append(transfersPerTime).append(" ").append("\n");
        }*/

            outputFile.write(outputText.toString());
            outputFile.flush();
        } catch (IOException e) {
            System.err.println("Can't write in file model.txt");
            throw new RuntimeException(e);
        }

    }
}