package com.plainplanner.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Project {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(length=255, nullable=false)
	private String title = "Default Title";

	@OneToMany(fetch=FetchType.EAGER)
	@OrderColumn(name="IDEA_POSITION")
	private List<Idea> ideas;
	
	@OneToMany(fetch=FetchType.EAGER)
	@OrderColumn(name="NOTE_POSITION")
	private List<Note> notes;
	
	@Column(name="DATE_CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated = new Date();
	
	@Column(name="DEADLINE")
	@Temporal(TemporalType.DATE)
	private Date deadline;
	
	public Project() {}
	
	public Project(String title) {
		this();
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Idea> getIdeas() {
		return ideas;
	}

	public void setIdeas(List<Idea> ideas) {
		this.ideas = ideas;
	}
	
	public void addIdea(Idea idea) {
		if (this.ideas == null) {
			this.ideas = new ArrayList<Idea>();
		}
		this.ideas.add(idea);
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}
	
	public void addNote(Note note) {
		if (this.notes == null) {
			this.notes = new ArrayList<Note>();
		}
		this.notes.add(note);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	@Override
	public String toString() {
		return "Project [title=" + title + ", ideas=" + ideas + ", notes=" + notes + ", dateCreated=" + dateCreated
				+ ", deadline=" + deadline + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Project other = (Project) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	
}
