package com.example.wherenextbackend.components;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

    @ControllerAdvice
    public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {
//        @ExceptionHandler
//        public ProblemDetail handleEntityNotFound(EntityNotFoundException e) {
//            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
//            problemDetail.setTitle("Id nicht gefunden!");
//            problemDetail.setDetail(e.getMessage());
//            return problemDetail;
//        }
//        @ExceptionHandler
//        public ProblemDetail handleEntityBadRequest(EntityBadRequestException e) {
//            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
//            problemDetail.setTitle("Fehlerhafter Request!");
//            problemDetail.setDetail(e.getMessage());
//            return problemDetail;
//        }
    }
