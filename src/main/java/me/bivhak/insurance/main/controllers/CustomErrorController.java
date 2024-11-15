package me.bivhak.insurance.main.controllers;

import io.micrometer.common.lang.Nullable;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Date;

/**
 * ControllerAdvice for handling global exceptions and providing custom error responses.
 */
@ControllerAdvice
public class CustomErrorController extends ResponseEntityExceptionHandler {

    /**
     * Handles all exceptions and returns a custom error response.
     */
    @ExceptionHandler(Exception.class)
    private ResponseEntity<Object> handleAllExceptions(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatusCode statusCode, WebRequest request) {
        ErrorResponse response = new ErrorResponse(500, "Internal Server Error", ex.getMessage(), null);
        if (body instanceof ProblemDetail problemDetail) {
            response = new ErrorResponse(problemDetail.getStatus(), problemDetail.getTitle(), problemDetail.getDetail(), null);

            if (request instanceof ServletWebRequest servletWebRequest) {
                response.setPath(servletWebRequest.getRequest().getRequestURI());
            }
        }
        return new ResponseEntity<>(response, headers, statusCode);
    }
}

/**
 * Error response structure for custom error messages.
 */
@Data
@Setter
@Getter
final class ErrorResponse {
    private Date timestamp;
    private int status;
    private String error;
    private String details;
    private String path;

    public ErrorResponse(int status, String error, String details, String path) {
        this.timestamp = new Date();
        this.status = status;
        this.error = error;
        this.details = details;
        this.path = path;
    }
}
