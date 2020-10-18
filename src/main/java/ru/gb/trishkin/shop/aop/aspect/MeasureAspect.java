package ru.gb.trishkin.shop.aop.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Aspect
@Component
public class MeasureAspect {

    @Pointcut("@annotation(MeasureMethod)")
    private void annotatedMethod(){
    }

    @Around("annotatedMethod()")
    public Object lodAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        long timeBefore = System.currentTimeMillis();
        Object value = proceedingJoinPoint.proceed();
        long timeAfter = System.currentTimeMillis();
        System.out.println("The method" +
                proceedingJoinPoint.toShortString() +
                "execution time was: " + (timeAfter - timeBefore) + " millis");
        return value;
    }
}
