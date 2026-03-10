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

  @Autowired
  private ProjectService projectService;

  public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

    // Exception: "Project not found"
    try {
      // Verify user owns the project
      Project project = projectService.findProjectByIdentifier(projectIdentifier, username);
      Backlog backlog = project.getBacklog();

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
      if (projectTask.getPriority() == null || projectTask.getPriority() == 0) {
        projectTask.setPriority(3);
      }

      // INITIAL status when status is null
      if (projectTask.getStatus() == "" || projectTask.getStatus() == null) {
        projectTask.setStatus("TO_DO");
      }

      return projectTaskRepository.save(projectTask);
    } catch (ProjectNotFoundException e) {
      throw e;
    } catch (Exception e) {
      throw new ProjectNotFoundException("Project with ID '" + projectIdentifier + "' not found");
    }
  }

  public Iterable<ProjectTask> findBacklogById(String id, String username){

    projectService.findProjectByIdentifier(id, username);

    return projectTaskRepository.findByProjectIdentifierOrderByPriority(id);
  }

  public ProjectTask findPTByProjectSequence(String backlog_id, String pt_id, String username) {

     // Verify user owns the project
     projectService.findProjectByIdentifier(backlog_id, username);

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

  public ProjectTask updateByProjectSequence(ProjectTask updatedTask, String backlog_id, String pt_id, String username){
    ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);

    projectTask.setSummary(updatedTask.getSummary());
    projectTask.setAcceptanceCriteria(updatedTask.getAcceptanceCriteria());
    projectTask.setStatus(updatedTask.getStatus());
    projectTask.setPriority(updatedTask.getPriority());
    projectTask.setDueDate(updatedTask.getDueDate());

    return projectTaskRepository.save(projectTask);
  }

  public void deletePTByProjectSequence(String backlog_id, String pt_id, String username){
    ProjectTask projectTask = findPTByProjectSequence(backlog_id, pt_id, username);

     projectTaskRepository.delete(projectTask);
  }
}
