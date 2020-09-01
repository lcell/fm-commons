package com.github.saleson.fm.performance.analysis;

import java.util.*;

/**
 * @author saleson
 * @date 2019-11-13 14:32
 */
public class CollectOperationPerformanceAnalyzer implements PerformanceAnalyzer {

    private static Comparator<Integer> DESC = new Desc();

    private Map<Integer, DurationCountor> durationCountors = new HashMap<>();

    @Override
    public void recordDuration(double duration) {
//        int computeDuration = DurationComputer.computeDuration((int) duration);
        int computeDuration = (int) duration;
        findOrCreateDurationCountor(computeDuration).incCount();
    }

    @Override
    public double getLineValue(double percent) {
        return computeLineValue(durationCountors,
                new double[] { percent }).values().stream().findFirst().orElse(0);
    }

    @Override
    public DurationPerformance getDurationPerformance() {
        Map<Double, Integer> lineValues = computeLineValue(durationCountors,
                new double[] { DurationPerformance.PERCENT_50, DurationPerformance.PERCENT_90,
                        DurationPerformance.PERCENT_95, DurationPerformance.PERCENT_99,
                        DurationPerformance.PERCENT_999, DurationPerformance.PERCENT_9999 });

        DurationPerformance durationPerformance = new DurationPerformance();
        durationPerformance.setLine50Value(lineValues.get(DurationPerformance.PERCENT_50));
        durationPerformance.setLine90Value(lineValues.get(DurationPerformance.PERCENT_90));
        durationPerformance.setLine95Value(lineValues.get(DurationPerformance.PERCENT_95));
        durationPerformance.setLine99Value(lineValues.get(DurationPerformance.PERCENT_99));
        durationPerformance.setLine999Value(lineValues.get(DurationPerformance.PERCENT_999));
        durationPerformance.setLine9999Value(lineValues.get(DurationPerformance.PERCENT_9999));
        return durationPerformance;
    }


    public Map<Double, Integer> computeLineValue(Map<Integer, DurationCountor> durationCountors, double[] percents){
        final Map<Integer, DurationCountor> sourtMap = sortMap(durationCountors);
        return _computeLineValue(sourtMap, percents);
    }


    private Map<Double, Integer> _computeLineValue(Map<Integer, DurationCountor> sorted, double[] percents) {
        int totalCount = 0;

        for (DurationCountor duration : sorted.values()) {
            totalCount += duration.getCount();
        }

        Map<Double, Integer> lineValue = new LinkedHashMap<Double, Integer>();
        Map<Double, Integer> remainings = new LinkedHashMap<>();

        for (double percent : percents) {
            int remaining = (int) (totalCount * (100 - percent) / 100);

            remainings.put(percent, remaining);
            lineValue.put(percent, 0);
        }

        for (Map.Entry<Integer, DurationCountor> entry : sorted.entrySet()) {
            int count = entry.getValue().getCount();
            int result = 1;

            for (double key : percents) {
                if (lineValue.get(key) == 0) {
                    int remaining = remainings.get(key);
                    remaining -= count;

                    if (remaining <= 0) {
                        lineValue.put(key, entry.getKey());
                    }
                    remainings.put(key, remaining);
                }
                result &= lineValue.get(key);
            }

            if (result > 0) {
                break;
            }
        }

        return lineValue;
    }


    private Map<Integer, DurationCountor> sortMap(Map<Integer, DurationCountor> durations) {
        Map<Integer, DurationCountor> sorted = new TreeMap<>(DESC);
        sorted.putAll(durations);
        return sorted;
    }



    public DurationCountor findOrCreateDurationCountor(int value) {
        DurationCountor durationCountor = durationCountors.get(value);

        if (durationCountor == null) {
            synchronized (durationCountors) {
                durationCountor = durationCountors.get(value);

                if (durationCountor == null) {
                    durationCountor = new DurationCountor(value);
                    durationCountors.put(value, durationCountor);
                }
            }
        }

        return durationCountor;
    }


    private static class Desc implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o2 - o1;
        }
    }
}
