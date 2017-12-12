package com.procurement.access.controller;

import com.procurement.access.model.dto.bpe.ResponseDetailsDto;
import com.procurement.access.model.dto.bpe.ResponseDto;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.util.stream.Collectors.toList;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseDto methodArgumentNotValidException(final MethodArgumentNotValidException e) {
        return new ResponseDto(false, getErrors(e.getBindingResult()), null);
    }

    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseDto handle(final ConstraintViolationException e) {
        return new ResponseDto(false, getErrors(e), null);
    }

    private List<ResponseDetailsDto> getErrors(final BindingResult result) {
        return result.getFieldErrors()
                     .stream()
                     .map(f -> new ResponseDetailsDto(
                         f.getField(),
                         f.getCode() + " : " + f.getDefaultMessage()))
                     .collect(Collectors.toList());
    }

    private List<ResponseDetailsDto> getErrors(final ConstraintViolationException e) {
        return e.getConstraintViolations()
                .stream()
                .map(f -> new ResponseDetailsDto(
                    f.getPropertyPath()
                     .toString(),
                    f.getMessage() + " " + f.getMessageTemplate()))
                .collect(toList());
    }
}