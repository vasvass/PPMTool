package com.vasvass.ppmtool.services;

import com.vasvass.ppmtool.domain.*;
import com.vasvass.ppmtool.exceptions.ProjectNotFoundException;
import com.vasvass.ppmtool.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p><i>Created on: 29/08/2019</i></p>
 *
 * @author vasvass
 */

@Service
public class ProjectTaskService {

  @Autowired
  private BacklogRepository backlogRepository;

  @Autowired
  private ProjectTaskRepository projectTaskRepository;

  @Autowired
  private ProjectRepository projectRepository;

  public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask) {

    // Exception: "Project not found"
    try {
      // All PTs to be added to a specific project, project != null, Backlog exists
      Backlog backlog = backlogRepository.findByProjectIdentifier(projectIdentifier);
      // Set the Backlog to the ProjectTask
      projectTask.setBacklog(backlog);
      // Project sequence to be like this IDPRO-1 IDPRO-2 ...
      Integer BacklogSequence = backlog.getPTSequence();
      // Update the Backlog sequence
      BacklogSequence++;

      backlog.setPTSequence(BacklogSequence);

      // add sequence to projectTask
      projectTask.setProjectSequence(backlog.getProjectIdentifier() + "-" + BacklogSequence);
      projectTask.setProjectIdentifier(projectIdentifier);

      // INITIAL Priority when priority is null
      // ToDo projectTask.getPriority() == 0 to handle the form
      if (projectTask.getPriority() == null) {
        projectTask.setPriority(3);
      }

      // INITIAL status when status is null
      if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
        projectTask.setStatus("TO_DO");
      }

      return projectTaskRepository.save(projectTask);
    } catch (Exception e) {
        throw new ProjectNotFoundException("Project Not Found!");
    }
  }

  public Iterable<ProjectTask>findBacklogById(String id){

    Project project = projectRepository.findByProjectIdentifier(id);

    if (project == null) {
      throw new ProjectNotFoundException("Project with ID: '"+id+"' does not exist");
    }
    return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
  }

  public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id) {

     // make sure we are searching on an existing backlog
     Backlog backlog = backlogRepository.findByProjectIdentifier(backlog_id);

     if (backlog==null) {
       throw new ProjectNotFoundException("Project with ID: '"+backlog_id+"' does not exist");
     }

     // make sure that our task exists
     ProjectTask projectTask = projectTaskRepository.findByProjectSequence(pt_id);

      if (projectTask==null){
        throw new ProjectNotFoundException("Project Task  '"+pt_id+"' not found");
      }

     // make sure that the backlog/project id in the path corresponds
      if (!projectTask.getProjectIdentifier().equals(backlog_id)) {
        throw new ProjectNotFoundException("Project Task '"+pt_id+"' does not exist in project: '"+backlog_id);
      }

     return projectTask;
  }

}


