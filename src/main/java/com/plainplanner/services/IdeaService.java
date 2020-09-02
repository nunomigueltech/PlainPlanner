package com.plainplanner.services;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Project;
import com.plainplanner.entities.User;
import com.plainplanner.main.repositories.BucketRepository;
import com.plainplanner.main.repositories.IdeaRepository;
import com.plainplanner.main.repositories.ProjectRepository;

@Service
public class IdeaService implements IIdeaService {

	@Autowired
	private IdeaRepository repository;
	
	@Autowired
	private BucketRepository bucketRepository;
	
	@Autowired
	private ProjectRepository projectRepository;

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
	@Transactional
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

	@Override
	@Transactional
	public void completeIdea(Idea idea) {
		idea.setComplete(true);
	}

	@Override
	@Transactional
	public void updateTitle(Idea idea, String title) {
		idea.setTitle(title);
	}

	@Override
	@Transactional
	public void updateDescription(Idea idea, String description) {
		idea.setDescription(description);
	}

	@Override
	@Transactional
	public void updateDeadline(Idea idea, Date deadline) {
		if (deadline == null) return;
		
		idea.setDeadline(deadline);
		if (idea.getType().equals("idea")) {
			idea.setType("task");
		}
	}

	@Override
	public Bucket getContainingBucket(Idea idea) {
		List<Bucket> buckets = bucketRepository.findAll();
		List<Bucket> containingBucket = buckets.stream()
											.filter(bucket -> bucket.getIdeas().contains(idea))
											.collect(Collectors.toList());
		
		if (containingBucket == null || containingBucket.isEmpty()) {
			return null;
		} else {
			return containingBucket.get(0);
		}
	}

	@Override
	public Project getContainingProject(Idea idea) {
		List<Project> projects = projectRepository.findAll();
		List<Project> containingProject = projects.stream()
											.filter(project -> project.getIdeas().contains(idea))
											.collect(Collectors.toList());
		
		if (containingProject == null || containingProject.isEmpty()) {
			return null;
		} else {
			return containingProject.get(0);
		}
	}

}
