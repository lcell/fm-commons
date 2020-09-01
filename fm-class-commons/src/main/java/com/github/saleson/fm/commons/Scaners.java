package com.github.saleson.fm.commons;

import com.github.saleson.fm.enums.ClassType;
import com.github.saleson.fm.scanner.ClassScanner;
import com.github.saleson.fm.scanner.EndPointScanner;
import com.github.saleson.fm.scanner.SpringClassScanner;
import com.github.saleson.fm.scanner.SpringMVCEndPointScanner;

public class Scaners {

    public static ClassScanner classScaner(){
        return springClassScaner();
    }


    public static ClassScanner springClassScaner(){
        return new SpringClassScanner();
    }

    public static EndPointScanner springMvcEndPointScanner(){
        return new SpringMVCEndPointScanner();
    }

    public static EndPointScanner springMvcEndPointScanner(ClassType classType, Class<?>... classes){
        return new SpringMVCEndPointScanner(classType, classes);
    }








}
