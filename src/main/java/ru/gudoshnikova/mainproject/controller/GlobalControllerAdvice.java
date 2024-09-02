package ru.gudoshnikova.mainproject.controller;

import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.web.bind.annotation.ExceptionHandler;

//@org.springframework.web.bind.annotation.ControllerAdvice
public class GlobalControllerAdvice {
//    @ExceptionHandler(KafkaException.class)
//    public String handleListenerExecutionFailedException(KafkaException ex) {
//        Throwable cause = ex.getCause();
//
//        if (cause instanceof BookedException) {
//            return "orders/error";
//        }
//        return cause.getMessage();
//    }
    @ExceptionHandler(ListenerExecutionFailedException.class)
    public String handleListenerExecutionFailedException(
            ListenerExecutionFailedException ex) {
        Throwable rootCause = ex.getCause();
//        if (rootCause instanceof BookedException) {
//            return "orders/error";
//        } else {
//            return "orders/error";
//        }
        return "orders/error";
    }
}
