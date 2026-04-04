package com.vasvass.ppmtool.web;

import com.vasvass.ppmtool.domain.Project;
import com.vasvass.ppmtool.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.security.Principal;

/**
 * <p><i>Created on: 05/04/2019</i></p>
 *
 * @author vasvass
 */


@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {


  @Autowired
  private ProjectService projectService;

  @Autowired
  private MapValidationErrorService mapValidationErrorService;

  @PostMapping("")
  public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal){

      ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
      if (errorMap !=null) return errorMap;

     Project project1 = projectService.saveOrUpdateProject(project, principal.getName());
     return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
  }

  @GetMapping("/{projectId}")
  public ResponseEntity<?> getProjectById(@PathVariable String projectId, Principal principal){

     Project project = projectService.findProjectByIdentifier(projectId, principal.getName());

    return new ResponseEntity<Project>(project, HttpStatus.OK);
  }

  @GetMapping("/all")
  public Iterable<Project> getAllProjects(Principal principal){
    return projectService.findAllProjects(principal.getName());
  }

  @PutMapping("/{projectId}")
  public ResponseEntity<?> updateProject(@Valid @RequestBody Project project, BindingResult result,
                                          @PathVariable String projectId, Principal principal) {
    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if (errorMap != null) return errorMap;

    project.setProjectIdentifier(projectId.toUpperCase());
    Project updatedProject = projectService.saveOrUpdateProject(project, principal.getName());
    return new ResponseEntity<>(updatedProject, HttpStatus.OK);
  }

  @DeleteMapping("/{projectId}")
  public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){

    projectService.deleteProjectByIdentifier(projectId, principal.getName());

    return new ResponseEntity<String>("Project with ID: '"+projectId+"' was deleted", HttpStatus.OK);
  }

}
