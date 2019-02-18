package com.bluepowermod.helper;

public class MathHelper {

    public static boolean isBetween(double min, double number, double max) {

        return number >= min && number <= max;
    }

    public static int map(int amt, int originalMin, int originalMax, int newMin, int newMax) {

        double amount = (amt - originalMin) / ((double) (originalMax - originalMin));
        return (int) (amount * (newMax - newMin)) + newMin;
    }

    public static long mean(long[] values) {

        long sum = 0l;
        for (long v : values) {
            sum += v;
        }
        return sum / values.length;
    }
}
