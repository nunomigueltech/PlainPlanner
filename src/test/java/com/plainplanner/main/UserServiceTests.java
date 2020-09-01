package com.plainplanner.main;


import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.User;
import com.plainplanner.services.UserService;

@SpringBootTest
@EntityScan("com.plainplanner.entities")
public class UserServiceTests {
	
	@Autowired
	private UserService userService;
	
	@Test
	public void testGetUserByID() {
		Long testID = (long) -1;
		User actual = userService.getUserByID(testID);
		
		Assert.assertNull(actual);
	}
	
	@Test
	public void testGetUserByUsername() {
		String testUsername = "/";
		User actual = userService.getUserByUsername(testUsername);
		
		Assert.assertNull(actual);
	}
	
	@Test
	public void testUserExists() {
		String testUsername = "/";
		boolean actual = userService.userExists(testUsername);
		
		Assert.assertFalse(actual);
	}
	
	@Test
	public void testAddUser() {
		User test = null;
		boolean actual = userService.addUser(test);
		
		Assert.assertFalse(actual);
	}
	
	@Test
	public void testRemoveUser() {
		String testUsername = "testuser1000";
		User testUser = new User(testUsername, "pass");
		userService.addUser(testUser);
		userService.removeUser(testUsername);
		
		boolean actual = userService.userExists(testUsername);
		Assert.assertFalse(actual);
	}
	
	@Test 
	public void testAddBucket() {
		String testUsername = "testname";
		User user = new User(testUsername, "pass1");
		Bucket bucket = new Bucket("bucketName");
		
		userService.addUser(user);
		userService.addBucket(user, bucket);
		int expectedBuckets = 1;
		int actualBuckets = user.getBuckets().size();
		
		userService.removeUser(testUsername);
		
		Assert.assertEquals(expectedBuckets, actualBuckets);
	}
	
	@Test
	public void testRemoveBucket() {
		String testUsername = "testname";
		User user = new User(testUsername, "pass1");
		Bucket bucket = new Bucket("bucketName");
		
		userService.addUser(user);
		userService.addBucket(user, bucket);

		boolean actual = userService.removeBucket(user, bucket);

		userService.removeUser(testUsername);
		
		Assert.assertTrue(actual);
	}
}
