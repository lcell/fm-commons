package com.github.saleson.fm.mvc.scanner;

import com.github.saleson.fm.enums.ClassType;
import com.github.saleson.fm.scanner.EndPointScanner;
import com.github.saleson.fm.scanner.SpringMVCEndPointScanner;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;

/**
 * @author saleson
 * @date 2020-09-01 16:43
 */
public class EndPointScannerTest {

    @Test
    public void test(){
        EndPointScanner endPointScanner = new SpringMVCEndPointScanner(ClassType.ANNOTATION, RestController.class);
        Set<EndPointScanner.EndPoint> eps = endPointScanner.scan("com.github.saleson.fm.mvc.scanner");
        eps.forEach(System.out::println);
    }

    @Test
    public void test2() throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = sdf.parse("2019-05-23");
        long day = 1000 * 60 * 60 * 24;
        long mss = System.currentTimeMillis() - date.getTime();
        System.out.println(mss/day);

    }
}
