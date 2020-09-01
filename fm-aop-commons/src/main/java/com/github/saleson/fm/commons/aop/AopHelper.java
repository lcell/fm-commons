package com.github.saleson.fm.commons.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.aop.support.AopUtils;

import java.lang.reflect.Method;

/**
 * @author saleson
 * @date 2020-01-10 13:43
 */
public class AopHelper {


    public static AopSignatureInfo createAopSignatureInfo(JoinPoint pjp){
        Signature sig = pjp.getSignature();
        MethodSignature msig = null;
        if (!(sig instanceof MethodSignature)) {
            throw new IllegalArgumentException("该注解只能用于方法");
        }
        msig = (MethodSignature) sig;
        Object target = pjp.getTarget();
        Class targetCls = AopUtils.getTargetClass(target);
        Method targetMethod = null;
        try {
            targetMethod = targetCls.getMethod(msig.getName(), msig.getParameterTypes());
        } catch (NoSuchMethodException e) {
            throw new IllegalArgumentException("找不到方法", e);
        }
//        Method method = ;
        return new AopSignatureInfo(target, msig.getMethod(), targetCls, targetMethod);
    }
}
