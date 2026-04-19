package com.group1.user.aspect;

import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
	
	@AfterThrowing(pointcut="(execution(* com.group1.user.service.UserServiceImpl.*(..)))" ,throwing="exception")
	public void logException(Exception exception) {
		log.error(exception.getMessage(),exception);
	}
}
