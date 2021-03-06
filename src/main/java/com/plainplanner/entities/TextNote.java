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
	private String content = "This note is empty.";
	
	public TextNote() {}
	
	public String getContent() {
		return content;
	}
	
	public TextNote(String content) {
		this.content = content;
	}
	
	public void setContent(String content) {
		this.content = content;
	}
}
