package com.github.saleson.fm.commons.sharding.jdbc.spring.aop.aspect;

import com.github.saleson.fm.commons.aop.AopHelper;
import com.github.saleson.fm.commons.aop.AopSignatureInfo;
import com.github.saleson.fm.commons.sharding.jdbc.ShardingJdbcHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

/**
 * @author saleson
 * @date 2020-01-10 21:53
 */
@Aspect
public class ShardingJdbcSupportWriteTransactionalMasterOnlyAspect {

    @Pointcut("@annotation(org.springframework.transaction.annotation.Transactional) " +
            "|| @within(org.springframework.transaction.annotation.Transactional)")
    public void pointCut() {
    }


    @Around("pointCut()")
    public Object aroundPointCut(ProceedingJoinPoint pjp) throws Throwable {
        if (isMasterOnly(pjp)) {
            ShardingJdbcHelper.setMasterRouteOnly();
            try {
                return pjp.proceed();
            } finally {
                ShardingJdbcHelper.clearMasterRouteOnly();
            }
        }
        return pjp.proceed();
    }


    private boolean isMasterOnly(ProceedingJoinPoint pjp) {
        AopSignatureInfo aopSignatureInfo = null;
        try {
            aopSignatureInfo = AopHelper.createAopSignatureInfo(pjp);
        } catch (Exception e) {
            return false;
        }
        Transactional transactional = AnnotationUtils.getAnnotation(aopSignatureInfo.getTargetMethod(), Transactional.class);
        if (Objects.isNull(transactional)) {
            transactional = AnnotationUtils.getAnnotation(aopSignatureInfo.getTargetClass(), Transactional.class);
        }
        return !transactional.readOnly();
    }
}
