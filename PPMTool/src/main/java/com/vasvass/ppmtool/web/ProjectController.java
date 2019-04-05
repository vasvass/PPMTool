package com.vasvass.ppmtool.web;

import com.vasvass.ppmtool.domain.Project;
import com.vasvass.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

/**
 * <p><i>Created on: 05/04/2019</i></p>
 *
 * @author vasvass
 */


@RestController
@RequestMapping("/api/project")
public class ProjectController {


  @Autowired
  private ProjectService projectService;

  @PostMapping("")
  public ResponseEntity<Project> createNewProject(@RequestBody Project project){

     Project project1 = projectService.saveOrUpadateProject(project);

     return new ResponseEntity<Project>(project, HttpStatus.CREATED);
  }


}
