package com.plainplanner.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.User;
import com.plainplanner.main.repositories.UserRepository;

@Service
@Transactional
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository repository;

	@Override
	public User getUserByID(Long id) {
		User result = null;
		
		Optional<User> user = repository.findById(id);
		if (user.isPresent()) {
			result = user.get();
		}
		
		return result;
	}

	@Override
	public User getUserByUsername(String username) {
		User result = repository.findFirstByUsername(username);
		
		return result;
	}

	@Override
	public boolean userExists(String username) {
		return getUserByUsername(username) != null;
	}

	@Override
	public boolean addUser(User newUser) {
		if (newUser == null) return false;
		if (userExists(newUser.getUsername())) return true;
		
		System.out.println(newUser.getUsername() + " doesn't exist in the database, adding..");
		repository.save(newUser);
		return userExists(newUser.getUsername());
	}

	@Override
	public void removeUser(String username) {
		User user = getUserByUsername(username);
		
		if (user != null) {
			repository.delete(user);
		}
	}
	
	@Override
	public void addBucket(User user, Bucket bucket) {
		if (user == null || bucket == null) return;
		
		List<Bucket> buckets = user.getBuckets();
		if (buckets == null) {
			buckets = new ArrayList<Bucket>();
			user.setBuckets(buckets);
		}
		
		buckets.add(bucket);
	}

	@Override
	public boolean removeBucket(User user, Bucket bucket) {
		if (user == null || bucket == null) return false;
		
		List<Bucket> buckets = user.getBuckets();
		buckets.remove(bucket);
		
		return (!buckets.contains(bucket));
	}
	
}
