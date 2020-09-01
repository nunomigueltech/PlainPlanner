package com.plainplanner.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Project;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
}
