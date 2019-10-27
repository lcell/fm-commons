package com.github.saleson.fm.clsscaner;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ClassScaner {

    
    Set<Class<?>> scan(String basePackages, Class<? extends Annotation>... annotations);

    Set<Class<?>> scan(String[] basePackages, Class<? extends Annotation>... annotations);

}
