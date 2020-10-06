package me.upperleaf.tobi_spring.user.pointcut;

import org.junit.jupiter.api.Test;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class PointcutExpressionTest {

    @Test
    public void methodSignaturePointcut() throws SecurityException, NoSuchMethodException {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();

        pointcut.setExpression("execution(public int me.upperleaf.tobi_spring.user.pointcut.Target.minus(int, int) throws java.lang.RuntimeException)");
        assertThat(pointcut.getClassFilter().matches(Target.class)
                && pointcut.getMethodMatcher().matches(Target.class.getMethod("minus", int.class, int.class), null), is (true));

        assertThat(pointcut.getClassFilter().matches(Bean.class) && pointcut.getMethodMatcher().matches(Target.class.getMethod("method"), null), is(false));
    }

}
