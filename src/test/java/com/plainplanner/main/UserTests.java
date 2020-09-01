package com.plainplanner.main;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.plainplanner.entities.User;

import junit.framework.Assert;

@SpringBootTest
public class UserTests {
	@Test
	public void testUserConstructor() {
		User expected = new User();
		expected.setUsername("test");
		expected.setPasswordHash("testhash");
		expected.setRole("User");
		
		User actual = new User("test", "testhash");
		Assert.assertEquals(expected.getUsername(), actual.getUsername());
		Assert.assertEquals(expected.getPasswordHash(), actual.getPasswordHash());
		Assert.assertEquals(expected.getRole(), actual.getRole());
	}
}
