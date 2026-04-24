package ru.bsuedu.cad.lab.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Aspect
@Component
public class ZamerAspect {
    @Around("execution(* ru.bsuedu.cad.lab.parser.CSVParser.parse(..))")
    public Object measureParseTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.nanoTime();
        String startTimeReadable = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH::mm:ss.SSS"));

        System.out.println("[AOP] начало парсинга CSV в: " + startTimeReadable);

        Object result = joinPoint.proceed();

        long endTime = System.nanoTime();
        String endTimeReadable = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss.SSS"));

        double durationMs = (endTime - startTime) / 1_000_000.0;

        System.out.println("[AOP] Окончание парсинга CSV в: " + endTimeReadable);
        System.out.printf("[AOP] Время парсинга CSV файла: %.3f мс\n", durationMs);

        return result;
    }
}
