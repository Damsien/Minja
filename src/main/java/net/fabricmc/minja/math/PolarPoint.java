package net.fabricmc.minja.math;

/**
 * Representation of a Polar point in a two-dimensional frame of reference
 *
 *
 */
public class PolarPoint {

    private double r;
    private double theta;



    public PolarPoint(double r, double theta) {
        this.r = r;
        this.theta = theta;
    }

    /**
     * Convert a polar point to a cartesian point.
     *
     * @return Representation of the point in the cartesian plan.
     */
    public CartesianPoint PolarToCart() {
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);
        return new CartesianPoint(x,y);
    }

    /**
     * Get distance from the point to the origin
     *
     * @return distance
     */
    public double r() {
        return this.r;
    }

    /**
     * Get the angle between the x-axis and the point
     *
     * @return angle
     */
    public double theta() {
        return this.theta;
    }

}