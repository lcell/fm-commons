package com.github.saleson.fm.commons.sharding.jdbc.spring.aop.aspect;

import com.github.saleson.fm.commons.sharding.jdbc.ShardingJdbcHelper;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author saleson
 * @date 2020-06-12 17:42
 */
@Aspect
public class ShardingJdbcMasterOnlyAspect {
    @Pointcut("@annotation(com.github.saleson.fm.commons.sharding.jdbc.anno.MasterOnly) " +
            "|| @within(com.github.saleson.fm.commons.sharding.jdbc.anno.MasterOnly)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object aroundPointCut(ProceedingJoinPoint pjp) throws Throwable {
        ShardingJdbcHelper.setMasterRouteOnly();
        try {
            return pjp.proceed();
        } finally {
            ShardingJdbcHelper.clearMasterRouteOnly();
        }
    }
}
