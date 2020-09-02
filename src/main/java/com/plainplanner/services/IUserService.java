package com.plainplanner.services;

import java.util.List;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Note;
import com.plainplanner.entities.Project;
import com.plainplanner.entities.TextNote;
import com.plainplanner.entities.User;

public interface IUserService {
	User getUserByID(Long id);
	User getUserByUsername(String username);
	boolean userExists(String username);
	User addUser(User user);
	void removeUser(String username);
	void addBucket(User user, Bucket bucket);
	boolean removeBucket(User user, Bucket bucket);
	void addProject(User user, Project project);
	void removeProject(User user, Project project);
	void addTextNote(User user, TextNote note);
	void removeTextNote(User user, TextNote note);
	List<Project> getProjects(User user);
	List<Note> getNotes(User user);
	List<Bucket> getBuckets(User user);
	List<Idea> getIdeas(User user);
	List<Idea> getTasks(User user);
} 
