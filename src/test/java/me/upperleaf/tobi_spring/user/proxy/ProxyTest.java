package me.upperleaf.tobi_spring.user.proxy;

import org.aopalliance.intercept.MethodInterceptor;
import org.junit.jupiter.api.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import java.lang.reflect.Proxy;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class ProxyTest {

    @Test
    public void simpleProxy() {
        Hello hello1 = new HelloTarget();
        assertThat(hello1.sayHello("Tobi"), is("Hello Tobi"));
        assertThat(hello1.sayHi("Tobi"), is("Hi Tobi"));
        assertThat(hello1.sayThankYou("Tobi"), is("Thank you Tobi"));

        Hello hello2 = new HelloUppercase(new HelloTarget());
        assertThat(hello2.sayHello("Tobi"), is ("HELLO TOBI"));
        assertThat(hello2.sayHi("Tobi"), is("HI TOBI"));
        assertThat(hello2.sayThankYou("Tobi"), is("THANK YOU TOBI"));
    }

    @Test
    public void dynamicProxyTest() {
        Hello dynamicProxyHello = (Hello)Proxy.newProxyInstance(getClass().getClassLoader(),
                new Class[]{Hello.class}, new UppercaseHandler(new HelloTarget()));

        assertThat(dynamicProxyHello.sayHello("Tobi"), is ("HELLO TOBI"));
        assertThat(dynamicProxyHello.sayHi("Tobi"), is("HI TOBI"));
        assertThat(dynamicProxyHello.sayThankYou("Tobi"), is("THANK YOU TOBI"));
    }

    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());
        pfBean.addAdvice((MethodInterceptor) invocation -> {
            String ret = (String) invocation.proceed();
            return ret.toUpperCase();
        });

        Hello proxiedHello = (Hello)pfBean.getObject();
        assertThat(proxiedHello.sayHello("Tobi"), is ("HELLO TOBI"));
        assertThat(proxiedHello.sayHi("Tobi"), is("HI TOBI"));
        assertThat(proxiedHello.sayThankYou("Tobi"), is("THANK YOU TOBI"));
    }

    @Test
    public void pointcutAdvisor() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget());

        NameMatchMethodPointcut pointcut = new NameMatchMethodPointcut();
        pointcut.setMappedName("sayH*");
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, (MethodInterceptor) invocation ->  {
            String ret = (String)invocation.proceed();
            return ret.toUpperCase();
        }));

        Hello proxiedHello = (Hello)pfBean.getObject();
        assertThat(proxiedHello.sayHello("Tobi"), is ("HELLO TOBI"));
        assertThat(proxiedHello.sayHi("Tobi"), is("HI TOBI"));
        assertThat(proxiedHello.sayThankYou("Tobi"), is("Thank you Tobi"));
    }

    @Test
    public void classNamePointcutAdvisor() {
        NameMatchMethodPointcut classNamePointcut = new NameMatchMethodPointcut() {
            @Override
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };

        classNamePointcut.setMappedName("sayH*");

        checkAdvice(new HelloTarget(), classNamePointcut, true);

        class HelloWorld extends HelloTarget {};
        class HelloToby extends HelloTarget {};

        checkAdvice(new HelloWorld(), classNamePointcut, false);

        checkAdvice(new HelloToby(), classNamePointcut, true);
    }

    private void checkAdvice(Object target, Pointcut pointcut, boolean advice) {
        ProxyFactoryBean pf = new ProxyFactoryBean();
        pf.setTarget(target);
        pf.addAdvisor(new DefaultPointcutAdvisor(pointcut, (MethodInterceptor)invocation-> {
            String ret = (String)invocation.proceed();
            return ret.toUpperCase();
        }));

        Hello proxiedHello = (Hello) pf.getObject();

        if(advice) {
            assertThat(proxiedHello.sayHello("Toby"), is("HELLO TOBY"));
            assertThat(proxiedHello.sayHi("Toby"), is("HI TOBY"));
            assertThat(proxiedHello.sayThankYou("Toby"), is("Thank you Toby"));
        }
        else {
            assertThat(proxiedHello.sayHello("Toby"), is("Hello Toby"));
            assertThat(proxiedHello.sayHi("Toby"), is("Hi Toby"));
            assertThat(proxiedHello.sayThankYou("Toby"), is("Thank you Toby"));
        }

    }
}
