package net.fabricmc.minja.math;

/**
 * Mathematical operations
 *
 *
 */
public class Operations {

    /**
     * Calculate the mathematical modulo. (no negative number)
     *
     * @param number
     * @param mod
     *
     * @return
     */
    public static double modulo(double number, double mod) {
        double result = number % (mod);
        if (result<0) result += mod;
        return result;
    }

}
