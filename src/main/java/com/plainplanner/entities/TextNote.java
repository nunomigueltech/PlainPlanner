package com.plainplanner.entities;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class TextNote extends Note<String> {
	private String content = "Note is empty.";
	
	public TextNote() {}
	
	public TextNote(String content) {
		this.content = content;
	}

	@Override
	public String getContent() {
		return content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
