package com.vasvass.ppmtool.web;

import com.vasvass.ppmtool.domain.ProjectTask;
import com.vasvass.ppmtool.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
 * <p><i>Created on: 29/08/2019</i></p>
 *
 * @author vasvass
 */

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class BacklogController {

  @Autowired
  private ProjectTaskService projectTaskService;

  @Autowired
  private MapValidationErrorService mapValidationErrorService;

  @PostMapping("/{backlog_id}")
  public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlog_id) {

     ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
     if (errorMap != null) return errorMap;

     ProjectTask projectTask1 = projectTaskService.addProjectTask(backlog_id, projectTask);

     return new ResponseEntity<ProjectTask>(projectTask1, HttpStatus.CREATED);
  }

  @GetMapping("/{backlog_id}")
    public Iterable<ProjectTask> getProjectBacklog(@PathVariable String backlog_id) {


    return projectTaskService.findBacklogById(backlog_id);
  }

  @GetMapping("/{backlog_id}/{pt_id}")
    public ResponseEntity<?> getProjectTask(@PathVariable String backlog_id, @PathVariable String pt_id){

    ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id, pt_id);
    return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
  }

  @PatchMapping("/{backlog_id}/{pt_id}")
  public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask projectTask, BindingResult result, @PathVariable String backlog_id, @PathVariable String pt_id){

    ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
    if (errorMap != null) return errorMap;

    ProjectTask updatedTask = projectTaskService.updateByProjectSequence(projectTask,backlog_id,pt_id);

    return new ResponseEntity<ProjectTask>(updatedTask, HttpStatus.OK);
  }

  @DeleteMapping("/{backlog_id}/{pt_id}")
  public ResponseEntity<?>deleteProject(@PathVariable String backlog_id, @PathVariable String pt_id){
     projectTaskService.deletePTByProjectSequence(backlog_id, pt_id);

     return new ResponseEntity<String>("Project Task "+pt_id+" was deleted successfully",HttpStatus.OK);
  }
}
