package com.simocaccia.awsomepizza.configuration;

import com.simocaccia.awsomepizza.util.LoggingFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class LoggingFilterConfiguration {
    @Bean
    LoggingFilter loggingFilter(
            @Value("${logging.headers.enabled}") boolean loggingHeadersEnabled,
            @Value("${logging.headers.list}") List<String> loggingHeadersList
    ) {
        return new LoggingFilter(loggingHeadersEnabled, loggingHeadersList);
    }
}
