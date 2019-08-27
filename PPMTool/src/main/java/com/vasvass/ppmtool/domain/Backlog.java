package com.vasvass.ppmtool.domain;

import javax.persistence.*;

/**
 * <p><i>Created on: 27/08/2019</i></p>
 *
 * @author vasvass
 */

@Entity
public class Backlog {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;
  private Integer PTSequence = 0;
  private String projectIdentifier;

  // OneToOne with project


  // OneToMany projectTasks

  public Backlog() {
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public Integer getPTSequence() {
    return PTSequence;
  }

  public void setPTSequence(Integer PTSequence) {
    this.PTSequence = PTSequence;
  }

  public String getProjectIdentifier() {
    return projectIdentifier;
  }

  public void setProjectIdentifier(String projectIdentifier) {
    this.projectIdentifier = projectIdentifier;
  }
}
