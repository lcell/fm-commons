package con.github.saleson.fm.performance.analysis;

import com.github.saleson.fm.performance.analysis.CollectOperationPerformanceAnalyzer;
import com.github.saleson.fm.performance.analysis.NormalDistributionPerformanceAnalyzer;
import org.junit.Test;

import java.util.Random;

/**
 * @author saleson
 * @date 2019-11-13 15:29
 */
public class PerformanceAnalyzerTest {

    @Test
    public void test(){
        Random random = new Random();
        NormalDistributionPerformanceAnalyzer normalDistributionPerformanceAnalyzer = new NormalDistributionPerformanceAnalyzer();
        CollectOperationPerformanceAnalyzer collectOperationPerformanceAnalyzer = new CollectOperationPerformanceAnalyzer();
        for(int i=0;i<10000000;i++){
            double d = 5000+random.nextDouble() * 500;
            normalDistributionPerformanceAnalyzer.recordDuration(d);
            collectOperationPerformanceAnalyzer.recordDuration(d);
        }

        System.out.println(String.format("normalDistributionPerformanceAnalyzer -> \n%s", normalDistributionPerformanceAnalyzer.getDurationPerformance()));

        System.out.println(String.format("\ncollectOperationPerformanceAnalyzer -> \n%s", collectOperationPerformanceAnalyzer.getDurationPerformance()));
    }


    @Test
    public void test1(){

        System.out.println(compute(0.94,16.0, 64.3));
        System.out.println(compute(2.77,16.0, 64.3));
    }

    private double compute(double percentValue, double bzc, double avg){
        return percentValue * bzc + avg;
    }
}
