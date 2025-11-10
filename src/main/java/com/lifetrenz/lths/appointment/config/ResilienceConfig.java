package com.lifetrenz.lths.appointment.config;

import java.time.Duration;

import org.apache.kafka.common.errors.RetriableException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;

import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;


/**
 * @author Ajith.K
 *
 */
@Configuration
public class ResilienceConfig {

	@Value("${resilience4j.circuitbreaker.instances.kafkaCircuitBreaker.waitDurationInOpenState}")
    private int waitDurationInOpenState;

	@Value("${resilience4j.circuitbreaker.instances.kafkaCircuitBreaker.failureRateThreshold}")
    private float failureRateThreshold;
	
	@Value("${resilience4j.retry.instances.kafkaRetry.maxRetryAttempts}")
    private int maxRetryAttempts;
	
	@Value("${resilience4j.retry.instances.kafkaRetry.waitDuration}")
    private int waitDuration;

	@Bean
    public CircuitBreaker circuitBreaker() {
        CircuitBreakerConfig config = CircuitBreakerConfig.custom()
                .failureRateThreshold(failureRateThreshold) // Example threshold
                .waitDurationInOpenState(Duration.ofMillis(waitDurationInOpenState)) // Example wait duration
                .build();

        return CircuitBreaker.of("kafkaCircuitBreaker", config);
    }
	
	 @Bean
	    public Retry retry() {
	        RetryConfig config = RetryConfig.custom()
	                .maxAttempts(maxRetryAttempts) // Example max retry attempts
	                .waitDuration(Duration.ofMillis(waitDuration)) // Example wait duration
	                .retryExceptions(RetriableException.class) // Example retryable exception
	                .build();

	        return Retry.of("kafkaRetry", config);
	    }
}
