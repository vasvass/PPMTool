package com.vasvass.ppmtool.repositories;

import com.vasvass.ppmtool.domain.Backlog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * <p><i>Created on: 27/08/2019</i></p>
 *
 * @author vasvass
 */

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {
}
