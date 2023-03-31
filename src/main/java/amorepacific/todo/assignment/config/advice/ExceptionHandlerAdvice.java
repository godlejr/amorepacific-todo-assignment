package amorepacific.todo.assignment.config.advice;

import amorepacific.todo.assignment.model.ErrorResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice(basePackages = "amorepacific.todo.assignment.controller")
public class ExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ErrorResult badExceptionHandle(IllegalArgumentException e) {
        log.error("exception djdjdjdjdjj:", e);
        return new ErrorResult(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResult exceptionHandle(Exception e) {
        log.error("exception :", e);
        return new ErrorResult(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 오류");
    }


}
