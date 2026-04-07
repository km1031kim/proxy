package hello.proxy.config.v5_autoproxy;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v3_proxyfactory.advice.LogTraceAdvice;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AutoProxyConfig {

    /**
     * 스프링 aop로 인해 AutoProxyCreator(자동 빈 후처리기) 가 빈으로 등록되고
     *  덕분에 Advisor만 빈으로 등록하면, pointcut, advise로 자동으로 프록시 생성.
     * 빈 등록은 퍼블릭
     */
//    @Bean
    public Advisor advisor1(LogTrace logTrace) {
        /**
         * 포인트컷은 두가지에 사용됨.
         * 프록시 생성단계, 이후 해당 메서드 호출 단계
         * 조건에 맞는 것이 하나라도 있으면 프록시 생성.
         * 프록시에서 메서드 호출 시 어드바이스 호출 여부 판단.
         * 어드바이스가 사용될 가능성이 있는 곳에만 프록시를 생성 -> 포인트컷으로 필터링
         * 이후 포인트컷으로 한번 더 어드바이스를 호출할지 여부를 확인한다.
         */
        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedNames("request*", "order*", "save*"); // 정밀한 포인트컷이 필요하다 -> AspectJExpressionPointcut

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

//    @Bean
    public Advisor advisor2(LogTrace logTrace) {
        //pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression("execution(* hello.proxy.app..*(..))");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }

    @Bean
    public Advisor advisor3(LogTrace logTrace) {
        //pointcut
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(
                "execution(* hello.proxy.app..*(..)) && " +
                "!execution(* hello.proxy.app..noLog(..)");

        // advice
        LogTraceAdvice advice = new LogTraceAdvice(logTrace);
        return new DefaultPointcutAdvisor(pointcut, advice);
    }
}
