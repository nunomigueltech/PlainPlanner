package com.plainplanner.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;

@Repository
public interface IdeaRepository extends JpaRepository<Idea, Long> {
}
