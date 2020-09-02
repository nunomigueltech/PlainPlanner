package com.plainplanner.services;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.entities.Project;
import com.plainplanner.entities.User;

public interface IIdeaService {
	Idea addIdea(Idea idea);
	Idea getIdea(Long id);
	Idea getIdea(Idea idea);
	boolean removeIdea(Idea idea);
	boolean removeIdea(Long id);
	boolean ideaExists(Idea idea);
	boolean ideaExists(Long id);
	List<Idea> getUpcomingTasks(User user);
	List<Idea> getUserIdeas(User user);
	void completeIdea(Idea idea);
	void updateTitle(Idea idea, String title);
	void updateDescription(Idea idea, String description);
	void updateDeadline(Idea idea, Date deadline);
	Bucket getContainingBucket(Idea idea);
	Project getContainingProject(Idea idea);
}
