package hello.proxy.advisor;


import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import hello.proxy.proxyfactory.ProxyFactoryTest;
import org.junit.jupiter.api.Test;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.DefaultPointcutAdvisor;

public class AdvisorTest {

    @Test
    void advisorTest1() {
        ServiceInterface target = new ServiceImpl();
        ProxyFactory proxyFactory = new ProxyFactory(target);

        /**
         * DefaultPointcutAdvisor : Advisor 인터페이스의 가장 일반적인 구현체. 하나의 포인트컷과 하나의 어드바이스
         * Pointcut.TRUE : 항상 true를 반환하는 포인트컷
         * 단순히 addAdvice로 어드바이스를 바로 적용하는 경우 아래와 같은 어드바이저가 생성된다.
         */
        DefaultPointcutAdvisor advisor = new DefaultPointcutAdvisor(Pointcut.TRUE, new TimeAdvice());
        proxyFactory.addAdvisor(advisor);
        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        proxy.save();
        proxy.find();
    }
}
