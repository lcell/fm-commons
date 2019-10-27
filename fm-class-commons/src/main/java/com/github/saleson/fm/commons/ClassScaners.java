package com.github.saleson.fm.commons;

import com.github.saleson.fm.clsscaner.ClassScaner;
import com.github.saleson.fm.clsscaner.SpringClassScaner;

public class ClassScaners {

    public static ClassScaner classScaner(){
        return springClassScaner();
    }


    public static ClassScaner springClassScaner(){
        return new SpringClassScaner();
    }



}
