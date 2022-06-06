package net.fabricmc.minja.math;

public class PolarPoint {

    private double r;
    private double theta;


    public PolarPoint(double r, double theta) {
        this.r = r;
        this.theta = theta;
    }

    public CartesianPoint PolarToCart() {
        double x = r * Math.cos(theta);
        double y = r * Math.sin(theta);
        return new CartesianPoint(x,y);
    }

    public double r() {
        return this.r;
    }

    public double theta() {
        return this.theta;
    }

}