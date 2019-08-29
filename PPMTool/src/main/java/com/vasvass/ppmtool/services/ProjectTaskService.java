package com.vasvass.ppmtool.services;

import com.vasvass.ppmtool.domain.*;
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

  public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask){

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
    projectTask.setProjectSequence(backlog.getProjectIdentifier()+"-"+BacklogSequence);
    projectTask.setProjectIdentifier(projectIdentifier);

    // INITIAL Priority when priority is null
    // ToDo projectTask.getPriority() == 0 to handle the form
   if(projectTask.getPriority()==null) {
     projectTask.setPriority(3);
   }

    // INITIAL status when status is null
    if(projectTask.getStatus()=="" || projectTask.getStatus()==null){
      projectTask.setStatus("TO_DO");
    }

 return projectTaskRepository.save(projectTask);
  }


}