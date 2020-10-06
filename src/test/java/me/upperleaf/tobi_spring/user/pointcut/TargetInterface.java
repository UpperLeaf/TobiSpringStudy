package me.upperleaf.tobi_spring.user.pointcut;

public interface TargetInterface {
    void hello();
    void hello(String s);
    int minus(int a, int b) throws RuntimeException;
    int plus(int a, int b);
}
