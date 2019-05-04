package com.vasvass.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p><i>Created on: 04/05/2019</i></p>
 *
 * @author vasvass
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectIdException extends RuntimeException {

  public ProjectIdException(String message) {
    super(message);
  }
}
