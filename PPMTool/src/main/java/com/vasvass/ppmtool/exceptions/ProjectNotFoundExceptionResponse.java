package com.vasvass.ppmtool.exceptions;


/**
 * <p><i>Created on: 31/10/19</i></p>
 *
 * @author vasvass
 */
public class ProjectNotFoundExceptionResponse {

  private String ProjectNotFound;

  public ProjectNotFoundExceptionResponse(String projectNotFound){
    ProjectNotFound = projectNotFound;
  }

  public void setProjectNotFound(String projectNotFound) {
    ProjectNotFound = projectNotFound;
  }
}
