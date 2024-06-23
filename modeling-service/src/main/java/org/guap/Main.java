package org.guap;

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
    public static double sizeOfQuant = 0.01;
    /** Данный параметр означает количество областей с которыми производиться моделирование*/
    public static int numberOfLocations = 1;
    /** Данный параметр означает какое условное количество единиц времени производится
     * моделирование*/
    public static float T = 1_000_00;
    /** Данный параметр означает, с какой интенсивностью серевер обрабатывает задачи пользователей */
    public static double serviceRate = 1.0;
    /** Данный параметр задаёт начальную входную интенсивность, с которой начинается моделирование */
    public static final double LAMBDA_IN_START = 0.1;
    /** Данный параметр задаёт финальную входную интенсивность, при достижении которой моделирование заканчивается */
    public static final double LAMBDA_IN_FINISH = 0.95;
    public static final double LAMBDA_FOR_TASK_SIZE = 1.0;
    public static final SystemType MODELING_SYSTEM_TYPE = SystemType.KR;
    public static final DistributionType TASK_SIZE_DISTRIBUTION_TYPE = DistributionType.EXPONENTIAL;

    /**
     * Данный флаг устанавливает такой параметр системы, как добавления трансферного времени,
     * при переносе задачи с одного сервера на другой
     * Значение true - означает что, при переносе задачи на её перенос потребуется exp(d) времени,
     * в течение которого задача не будет выполняться
     * Значение false - означает, что при переносе задачи на её перенос не потребуется дополнительного
     * времени
     */
    public static final boolean ADD_TRANSFER_TIME = false;

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
        BufferedWriter outputFile;
        try {
            outputFile = new BufferedWriter(new FileWriter("model.txt"));
        } catch (IOException e) {
            System.err.println("Problems with output file model.txt " + e.getMessage());
            throw new RuntimeException(e);
        }

        long start = System.currentTimeMillis();

        ExecutorService executor = Executors.newFixedThreadPool(4);

        CompletionService<OutputData> service
                = new ExecutorCompletionService<>(executor);

        List<Callable<OutputData>> callablesList = new ArrayList<>();
        for (double lambda = LAMBDA_IN_START; lambda <= LAMBDA_IN_FINISH; lambda += 0.1) {
            callablesList.add(new Model(lambda));
        }

        for (Callable<OutputData> currentCallable: callablesList) {
            service.submit(currentCallable);
        }

        Map<Double, OutputData> results = new TreeMap<>();
        try {
            OutputData outputData;
            for (double lambda = LAMBDA_IN_START; lambda <= LAMBDA_IN_FINISH; lambda += 0.1) {
                outputData = service.take().get();
                results.put(outputData.getLambdaIn(), outputData);
            }
            for (Map.Entry<Double, OutputData> currentResult: results.entrySet()) {
                writeInOutputFile(currentResult.getValue(), outputFile);
            }
        } catch (ExecutionException e) {
            System.err.println("Something went wrong. Check error message " + e.getMessage());
        } finally {
            executor.shutdown();
        }

        long end = System.currentTimeMillis();

        System.out.println("Final time " + (end - start));
    }

    public synchronized static void writeInOutputFile(OutputData outputData, BufferedWriter outputFile) {
        try {
            outputFile.write(outputData.toString());
            outputFile.flush();
        } catch (IOException e) {
            System.err.println("Can't write in file model.txt");
            throw new RuntimeException(e);
        }
    }
}