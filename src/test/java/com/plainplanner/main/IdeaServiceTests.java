package com.plainplanner.main;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import com.plainplanner.entities.Idea;
import com.plainplanner.services.IdeaService;

import junit.framework.Assert;

@SpringBootTest
@EntityScan("com.plainplanner.entities")
public class IdeaServiceTests {

	@Autowired
	private IdeaService ideaService;
	
	@Test
	public void testAddIdea() {
		Idea idea = new Idea("test991", "idea");
		idea = ideaService.addIdea(idea);
		
		boolean expected = true;
		boolean actual = ideaService.ideaExists(idea);
		
		ideaService.removeIdea(idea);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetIdeaId() {
		Idea expected = new Idea("test992", "idea");
		expected = ideaService.addIdea(expected);
		
		Idea actual = ideaService.getIdea(expected.getId());
		
		ideaService.removeIdea(expected);
		System.out.println("[testGetIdeaId] expected = " + expected);
		System.out.println("[testGetIdeaId] actual = " + actual);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetIdeaObject() {
		Idea expected = new Idea("test993", "idea");
		expected = ideaService.addIdea(expected);
		
		Idea actual = ideaService.getIdea(expected);
		
		ideaService.removeIdea(expected);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testDeleteIdeaObject() {
		Idea idea = new Idea("test994", "idea");
		idea = ideaService.addIdea(idea);
		
		boolean expected = true;
		boolean actual = ideaService.removeIdea(idea);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testDeleteIdeaId() {
		Idea idea = new Idea("test995", "idea");
		idea = ideaService.addIdea(idea);
		
		boolean expected = true;
		boolean actual = ideaService.removeIdea(idea.getId());
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testIdeaExists() {
		Idea idea = new Idea("test999", "idea");
		idea = ideaService.addIdea(idea);
		
		boolean expected = true;
		boolean actual = ideaService.ideaExists(idea);
		
		ideaService.removeIdea(idea);
		Assert.assertEquals(expected, actual);
	}
}
