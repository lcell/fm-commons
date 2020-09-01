package com.github.saleson.fm.commons.aop;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.lang.reflect.Method;

/**
 * @author saleson
 * @date 2020-01-10 13:38
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public class AopSignatureInfo {
    private final Object object;
    private final Method method;
    private final Class targetClass;
    private final Method targetMethod;


}
