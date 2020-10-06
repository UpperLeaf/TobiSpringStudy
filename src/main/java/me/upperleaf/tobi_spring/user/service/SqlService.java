package me.upperleaf.tobi_spring.user.service;

public interface SqlService {
    String getSql(String key) throws SqlRetrievalFailureException;
}
