package me.upperleaf.tobi_spring;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ContextConfiguration;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ContextConfiguration(locations = "/test-applicationContext.xml")
public class JUnitTest {

    @Autowired
    ApplicationContext context;

    static Set<JUnitTest> testObjects = new HashSet<>();
    static ApplicationContext applicationContext = null;

    @Test
    public void test1(){
        assertThat(testObjects, not(contains(this)));
        testObjects.add(this);

        assertThat((applicationContext == null || applicationContext == this.context), is(true));
        applicationContext = context;
    }

    @Test
    public void test2(){
        assertThat(testObjects, not(contains(this)));
        testObjects.add(this);

        assertTrue((applicationContext == null || applicationContext == this.context));
        applicationContext = context;
    }

    @Test
    public void test3(){
        assertThat(testObjects, not(contains(this)));
        testObjects.add(this);

        assertThat(applicationContext, either(is(nullValue())).or(is(this.context)));
        applicationContext = context;
    }
}
