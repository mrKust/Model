package org.guap;

class Utils {
    public static double generateExponentialValue(double lambda) {
        return - Math.log(Math.random()) / lambda;
    }
}