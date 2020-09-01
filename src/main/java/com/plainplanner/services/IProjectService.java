package com.plainplanner.services;

import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Project;

public interface IProjectService {
	Project addProject(Project project);
	Project getProject(Long id);
	Project getProject(Project project);
	boolean removeProject(Project project);
	boolean removeProject(Long id);
	boolean projectExists(Project project);
	boolean projectExists(Long id);
}
