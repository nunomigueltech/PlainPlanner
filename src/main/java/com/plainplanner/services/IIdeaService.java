package com.plainplanner.services;

import com.plainplanner.entities.Idea;

public interface IIdeaService {
	Idea addIdea(Idea idea);
	Idea getIdea(Long id);
	Idea getIdea(Idea idea);
	boolean removeIdea(Idea idea);
	boolean removeIdea(Long id);
	boolean ideaExists(Idea idea);
	boolean ideaExists(Long id);
}
