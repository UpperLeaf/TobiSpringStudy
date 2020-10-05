package me.upperleaf.tobi_spring.user.proxy;

import org.junit.jupiter.api.Test;

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

}
