package com.plainplanner.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.OrderColumn;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(length=32, nullable=false)
	private String username;
	
	@Column(length=60, nullable=false)
	private String passwordHash;
	
	@Column(nullable=false)
	private String role;
	
	@OneToOne
	private Bucket defaultBucket;
	
	@Column(name="DATE_CREATED")
	@Temporal(TemporalType.DATE)
	private Date dateCreated = new Date();
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	@OrderColumn(name="POSITION")
	private List<Project> projects = new ArrayList<>();
	
	@OneToMany(fetch=FetchType.EAGER, cascade=CascadeType.PERSIST)
	@OrderColumn(name="POSITION")
	private List<Bucket> buckets = new ArrayList<>();
	
	@OneToMany(cascade=CascadeType.PERSIST)
	private List<Note> notes = new ArrayList<>();
	
	@ElementCollection
	@MapKeyColumn(name="setting")
	@Column(name="setting_value")
	@CollectionTable(name="user_settings", joinColumns=@JoinColumn(name="user_id"))
	Map<String, Boolean> settings = new HashMap<>();
	
	public User() {
		settings.put("Cat Mode", false);
		settings.put("Show completed tasks and ideas", true);
	}
	
	public User(String username, String passHash) {
		this();
		this.username = username;
		this.passwordHash = passHash;
		this.role = "User";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passHash) {
		this.passwordHash = passHash;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public List<Bucket> getBuckets() {
		return buckets;
	}

	public void setBuckets(List<Bucket> buckets) {
		this.buckets = buckets;
	}
	
	public Bucket getDefaultBucket() {
		return defaultBucket;
	}
	
	public void setDefaultBucket(Bucket defaultBucket) {
		this.defaultBucket = defaultBucket;
	}
		
	public List<Project> getProjects() {
		return projects;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<Note> getNotes() {
		return notes;
	}

	public void setNotes(List<Note> notes) {
		this.notes = notes;
	}

	public Map<String, Boolean> getSettings() {
		return settings;
	}
	
	public void updateSetting(String key, Boolean value) {
		settings.put(key, value);
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + "]";
	}

}
