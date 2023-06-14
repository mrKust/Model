import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

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
    public static int numberOfLocations = 2;
    /** Данный параметр означает какое условное количество единиц времени производится
     * моделирование*/
    public static float T = 5000;
    /** Данный параметр означает, с какой интенсивностью серевер обрабатывает задачи пользователей */
    public static double serviceRate = 1.0;
    /** Данный параметр задаёт начальную входную интенсивность, с которой начинается моделирование */
    public static final float LAMBDA_IN_START = 0.1F;
    /** Данный параметр задаёт финальную входную интенсивность, при достижении которой моделирование заканчивается */
    public static final float LAMBDA_IN_FINISH = 1.15F;

    /**
     * Данный флаг устанавливает такой параметр системы, как добавления трансферного времени,
     * при переносе задачи с одного сервера на другой
     * Значение true - означает что, при переносе задачи на её перенос потребуется exp(d) времени,
     * в течение которого задача не будет выполняться
     * Значение false - означает, что при переносе задачи на её перенос не потребуется дополнительного
     * времени
     */
    public static final boolean ADD_TRANSFER_TIME = true;

    /** Данный параметр хранит значение необходимое для отображения итоговых данных по каждой области.
     * true - выводятся данные в формате номер области - номер задачи - объём выполненной работы - общий объём задачи
     * false - данные о задачах в области не выводятся*/
    public static final boolean SHOW_LOCATION_SUMMARY = false;

    /**
     * Данный флаг устанавливает такой параметр системы, как вывод в консоль данных о вероятности
     * n-ого переноса задачи
     * Значение true - означает что, в консоль выведется кол-во переходов задачи и вероятнсоть такого
     * события
     * Значение false - означает, что в консоль не выведется кол-во переходов задачи и вероятнсоть
     * такого события
     */
    public static final boolean SHOW_WORKS_TRANSFER_PROBABILITY = false;
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
    public static void main(String[] args) throws InterruptedException {

        BufferedWriter outputFile = null;
        try {
            outputFile = new BufferedWriter(new FileWriter("model.txt"));
        } catch (IOException e) {
            System.err.println("Problems with output file model.txt");
            throw new RuntimeException(e);
        }

        long start = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        CompletionService<OutputData> service
                = new ExecutorCompletionService<>(executor);

        List<Callable<OutputData>> callablesList = new ArrayList<>();
        for (float lambda = LAMBDA_IN_START; lambda <= LAMBDA_IN_FINISH; lambda += 0.1) {
            callablesList.add(new Model(lambda, a, q, d, quant, numberOfLocations, T, serviceRate));
        }

        for (Callable<OutputData> callable : callablesList) {
            service.submit(callable);
        }

        try {
            Future<OutputData> future = null;
            for (float lambda = LAMBDA_IN_START; lambda <= LAMBDA_IN_FINISH; lambda += 0.1) {
                future = service.take();
                writeInOutputFile(future.get(), outputFile);
            }
        } catch (ExecutionException e) {
            System.err.println("Something went wrong. Check error message");
            e.printStackTrace();
            executor.shutdown();
        }

        long end = System.currentTimeMillis();

        executor.shutdown();

        System.out.println("Final time " + (end - start));
    }

    public synchronized static void writeInOutputFile(OutputData outputData, BufferedWriter outputFile) {
        try {
            StringBuilder outputText = new StringBuilder();
            outputText.append(outputData.toString());
            outputFile.write(outputText.toString());
            outputFile.flush();
        } catch (IOException e) {
            System.err.println("Can't write in file model.txt");
            throw new RuntimeException(e);
        }
    }
}