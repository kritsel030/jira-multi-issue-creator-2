package com.kramphub.jiramic.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RestLoggingInterceptor implements ClientHttpRequestInterceptor {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public ClientHttpResponse intercept(
            HttpRequest req, byte[] reqBody, ClientHttpRequestExecution ex) throws IOException {
        logger.info("Request URI: {} {}", req.getMethod(), req.getURI());
        logger.info("Request body: {}", new String(reqBody, StandardCharsets.UTF_8));
        return ex.execute(req, reqBody);
    }
}