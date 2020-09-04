package com.plainplanner.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Note;
import com.plainplanner.entities.Project;
import com.plainplanner.entities.TextNote;
import com.plainplanner.entities.User;
import com.plainplanner.main.repositories.BucketRepository;
import com.plainplanner.main.repositories.UserRepository;

@Service
@Transactional
public class UserService implements IUserService {
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private BucketService bucketService;

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
	@Transactional
	public User addUser(User newUser) {
		if (newUser == null) return newUser;
		if (userExists(newUser.getUsername())) return getUserByUsername(newUser.getUsername());
		
		System.out.println(newUser.getUsername() + " doesn't exist in the database, adding..");
		newUser = repository.save(newUser);
		
		// Every user comes with a default bucket that cannot be deleted
		Bucket defaultBucket = new Bucket("Default Bucket");
		defaultBucket.disallowDeletion();
		//defaultBucket = bucketService.addBucket(defaultBucket);
		addBucket(newUser, defaultBucket);
		newUser.setDefaultBucket(defaultBucket);
	
		return newUser;
	}

	@Override
	public void removeUser(String username) {
		User user = getUserByUsername(username);
		
		if (user != null) {
			repository.delete(user);
		}
	}
	
	@Override
	@Transactional
	public void addBucket(User user, Bucket bucket) {
		if (user == null || bucket == null) return;
		
		List<Bucket> buckets = user.getBuckets();
		buckets.add(bucket);
	}

	@Override
	@Transactional
	public boolean removeBucket(User user, Bucket bucket) {
		if (user == null || bucket == null) return false;
		
		List<Bucket> buckets = user.getBuckets();
		buckets.remove(bucket);
		
		return (!buckets.contains(bucket));
	}

	@Override
	@Transactional
	public void addProject(User user, Project project) {
		if (user == null || project == null) return;
		
		user.getProjects().add(project);
	}

	@Override
	@Transactional
	public void removeProject(User user, Project project) {
		if (user == null || project == null) return;
		
		user.getProjects().remove(project);
	}

	@Override
	@Transactional
	public void addTextNote(User user, TextNote note) {
		if (user == null || note == null) return;
		
		user.getNotes().add(note);
	}

	@Override
	@Transactional
	public void removeTextNote(User user, TextNote note) {
		if (user == null || note == null) return;
		
		user.getNotes().remove(note);
	}
	
	@Override
	public boolean hasProject(User user, Long id) {
		if (user == null || id == null) return false;
		
		List<Project> projects = getProjects(user);
		
		for (Project project : projects) {
			if (project.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Project> getProjects(User user) {
		if (user == null) return new ArrayList<Project>();
		
		return user.getProjects();
	}
	
	@Override
	public boolean hasNote(User user, Long id) {
		if (user == null || id == null) return false;
		
		List<Note> notes = getNotes(user);
		
		for (Note note : notes) {
			if (note.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Note> getNotes(User user) {
		if (user == null) return new ArrayList<Note>();
		
		return user.getNotes();
	}
	
	@Override
	public boolean hasBucket(User user, Long id) {
		if (user == null || user == null) return false;
		
		List<Bucket> buckets = getBuckets(user);
		
		for (Bucket bucket : buckets) {
			if (bucket.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Bucket> getBuckets(User user) {
		if (user == null) return new ArrayList<Bucket>();
		
		return user.getBuckets();
	}
	
	@Override
	public boolean hasIdea(User user, Long id) {
		if (user == null || id == null) return false;
		
		List<Idea> ideas = getIdeas(user);
		
		for (Idea idea : ideas) {
			if (idea.getId().equals(id)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public List<Idea> getIdeas(User user) {
		if (user == null) return new ArrayList<Idea>();
		
		List<Idea> ideas = user.getBuckets().stream()
								.flatMap(bucket -> bucket.getIdeas().stream())
								.collect(Collectors.toList());
		
		return ideas;
	}

	@Override
	public List<Idea> getTasks(User user) {
		if (user == null) return new ArrayList<Idea>();
		
		List<Idea> ideas = getIdeas(user);
		List<Idea> tasks = ideas.stream()
								.filter(idea -> idea.isTask())
								.collect(Collectors.toList());
		
		return tasks;
	}
	
	@Override
	public List<Idea> getUpcomingTasks(User user) {
		if (user == null) return new ArrayList<Idea>();
		
		List<Idea> ideaList = user.getBuckets().stream()
									.flatMap(bucket -> bucket.getIdeas().stream())
									.collect(Collectors.toList());
		Date today = new Date();
		today.setHours(0);
		today.setMinutes(0);
		today.setSeconds(0);
		return ideaList.stream()
				.filter(idea -> idea.getDeadline() != null && ((idea.getDeadline().compareTo(today) >= 0) || sameDay(idea.getDeadline(), today)))
				.sorted((x, y) -> x.getDeadline().compareTo(y.getDeadline()))
				.limit(10)
				.collect(Collectors.toList());
		
	}
	
	private boolean sameDay(Date date1, Date date2) {
		if (date1 == null || date2 == null) return false; 
		
		return date1.getDay() == date2.getDay() && date1.getMonth() == date2.getMonth() &&
				date1.getYear() == date2.getYear();
	}

	@Override
	@Transactional
	public void updateSetting(User user, String settingKey, Boolean setting) {
		if (user == null || settingKey == null || setting == null) return;
		
		user.getSettings().put(settingKey, setting);
	}
}
