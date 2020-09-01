package com.plainplanner.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plainplanner.entities.Idea;
import com.plainplanner.entities.TextNote;
import com.plainplanner.main.repositories.IdeaRepository;
import com.plainplanner.main.repositories.TextNoteRepository;

@Service
public class TextNoteService implements ITextNoteService {

	@Autowired
	private TextNoteRepository repository;

	@Override
	public TextNote addTextNote(TextNote note) {
		if (note == null) return note;
		
		if (textNoteExists(note)) {
			return getTextNote(note);
		} else {
			return repository.save(note);
		}
	}

	@Override
	public TextNote getTextNote(Long id) {
		return repository.findById(id).get();
	}
	
	/***
	 * Returns the idea from the repository with the same title, description and type. 
	 */
	@Override
	public TextNote getTextNote(TextNote note) {
		if (note == null) return note;
		
		List<TextNote> noteList = repository.findAll();
		int listIndex = noteList.indexOf(note);
		return noteList.get(listIndex);
	}

	@Override
	public boolean removeTextNote(TextNote note) {
		if (note == null) return true;
		
		repository.delete(note);
		return (!textNoteExists(note));
	}

	@Override
	public boolean removeTextNote(Long id) {
		if (id == null) return true;
		
		repository.deleteById(id);
		return (!textNoteExists(id));
	}

	@Override
	public boolean textNoteExists(TextNote note) {
		List<TextNote> noteList = repository.findAll();
		return noteList.contains(note);
	}

	@Override
	public boolean textNoteExists(Long id) {
		return repository.existsById(id);
	}

}
