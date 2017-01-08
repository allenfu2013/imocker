package org.allen.imocker.httpclient;

import java.util.Map;

public interface HttpOperation {

    <T> T get(String url, Map<String, Object> headers, Map<String, Object> parameters, Class<T> t, int timeout) throws HttpException;

    <T> T post(String url, Map<String, Object> headers, Map<String, Object> parameters, Class<T> t, int timeout) throws HttpException;

    <T> T postJson(String url, Map<String, Object> headers, String json, Class<T> t, int timeout) throws HttpException;
}