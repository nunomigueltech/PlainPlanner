package com.plainplanner.dto;

import java.util.Date;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.plainplanner.validation.ValidDate;

@ValidDate
public class NewItemDTO {
	
	private Long id;
	
	@NotNull
	private String title;

	@NotNull
	@NotEmpty
	private String itemType;
	
	private String content;
	
	private Date date;
	
	private boolean completed;
	
	private Long projectID;
	private Long bucketID;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getItemType() {
		return itemType;
	}

	public void setItemType(String itemType) {
		this.itemType = itemType;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public boolean isCompleted() {
		return completed;
	}

	public void setCompleted(boolean completed) {
		this.completed = completed;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getProjectID() {
		return projectID;
	}

	public void setProjectID(Long projectID) {
		this.projectID = projectID;
	}

	public Long getBucketID() {
		return bucketID;
	}

	public void setBucketID(Long bucketID) {
		this.bucketID = bucketID;
	}
	
	
}
