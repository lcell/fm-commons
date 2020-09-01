package com.github.saleson.fm.performance.analysis;

/**
 * @author saleson
 * @date 2019-11-13 11:55
 */
public interface PerformanceAnalyzer {


    void recordDuration(double duration);



    double getLineValue(double percent);


    DurationPerformance getDurationPerformance();

}
