package com.plainplanner.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.plainplanner.entities.Idea;
import com.plainplanner.main.repositories.IdeaRepository;

@Service
public class IdeaService implements IIdeaService {

	@Autowired
	private IdeaRepository repository;

	@Override
	public Idea addIdea(Idea idea) {
		if (idea == null) return idea;
		
		if (ideaExists(idea)) {
			return getIdea(idea);
		} else {
			return repository.save(idea);
		}
	}

	@Override
	public Idea getIdea(Long id) {
		return repository.findById(id).get();
	}
	
	/***
	 * Returns the idea from the repository with the same title, description and type. 
	 */
	@Override
	public Idea getIdea(Idea idea) {
		if (idea == null) return idea;
		
		List<Idea> ideaList = repository.findAll();
		int listIndex = ideaList.indexOf(idea);
		return ideaList.get(listIndex);
	}

	@Override
	public boolean removeIdea(Idea idea) {
		if (idea == null) return true;
		
		repository.delete(idea);
		return (!ideaExists(idea));
	}

	@Override
	public boolean removeIdea(Long id) {
		if (id == null) return true;
		
		repository.deleteById(id);
		return (!ideaExists(id));
	}

	@Override
	public boolean ideaExists(Idea idea) {
		List<Idea> ideaList = repository.findAll();
		return ideaList.contains(idea);
	}

	@Override
	public boolean ideaExists(Long id) {
		return repository.existsById(id);
	}

}
