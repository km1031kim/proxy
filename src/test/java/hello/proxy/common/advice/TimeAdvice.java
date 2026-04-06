package hello.proxy.common.advice;

import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

import java.lang.reflect.Method;

@Slf4j
public class TimeAdvice implements MethodInterceptor {

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        // advice 내에 pointcut 기능을 추가하면, 역할 분리 및 범용성이 떨어진다. -> pointcut으로 역할 분리
        log.info("TimeProxy 실행");
        long startTime = System.currentTimeMillis();

        // 타겟 클래스를 호출하고 그 결과를 받는다.
        // 프록시 팩토리로 프록시를 생성하는 단계에서 타겟 정보를 넘김
        Object result = invocation.proceed();

        long endTime = System.currentTimeMillis();
        long resultTime = (endTime - startTime);
        log.info("TimeProxy 종료, resultTime = {} ", resultTime);
        return result;
    }
}