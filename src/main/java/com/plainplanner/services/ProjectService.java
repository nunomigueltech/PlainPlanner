package com.plainplanner.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Note;
import com.plainplanner.entities.Project;
import com.plainplanner.entities.User;
import com.plainplanner.main.repositories.IdeaRepository;
import com.plainplanner.main.repositories.ProjectRepository;

@Service
public class ProjectService implements IProjectService {

	@Autowired
	private ProjectRepository repository;

	@Override
	public Project addProject(Project project) {
		if (project == null) return project;
		
		if (projectExists(project)) {
			return getProject(project);
		} else {
			return repository.save(project);
		}
	}

	@Override
	public Project getProject(Long id) {
		return repository.findById(id).get();
	}
	
	/***
	 * Returns the idea from the repository with the same title, description and type. 
	 */
	@Override
	public Project getProject(Project project) {
		if (project == null) return project;
		
		List<Project> projectList = repository.findAll();
		int listIndex = projectList.indexOf(project);
		return projectList.get(listIndex);
	}

	@Override
	@Transactional
	public boolean removeProject(Project project) {
		if (project == null) return true;
		
		repository.delete(project);
		return (!projectExists(project));
	}

	@Override
	@Transactional
	public boolean removeProject(Long id) {
		if (id == null) return true;
		
		repository.deleteById(id);
		return (!projectExists(id));
	}

	@Override
	public boolean projectExists(Project project) {
		List<Project> projectList = repository.findAll();
		return projectList.contains(project);
	}

	@Override
	public boolean projectExists(Long id) {
		return repository.existsById(id);
	}

	@Override
	@Transactional
	public void addNote(Project project, Note note) {
		if (project == null || note == null) return;
		
		project.getNotes().add(note);
	}

	@Override
	@Transactional
	public void addIdea(Project project, Idea idea) {
		if (project == null || idea == null) return;
		
		project.getIdeas().add(idea);
	}

	@Override
	@Transactional
	public void removeIdea(Project project, Idea idea) {
		if (project == null || idea == null) return;
		
		if (project.getIdeas().contains(idea)) {
			project.getIdeas().remove(idea);
		}
		
	}

	@Override
	@Transactional
	public void updateDeadline(Project project, Date deadline) {
		if (project == null || deadline == null) return;
		
		project.setDeadline(deadline);
	}

	@Override
	@Transactional
	public void updateTitle(Project project, String title) {
		if (project == null || title == null) return;
		
		project.setTitle(title);
	}
}
