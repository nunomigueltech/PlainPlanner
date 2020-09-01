package com.plainplanner.services;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.User;

public interface IUserService {
	User getUserByID(Long id);
	User getUserByUsername(String username);
	boolean userExists(String username);
	boolean addUser(User user);
	void removeUser(String username);
	void addBucket(User user, Bucket bucket);
	boolean removeBucket(User user, Bucket bucket);
}
