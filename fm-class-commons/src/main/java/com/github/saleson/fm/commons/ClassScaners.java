package com.github.saleson.fm.commons;

import com.github.saleson.fm.scanner.ClassScanner;
import com.github.saleson.fm.scanner.SpringClassScanner;

public class ClassScaners {

    public static ClassScanner classScaner(){
        return springClassScaner();
    }


    public static ClassScanner springClassScaner(){
        return new SpringClassScanner();
    }



}
