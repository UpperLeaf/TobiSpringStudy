package me.upperleaf.tobi_spring.user.service;

public class SqlRetrievalFailureException extends RuntimeException {

    public SqlRetrievalFailureException(String message) {
        super(message);
    }

    public SqlRetrievalFailureException(String message, Throwable e) {
        super(message, e);
    }
}
