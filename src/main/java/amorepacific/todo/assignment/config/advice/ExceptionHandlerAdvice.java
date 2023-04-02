package amorepacific.todo.assignment.config.advice;

import amorepacific.todo.assignment.model.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.server.ResponseStatusException;

import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice(basePackages = "amorepacific.todo.assignment.controller")
public class ExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error("exception :", e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage(), e.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ResponseStatusException.class)
    public ErrorResponse responseStatusException(ResponseStatusException e) {
        log.error("exception :", e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getReason(), e.toString());
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NoSuchElementException.class)
    public ErrorResponse noSuchElementException(NoSuchElementException e) {
        log.error("exception :", e);
        return new ErrorResponse(HttpStatus.BAD_REQUEST.value(), e.getLocalizedMessage(), e.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ErrorResponse methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.error("exception :", e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "입력 유형을 확인하세요", e.toString());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ErrorResponse runtimeException(RuntimeException e) {
        log.error("exception :", e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), e.getLocalizedMessage(), e.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(DateTimeParseException.class)
    public ErrorResponse dateTimeParseException(DateTimeParseException e) {
        log.error("exception :", e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "날짜 형식(예시: 2023-04-01)을 확인하세요", e.toString());
    }


    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ErrorResponse httpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error("exception :", e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "입력 JSON을 확인하세요", e.toString());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler
    public ErrorResponse exceptionHandle(Exception e) {
        log.error("exception :", e);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), "내부 오류", e.toString());
    }
}
