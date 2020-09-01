package com.plainplanner.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Project;
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
	public boolean removeProject(Project project) {
		if (project == null) return true;
		
		repository.delete(project);
		return (!projectExists(project));
	}

	@Override
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

}
