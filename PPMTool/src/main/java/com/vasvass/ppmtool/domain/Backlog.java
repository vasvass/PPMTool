package com.vasvass.ppmtool.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

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
   @OneToOne(fetch = FetchType.EAGER)
   @JoinColumn(name = "project_id",nullable = false)
   @JsonIgnore
   private Project project;

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

  public Project getProject() {
    return project;
  }

  public void setProject(Project project) {
    this.project = project;
  }
}
