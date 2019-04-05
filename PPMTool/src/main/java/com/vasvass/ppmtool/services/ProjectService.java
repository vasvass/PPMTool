package com.vasvass.ppmtool.services;

import com.vasvass.ppmtool.domain.Project;
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


   public Project saveOrUpadateProject(Project project) {

      //Logic

     return projectRepository.save(project);
   }


}
