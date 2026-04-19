package com.group1.mediaservice.utility;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class LoggingAspect {
	
	@Before("execution(* com.group1.mediaservice..*(..))")
	public void logBefore(JoinPoint joinPoint) {
		log.info(
				"Entering method: {} with arguments: {}",
				joinPoint.getSignature(),
				joinPoint.getArgs()
		);
	}
	
	@AfterReturning(pointcut = "execution(* com.group1.mediaservice..*(..))",returning = "result")
	public void logAfterReturning(JoinPoint joinPoint , Object result) {
		log.info(
				"Existing method: {} with result: {}",
				joinPoint.getSignature(),
				result
		);
	}
	
	
	

}
