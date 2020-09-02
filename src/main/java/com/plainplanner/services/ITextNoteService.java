package com.plainplanner.services;

import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Project;
import com.plainplanner.entities.TextNote;

public interface ITextNoteService {
	TextNote addTextNote(TextNote note);
	TextNote getTextNote(Long id);
	TextNote getTextNote(TextNote note);
	boolean removeTextNote(TextNote note);
	boolean removeTextNote(Long id);
	boolean textNoteExists(TextNote note);
	boolean textNoteExists(Long id);
	Project getContainingProject(TextNote note);
}
