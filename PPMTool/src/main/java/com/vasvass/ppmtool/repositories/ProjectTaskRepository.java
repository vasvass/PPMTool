package com.vasvass.ppmtool.repositories;

import com.vasvass.ppmtool.domain.ProjectTask;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <p><i>Created on: 27/08/2019</i></p>
 *
 * @author vasvass
 */

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {

  List<ProjectTask> findByProjectIdentifierOrderByPriority(String id);
}
