package com.vasvass.ppmtool.services;

import com.vasvass.ppmtool.domain.*;
import com.vasvass.ppmtool.exceptions.ProjectIdException;
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


   public Project saveOrUpdateProject(Project project) {

     try{
       project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());

       if (project.getId()==null) {
         Backlog backlog = new Backlog();
         project.setBacklog(backlog);
         backlog.setProject(project);
         backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
       }

       if (project.getId() != null) {
         project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
       }

       return projectRepository.save(project);

     } catch (Exception e) {
       throw new ProjectIdException("ProjectID' "+ project.getProjectIdentifier().toUpperCase() + " 'already exists ");
     }
   }

   public Project findProjectByIdentifier(String projectId) {

     Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());

     if (project == null) {
        throw new ProjectIdException("Project ID '"+projectId+"' does not exist!");
     }

     return project;
   }

   public Iterable<Project> findAllProjects(){
     return projectRepository.findAll();
   }

   public void deleteProjectByIdentifier(String projectId) {
     Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
     if(project == null){
       throw new ProjectIdException("Cannot delete Project with ID '"+projectId+"' . This project does not exist");
     }
     projectRepository.delete(project);
   }

}
