package com.vasvass.ppmtool.web;

import com.vasvass.ppmtool.domain.Project;
import com.vasvass.ppmtool.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

  @Autowired
  private MapValidationErrorService mapValidationErrorService;

  @PostMapping("")
  public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result){

      ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
      if (errorMap !=null) return errorMap;

     Project project1 = projectService.saveOrUpdateProject(project);
     return new ResponseEntity<Project>(project, HttpStatus.CREATED);
  }

  @GetMapping("/{projectId}")
  public ResponseEntity<?> getProjectById(@PathVariable String projectId){

     Project project = projectService.findProjectByIdentifier(projectId);

    return new ResponseEntity<Project>(project, HttpStatus.OK);
  }

  @GetMapping("/all")
  public Iterable<Project> getAllProjects(){
    return projectService.findAllProjects();
  }


}
