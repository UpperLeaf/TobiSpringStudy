package me.upperleaf.tobi_spring.user.message;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

@SpringBootTest
@ContextConfiguration(locations = "/proxyfactory-bean-test.xml")
public class FactoryBeanTest {

    @Autowired
    ApplicationContext applicationContext;


    @Test
    public void getMessageFromFactoryBean() {
        Object message = applicationContext.getBean("message");
        assertThat(message.getClass(), is(Message.class));
        assertThat(((Message)message).getText(), is("Factory Bean"));
    }
}
