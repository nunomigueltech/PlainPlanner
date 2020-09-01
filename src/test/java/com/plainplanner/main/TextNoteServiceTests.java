package com.plainplanner.main;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.entities.TextNote;
import com.plainplanner.services.TextNoteService;

@SpringBootTest
@EntityScan("com.plainplanner.entities")
public class TextNoteServiceTests {

	@Autowired
	private TextNoteService textNoteService;
	
	@Test
	public void testAddTextNote() {
		TextNote note = new TextNote("Test content");
		note = textNoteService.addTextNote(note);
		
		boolean actual = textNoteService.textNoteExists(note);
		textNoteService.removeTextNote(note);
		Assert.assertTrue(actual);
	}
	
	@Test
	public void testGetIdeaId() {
		TextNote expected = new TextNote("Test content");
		expected = textNoteService.addTextNote(expected);
		
		TextNote actual = textNoteService.getTextNote(expected.getId());
		
		textNoteService.removeTextNote(expected);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testGetIdeaObject() {
		TextNote expected = new TextNote("Test content");
		expected = textNoteService.addTextNote(expected);
		
		TextNote actual = textNoteService.getTextNote(expected);
		
		textNoteService.removeTextNote(expected);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testDeleteIdeaObject() {
		TextNote note = new TextNote("Test content");
		note = textNoteService.addTextNote(note);
		
		boolean expected = true;
		boolean actual = textNoteService.removeTextNote(note);
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testDeleteIdeaId() {
		TextNote note = new TextNote("Test content");
		note = textNoteService.addTextNote(note);
		
		boolean expected = true;
		boolean actual = textNoteService.removeTextNote(note.getId());
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void testIdeaExists() {
		TextNote note = new TextNote("Test content");
		note = textNoteService.addTextNote(note);
		
		boolean expected = true;
		boolean actual = textNoteService.textNoteExists(note);
		
		textNoteService.removeTextNote(note);
		Assert.assertEquals(expected, actual);
	}
}
