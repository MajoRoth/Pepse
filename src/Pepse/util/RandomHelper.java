package Pepse.util;

import java.util.Random;

public class RandomHelper {
    //TODO: get Perlin Noise implementation from the web for terrain

    /**
     * Flips a weighted coin n times.
     * @param weight probability of true.
     * @param n amount of coin flips
     * @param rnd a random object
     * @return an array of boolean values
     */
    public static boolean[] weightedCoin(float weight,int n, Random rnd){
        boolean[] result = new boolean[n];
        for (int i = 0; i < n; i++) {
            result[i] = weightedCoin(weight,rnd);
        }
        return result;
    }

    /**
     * Flips a weighted coin n times.
     * @param weight probability of true.
     * @param n amount of coin flips
     * @return an array of boolean values
     */
    public static boolean[] weightedCoin(float weight,int n){
        Random rnd = new Random();
        return weightedCoin(weight,n,rnd);
    }

    /**
     * Flips a weighted coin once.
     * @param weight probability of true.
     * @return true or false
     */
    public static boolean weightedCoin(float weight){
        Random rnd = new Random();
        return weightedCoin(weight,rnd);
    }

    /**
     * Flips a weighted coin once.
     * @param weight probability of true.
     * @param rnd a random object
     * @return true or false
     */
    public static boolean weightedCoin(float weight, Random rnd){
        return rnd.nextFloat() < weight;
    }
}
