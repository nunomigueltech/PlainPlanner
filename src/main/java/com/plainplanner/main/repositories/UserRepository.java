package com.plainplanner.main.repositories;

import java.util.List;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.plainplanner.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	List<User> findByUsername(String username);
	User findFirstByUsername(String username);
}
