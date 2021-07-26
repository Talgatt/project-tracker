package com.personaltools.ppmtool.repositories;

import com.personaltools.ppmtool.domain.Project;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Project repository
 */
@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {
//    @Override
//    Iterable<Project> findAllById(Iterable<Long> iterable);

    Project findByProjectIdentifier(String projectId);

    @Override
    Iterable<Project> findAll();

    Iterable<Project> findAllByProjectLeader(String username);
}
