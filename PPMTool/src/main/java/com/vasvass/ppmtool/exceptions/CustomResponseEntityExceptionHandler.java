package com.vasvass.ppmtool.exceptions;

import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * <p><i>Created on: 04/05/2019</i></p>
 *
 * @author vasvass
 */

@ControllerAdvice
@RestController
public class CustomResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler
  public final ResponseEntity<Object> handleProjectIdException(ProjectIdException ex, WebRequest request) {

    ProjectIdExceptionResponse exceptionResponse = new ProjectIdExceptionResponse(ex.getMessage());
    return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler
  public final ResponseEntity<Object> handleProjectNotFoundException(ProjectNotFoundException ex, WebRequest request) {

    ProjectNotFoundExceptionResponse exceptionResponse = new ProjectNotFoundExceptionResponse(ex.getMessage());
    return new ResponseEntity(exceptionResponse, HttpStatus.BAD_REQUEST);
  }



}
