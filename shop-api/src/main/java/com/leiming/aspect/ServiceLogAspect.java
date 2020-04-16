package com.leiming.aspect;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

/**
 * Create by LovelyLM
 * 2020/4/2 23:32
 * V 1.0
 */

@Aspect
@Slf4j
@Component
public class ServiceLogAspect {

    /**
     *
     * @param joinPoint
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.leiming.service..*.*(..))")
    public Object recordTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("==== 开始执行:{}.{} ====",
                joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());

        //开始时间
        long begin = System.currentTimeMillis();

        //执行目标的Service
        Object proceed = joinPoint.proceed();

        long end = System.currentTimeMillis();

        long takeTime = end - begin;

        if (takeTime >3000) {
            log.error("==== 执行结束，耗时：{} 毫秒 ====",takeTime);
        } else if (takeTime > 2000){
            log.warn("==== 执行结束，耗时：{} 毫秒 ====",takeTime);
        } else {
            log.info("==== 执行结束，耗时：{} 毫秒 ====", takeTime);
        }


        return proceed;
    }
}
