package com.plainplanner.entities;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
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
	
	@Column(name="DATE_CREATED")
	@Temporal(TemporalType.DATE)
	private Date dateCreated;
	
	@OneToMany(cascade=CascadeType.PERSIST)
	@OrderColumn(name="POSITION")
	private List<Bucket> buckets;
	
	@OneToMany(cascade=CascadeType.PERSIST)
	private List<Note> notes;
	
	@ElementCollection
	@MapKeyCollection(name="setting")
	@Column(name="setting_value")
	
	
	public User() {
		dateCreated = new Date();
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

	@Override
	public String toString() {
		return "User [id=" + id + ", username=" + username + "]";
	}

}
