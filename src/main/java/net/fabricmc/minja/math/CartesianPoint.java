package net.fabricmc.minja.math;

/**
 * Representation of a Cartesian point in a two-dimensional frame of reference
 *
 *
 */
public class CartesianPoint {

    private double x;

    private double y;

    public CartesianPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Convert a cartesian point to a polar point.
     *
     * @return Representation of the point in the polar plan.
     */
    public PolarPoint CartToPolar() {
        double r     = Math.sqrt(x*x + y*y);
        double theta = Math.atan2(y, x);

        return new PolarPoint(r,theta);
    }

    /**
     * Get X coordinate
     *
     * @return (X, _)
     */
    public double x() {
        return this.x;
    }

    /**
     * Get Y coordinate
     *
     * @return (_, Y)
     */
    public double y() {
        return this.y;
    }
}