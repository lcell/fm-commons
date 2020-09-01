package com.github.saleson.fm.performance.analysis;

import lombok.ToString;

/**
 * @author saleson
 * @date 2019-11-13 14:23
 */
@ToString
public class DurationPerformance {


    public final static double PERCENT_50 = 50.0;

    public final static double PERCENT_90 = 90.0;

    public final static double PERCENT_95 = 95.0;

    public final static double PERCENT_99 = 99.0;

    public final static double PERCENT_999 = 99.9;

    public final static double PERCENT_9999 = 99.99;


    private double line50Value;
    private double line90Value;
    private double line95Value;
    private double line99Value;
    private double line999Value;
    private double line9999Value;


    public double getLine50Value() {
        return line50Value;
    }

    public void setLine50Value(double line50Value) {
        this.line50Value = line50Value;
    }

    public double getLine90Value() {
        return line90Value;
    }

    public void setLine90Value(double line90Value) {
        this.line90Value = line90Value;
    }

    public double getLine95Value() {
        return line95Value;
    }

    public void setLine95Value(double line95Value) {
        this.line95Value = line95Value;
    }

    public double getLine99Value() {
        return line99Value;
    }

    public void setLine99Value(double line99Value) {
        this.line99Value = line99Value;
    }

    public double getLine999Value() {
        return line999Value;
    }

    public void setLine999Value(double line999Value) {
        this.line999Value = line999Value;
    }

    public double getLine9999Value() {
        return line9999Value;
    }

    public void setLine9999Value(double line9999Value) {
        this.line9999Value = line9999Value;
    }
}
