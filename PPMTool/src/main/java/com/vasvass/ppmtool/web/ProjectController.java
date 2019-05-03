package com.vasvass.ppmtool.web;

import com.vasvass.ppmtool.domain.Project;
import com.vasvass.ppmtool.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.*;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

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
  public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result){

     if (result.hasErrors()) {

       Map<String, String> errorMap = new HashMap<>();

       for(FieldError error: result.getFieldErrors()){
         errorMap.put(error.getField(), error.getDefaultMessage());
       }

        return new ResponseEntity<Map<String, String>>(errorMap, HttpStatus.BAD_REQUEST);
     }

     Project project1 = projectService.saveOrUpdateProject(project);
     return new ResponseEntity<Project>(project, HttpStatus.CREATED);
  }


}
