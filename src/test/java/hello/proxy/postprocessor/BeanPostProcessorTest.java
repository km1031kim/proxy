package hello.proxy.postprocessor;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

public class BeanPostProcessorTest {

    @Test
    void basicConfig() {
        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(BeanPostProcessorConfig.class);

        // A는 B로 교체되어 빈으로 등록된다
        B b = applicationContext.getBean("a", B.class);
        b.helloB();

        // A는 빈으로 등록되지 않는다
        Assertions.assertThrows(NoSuchBeanDefinitionException.class, () -> applicationContext.getBean(A.class));
    }

    @Slf4j
    @Configuration
    static class BeanPostProcessorConfig {
        @Bean
        public A a() {
            return new A();
        }

        @Bean
        public AToBPostProcessor helloPostProcessor() {
            return new AToBPostProcessor();
        }
    }

    @Slf4j
    static class A {
        public void helloA() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class B {
        public void helloB() {
            log.info("hello A");
        }
    }

    @Slf4j
    static class AToBPostProcessor implements BeanPostProcessor {

        /**
         * A 객체를 B 객체로 바꿔치기
         * BeanPostProcessor 를 구현하고 빈으로 등록하면 스프링 컨테이너가 빈 후처리기로 인식하고 동작한다.
         * @PostConstruct는 CommonAnnotationBeanPostProcessor 가 처리한다.
         * @param bean
         * @param beanName
         * @return
         * @throws BeansException
         */
        @Override
        public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
            log.info("빈 이름이랑 객체 전달. beanName={}, bean={}", beanName, bean);
            if (bean instanceof A) {
                log.info("A -> B 교체");
                return new B();
            }
            return bean;
        }
    }
}
