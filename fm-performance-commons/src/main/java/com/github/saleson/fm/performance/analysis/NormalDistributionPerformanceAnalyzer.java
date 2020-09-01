package com.github.saleson.fm.performance.analysis;

import java.math.BigDecimal;

/**
 * @author saleson
 * @date 2019-11-13 14:28
 */
public class NormalDistributionPerformanceAnalyzer implements PerformanceAnalyzer {


    private double avg;
    private int counts;
    private double variance;

    @Override
    public void recordDuration(double duration) {
        int newCounts = counts + 1;
        avg = (avg * counts + duration) / (newCounts);
        variance = (variance * counts + Math.pow(duration - avg, 2))/newCounts;
        counts = newCounts;
    }

    @Override
    public double getLineValue(double percent) {
        return 0;
    }

    @Override
    public DurationPerformance getDurationPerformance() {
        DurationPerformance durationPerformance = new DurationPerformance();
        durationPerformance.setLine50Value(avg);
        durationPerformance.setLine90Value(computeNormalDistributionValue(1.289));
        durationPerformance.setLine95Value(computeNormalDistributionValue(1.643));
        durationPerformance.setLine99Value(computeNormalDistributionValue(2.329));
        durationPerformance.setLine999Value(computeNormalDistributionValue(3.01));
        durationPerformance.setLine9999Value(computeNormalDistributionValue(3.07));

        return durationPerformance;
    }


    private double computeNormalDistributionValue(double percentValue){
        return BigDecimal.valueOf(percentValue * Math.sqrt(variance) + avg).doubleValue();
    }



}
