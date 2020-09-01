package com.plainplanner.main;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Project;
import com.plainplanner.services.IdeaService;
import com.plainplanner.services.ProjectService;

import junit.framework.Assert;

@SpringBootTest
@EntityScan("com.plainplanner.entities")
public class ProjectServiceTests {

	@Autowired
	private ProjectService projectService;
	
	@Test
	public void testAddProject() {
		Project project = new Project("Test Project 1");
		project = projectService.addProject(project);
		
		boolean expected = true;
		boolean actual = projectService.projectExists(project);
		
		projectService.removeProject(project);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetProjectId() {
		Project expected = new Project("Test Project 2");
		expected = projectService.addProject(expected);
		
		Project actual = projectService.getProject(expected.getId());
		
		projectService.removeProject(expected);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetProjectObject() {
		Project expected = new Project("Test Project 3");
		expected = projectService.addProject(expected);
		
		Project actual = projectService.getProject(expected);
		
		projectService.removeProject(expected);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testDeleteProjectObject() {
		Project project = new Project("Test Project 4");
		project = projectService.addProject(project);
		
		boolean expected = true;
		boolean actual = projectService.removeProject(project);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testDeleteProjectId() {
		Project project = new Project("Test Project 5");
		project = projectService.addProject(project);
		
		boolean expected = true;
		boolean actual = projectService.removeProject(project.getId());
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testProjectExists() {
		Project project = new Project("Test Project 6");
		project = projectService.addProject(project);
		
		boolean expected = true;
		boolean actual = projectService.projectExists(project);
		
		projectService.removeProject(project);
		Assert.assertEquals(expected, actual);
	}
}
