package com.simocaccia.awsomepizza.util;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Component
@Slf4j
public class LoggingFilter extends OncePerRequestFilter {

    final boolean loggingHeadersEnabled;

    final List<String> loggingHeadersList;

    @Autowired
    public LoggingFilter(boolean loggingHeadersEnabled, List<String> loggingHeadersList) {
        this.loggingHeadersEnabled = loggingHeadersEnabled;
        this.loggingHeadersList = loggingHeadersList;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        ContentCachingRequestWrapper req = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper resp = new ContentCachingResponseWrapper(response);

        filterChain.doFilter(req, resp);

        byte[] requestBody = req.getContentAsByteArray();
        byte[] responseBody = resp.getContentAsByteArray();

        StringBuilder sbReqHeaders = getRequestHeadersToLogFrom(req);


        log.info("""

                        ======================== REQUEST START ========================
                            URI: {}
                            Method: {}{}
                            Body: {}
                        ========================= REQUEST END =========================
                        """,
                req.getRequestURL(),
                req.getMethod(),
                sbReqHeaders,
                new String(requestBody, StandardCharsets.UTF_8)
        );

        StringBuilder sbRespHeaders = getResponseHeadersToLogFrom(resp);

        log.info("""

                        ======================== RESPONSE START ========================
                            Status: {}{}
                            Body: {}
                        ========================= RESPONSE END =========================
                        """,
                resp.getStatus(),
                sbRespHeaders,
                new String(responseBody, StandardCharsets.UTF_8)
        );

        resp.copyBodyToResponse();

    }

    private StringBuilder getRequestHeadersToLogFrom(ContentCachingRequestWrapper requestWrapper) {
        StringBuilder stringBuilder = new StringBuilder();

        if (!loggingHeadersEnabled) return stringBuilder;

        stringBuilder.append("\n\tHeaders: ");
        var reqHeaders = Collections.list(requestWrapper.getHeaderNames());
        Stream.of(reqHeaders).forEach(headerList ->
                headerList.forEach(headerName -> {
                    if (loggingHeadersList.contains(headerName)) {
                        stringBuilder.append("\n\t\t").append(headerName).append(": ").append(requestWrapper.getHeader(headerName));
                    }
                })
        );
        return stringBuilder;
    }

    private StringBuilder getResponseHeadersToLogFrom(ContentCachingResponseWrapper responseWrapper) {
        StringBuilder stringBuilder = new StringBuilder();

        if (!loggingHeadersEnabled) return stringBuilder;

        stringBuilder.append("\n\tHeaders: ");
        responseWrapper.getHeaderNames().forEach(headerName -> {
            if (loggingHeadersList.contains(headerName)) {
                stringBuilder.append("\n\t\t").append(headerName).append(": ").append(responseWrapper.getHeader(headerName));
            }
        });
        return stringBuilder;
    }
}
