package org.guap;

/**
 * Данный файл является вспомогательным и хранит в себе пару значений, необходимых для заявки
 */
public class Pair {

    double windowIn;
    double workSize;

    public Pair(double tmp1, double tmp2) {
        this.windowIn = tmp1;
        this.workSize = tmp2;
    }
}
