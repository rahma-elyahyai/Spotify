package com.endava.mss.aspects;

import java.time.LocalDateTime;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@Component
public class LoggingAspect {

	@Pointcut("execution(* com.endava.mss.controller..*(..)) && !within(com.endava.mss.aspect..*)")
	public void loggingPointCutForControllers() {
	}

	@Pointcut("execution(* com.endava.mss.serviceImpl..*(..)) && !within(com.endava.mss.aspect..*)")
	public void loggingPointCutForServices() {
	}

	@AfterReturning(pointcut = "loggingPointCutForControllers()", returning = "result")
	public void logAfterReturningForControllers(JoinPoint joinPoint, Object result) {
		log.info("Method " + joinPoint.getSignature().getName()); 
	}

	@AfterThrowing(pointcut = "loggingPointCutForControllers()", throwing = "exception")
	public void logAfterThrowingForControllers(JoinPoint joinPoint, Throwable exception) {
		log.info("Method " + joinPoint.getSignature().getName() + " threw exception: " + exception.getMessage());
	}

	@Around("loggingPointCutForControllers()")
	public Object aroundLoggingForControllers(ProceedingJoinPoint joinPoint) throws Throwable {
		log.info("Before method: " + joinPoint.getSignature().getName());
		Object result = joinPoint.proceed();
		log.info("After method: " + joinPoint.getSignature().getName());
		return result;
	}

	@AfterReturning(pointcut = "loggingPointCutForServices()", returning = "result")
	public void logAfterReturningForServices(JoinPoint joinPoint, Object result) {
		log.info("Method " + joinPoint.getSignature().getName());// + " returned value: " + result);
	}

	@AfterThrowing(pointcut = "loggingPointCutForServices()", throwing = "exception")
	public void logAfterThrowingForServices(JoinPoint joinPoint, Throwable exception) {
		log.info("Method " + joinPoint.getSignature().getName() + " threw exception: " + exception.getMessage());
	}

	@Around("loggingPointCutForServices()")
	public Object aroundLoggingForServices(ProceedingJoinPoint joinPoint) throws Throwable {
		long startTime=System.currentTimeMillis();
		log.info("Before method: " + joinPoint.getSignature().getName());
		Object result = joinPoint.proceed();
		long afterTime=System.currentTimeMillis()-startTime;
		log.info("Time taken : " + afterTime);
		log.info("After method: " + joinPoint.getSignature().getName());
		return result;
	}
}
