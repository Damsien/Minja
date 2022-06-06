package net.fabricmc.minja.math;

/**
 * Representation of a Cartesian point in a two-dimensional frame of reference
 *
 * @author      Tom Froment
 *
 */
public class CartesianPoint {

    private double x;

    private double y;

    public CartesianPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public PolarPoint CartToPolar() {
        double r     = Math.sqrt(x*x + y*y);
        double theta = Math.atan2(y, x);

        return new PolarPoint(r,theta);
    }


    public double x() {
        return this.x;
    }

    public double y() {
        return this.y;
    }
}