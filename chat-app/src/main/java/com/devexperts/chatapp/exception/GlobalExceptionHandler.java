package com.devexperts.chatapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ObjectNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleObjectNotFoundException(ObjectNotFoundException ex, Model model) {

        ex.printStackTrace();
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler(UserLimitExceededException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleUserLimitExceeded(UserLimitExceededException ex, Model model) {

        ex.printStackTrace();
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/errorPage";
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleAccessDeniedException(AccessDeniedException ex, Model model) {
        ex.printStackTrace();
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/403";
    }
}



