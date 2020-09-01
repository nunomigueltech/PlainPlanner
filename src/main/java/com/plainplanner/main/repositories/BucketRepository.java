package com.plainplanner.main.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plainplanner.entities.Bucket;

@Repository
public interface BucketRepository extends JpaRepository<Bucket, Long> {
	Bucket findFirstByName(String name);
}
