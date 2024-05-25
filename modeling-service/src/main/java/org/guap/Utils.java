package org.guap;

import static org.guap.Main.sizeOfQuant;

class Utils {
    public static double generateExponentialValue(double lambda) {
        return - Math.log(Math.random()) / lambda;
    }

    public static int generateExponentialDistributedNumberOfQuants(double lambda) {
        return (int) Math.ceil(Utils.generateExponentialValue(lambda) / sizeOfQuant);
    }
}