package com.vasvass.ppmtool.services;

import com.vasvass.ppmtool.domain.Project;
import com.vasvass.ppmtool.exceptions.ProjectIdException;
import com.vasvass.ppmtool.repositories.ProjectRepository;
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


   public Project saveOrUpdateProject(Project project) {

     try{
       project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
       return projectRepository.save(project);
     } catch (Exception e) {
       throw new ProjectIdException("ProjectID' "+ project.getProjectIdentifier().toUpperCase() + " 'already exists ");
     }
   }


}
