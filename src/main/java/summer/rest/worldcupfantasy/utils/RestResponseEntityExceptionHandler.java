package summer.rest.worldcupfantasy.utils;

import org.springframework.beans.TypeMismatchException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import summer.rest.worldcupfantasy.models.ApiErrorResponse;


@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {Exception.class})
    protected ResponseEntity<Object> handleGlobal(Exception ex, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR,ex.getMessage());
        return handle(errorResponse,request);
    }

    @ExceptionHandler(value = {ApiErrorResponse.class})
    protected ResponseEntity<Object> handle(ApiErrorResponse ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getBody(),  new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(status,ex.getMessage());
        return handle(errorResponse, request);
    }

    @Override
    protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(status,ex.getMessage());
        return handle(errorResponse, request);
    }


}
