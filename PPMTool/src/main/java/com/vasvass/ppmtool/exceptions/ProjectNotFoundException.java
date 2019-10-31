package com.vasvass.ppmtool.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * <p><i>Created on: 31/10/19</i></p>
 *
 * @author vasvass
 */

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ProjectNotFoundException extends RuntimeException {

  public ProjectNotFoundException(String message) {
    super(message);
  }
}
