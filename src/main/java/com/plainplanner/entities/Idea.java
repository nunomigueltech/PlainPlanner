package com.plainplanner.entities;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class Idea {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(length=255, nullable=false)
	private String title = "Default Title";
	
	@Column(length=255)
	private String description = "Default description.";
	
	private boolean complete = false;
	
	@Column(nullable=false)
	private String type = "idea";
	
	@Column(name="DATE_CREATED")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated  = new Date();
	
	@Column(name="DEADLINE")
	@Temporal(TemporalType.TIMESTAMP)
	private Date deadline;
	
	protected Idea() {}

	public Idea(String name, String type) {
		this.title = name;
		this.type = type;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String name) {
		this.title = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
	public Long getId() {
		return id;
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
	
	public String getDeadlineString() {
		Date date = deadline;
		if (deadline == null) {
			date = new Date();
		}
		return (date.getMonth() + 1) + "/" + date.getDate() + "/" + (date.getYear() + 1900);
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	public boolean isTask() {
		return type.equals("task");
	}

	@Override
	public String toString() {
		return "Idea [id=" + id + ", name=" + title + ", description=" + description + ", type=" + type + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		Idea other = (Idea) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}
	
	
}
