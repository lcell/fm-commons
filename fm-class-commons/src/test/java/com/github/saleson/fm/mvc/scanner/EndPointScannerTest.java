package com.github.saleson.fm.mvc.scanner;

import com.github.saleson.fm.enums.ClassType;
import com.github.saleson.fm.scanner.EndPointScanner;
import com.github.saleson.fm.scanner.SpringMVCEndPointScanner;
import org.junit.Test;
import org.springframework.web.bind.annotation.RestController;

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
}
