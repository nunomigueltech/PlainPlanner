package com.plainplanner.services;

import java.util.Date;
import java.util.List;

import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Note;
import com.plainplanner.entities.Project;
import com.plainplanner.entities.User;

public interface IProjectService {
	Project addProject(Project project);
	Project getProject(Long id);
	Project getProject(Project project);
	boolean removeProject(Project project);
	boolean removeProject(Long id);
	boolean projectExists(Project project);
	boolean projectExists(Long id);
	void addNote(Project project, Note note);
	void addIdea(Project project, Idea idea);
	void removeIdea(Project project, Idea idea);
	void updateTitle(Project project, String title);
	void updateDeadline(Project project, Date deadline);
}
