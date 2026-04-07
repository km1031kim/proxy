package hello.proxy.config.v6_aop;

import hello.proxy.config.AppV1Config;
import hello.proxy.config.AppV2Config;
import hello.proxy.config.v6_aop.aspect.LogTraceAspect;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({AppV1Config.class, AppV2Config.class})
public class AopConfig {

    /**
     * 자동 프록시 생성기는 2가지 일을 한다.
     * @Aspect를 보고 Advisor로 변환해서 저장
     * Advisor를 기반으로 프록시를 생성한다.
     */
    @Bean
    public LogTraceAspect logTraceAspect(LogTrace logTrace) {
        // @Aspect 어노테이션이 적용된 빈을 반환 -> Advisor 등록
        return new LogTraceAspect(logTrace);
    }
}
