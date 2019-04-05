package com.vasvass.ppmtool.repositories;

/**
 * <p><i>Created on: 05/04/2019</i></p>
 *
 * @author vasvass
 */

import com.vasvass.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

   @Override
    Iterable<Project> findAllById(Iterable<Long> iterable);

}
