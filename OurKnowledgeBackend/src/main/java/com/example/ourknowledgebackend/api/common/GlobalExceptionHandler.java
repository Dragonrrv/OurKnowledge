package com.example.ourknowledgebackend.api.common;

import com.example.ourknowledgebackend.exceptions.DuplicateInstanceException;
import com.example.ourknowledgebackend.exceptions.InstanceNotFoundException;
import com.example.ourknowledgebackend.exceptions.PermissionException;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

//@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    private final static String INSTANCE_NOT_FOUND_EXCEPTION_CODE = "project.exceptions.InstanceNotFoundException";
    private final static String DUPLICATE_INSTANCE_EXCEPTION_CODE = "project.exceptions.DuplicateInstanceException";
    private final static String PERMISSION_EXCEPTION_CODE = "project.exceptions.PermissionException";

    private MessageSource messageSource;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDTO handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {

        List<FieldErrorDTO> fieldErrors = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new FieldErrorDTO(error.getField(), error.getDefaultMessage())).collect(Collectors.toList());

        return new ErrorsDTO(fieldErrors);

    }

    @ExceptionHandler(InstanceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorsDTO handleInstanceNotFoundException(InstanceNotFoundException exception, Locale locale) {

        String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
        String errorMessage = messageSource.getMessage(INSTANCE_NOT_FOUND_EXCEPTION_CODE,
                new Object[] {nameMessage, exception.getKey().toString()}, INSTANCE_NOT_FOUND_EXCEPTION_CODE, locale);

        return new ErrorsDTO(errorMessage);

    }

    @ExceptionHandler(DuplicateInstanceException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ErrorsDTO handleDuplicateInstanceException(DuplicateInstanceException exception, Locale locale) {

        String nameMessage = messageSource.getMessage(exception.getName(), null, exception.getName(), locale);
        String errorMessage = messageSource.getMessage(DUPLICATE_INSTANCE_EXCEPTION_CODE,
                new Object[] {nameMessage, exception.getKey().toString()}, DUPLICATE_INSTANCE_EXCEPTION_CODE, locale);

        return new ErrorsDTO(errorMessage);

    }

    @ExceptionHandler(PermissionException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorsDTO handlePermissionException(PermissionException exception, Locale locale) {

        String errorMessage = messageSource.getMessage(PERMISSION_EXCEPTION_CODE, null, PERMISSION_EXCEPTION_CODE,
                locale);

        return new ErrorsDTO(errorMessage);

    }
}
