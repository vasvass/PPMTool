package com.vasvass.ppmtool.exceptions;

/**
 * <p><i>Created on: 04/05/2019</i></p>
 *
 * @author vasvass
 */

public class ProjectIdExceptionResponse {

  private String projectIdentifier;

  public ProjectIdExceptionResponse(String projectIdentifier) {
    this.projectIdentifier = projectIdentifier;
  }

  public String getProjectIdentifier() {
    return projectIdentifier;
  }

  public void setProjectIdentifier(String projectIdentifier) {
    this.projectIdentifier = projectIdentifier;
  }
}
