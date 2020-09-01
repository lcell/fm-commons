package com.github.saleson.fm.scanner;

import java.lang.annotation.Annotation;
import java.util.Set;

public interface ClassScanner {


    Set<Class<?>> scan(String... basePackages);

    Set<Class<?>> scanAssignableType(String basePackages, Class<?>... targetType);

    Set<Class<?>> scanAssignableType(String[] basePackages, Class<?>... targetType);

    Set<Class<?>> scanByAnnotation(String basePackages, Class<? extends Annotation>... annotations);

    Set<Class<?>> scanByAnnotation(String[] basePackages, Class<? extends Annotation>... annotations);

}
