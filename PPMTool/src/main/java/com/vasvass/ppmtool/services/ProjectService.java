package com.vasvass.ppmtool.services;

import com.vasvass.ppmtool.domain.*;
import com.vasvass.ppmtool.exceptions.ProjectIdException;
import com.vasvass.ppmtool.exceptions.ProjectNotFoundException;
import com.vasvass.ppmtool.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p><i>Created on: 05/04/2019</i></p>
 *
 * @author vasvass
 */


@Service
public class ProjectService {

   @Autowired
   private ProjectRepository projectRepository;

   @Autowired
   private BacklogRepository backlogRepository;

   @Autowired
   private UserRepository userRepository;


   public Project saveOrUpdateProject(Project project, String username) {

     try{
       project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

       if (project.getId()==null) {
         Backlog backlog = new Backlog();
         project.setBacklog(backlog);
         backlog.setProject(project);
         backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
       }

       if (project.getId() != null) {
         Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase());

         if (existingProject != null && !existingProject.getProjectLeader().equals(username)) {
           throw new ProjectNotFoundException("Project not found in your account");
         }

         project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
       }

       User user = userRepository.findByUsername(username);
       project.setUser(user);
       project.setProjectLeader(user.getUsername());

       return projectRepository.save(project);

     } catch (Exception e) {
       throw new ProjectIdException("ProjectID' "+ project.getProjectIdentifier().toUpperCase() + " 'already exists ");
     }
   }

   public Project findProjectByIdentifier(String projectId, String username) {

     Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

     if (project == null) {
        throw new ProjectIdException("Project ID '"+projectId+"' does not exist!");
     }

     if (!project.getProjectLeader().equals(username)) {
       throw new ProjectNotFoundException("Project not found in your account");
     }

     return project;
   }

   public Iterable<Project> findAllProjects(String username){
     return projectRepository.findAllByProjectLeader(username);
   }

   public void deleteProjectByIdentifier(String projectId, String username) {
     projectRepository.delete(findProjectByIdentifier(projectId, username));
   }

}
