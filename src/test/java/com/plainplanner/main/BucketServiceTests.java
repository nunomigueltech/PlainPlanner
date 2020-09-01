package com.plainplanner.main;

import java.util.List;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.entities.User;
import com.plainplanner.services.BucketService;
import com.plainplanner.services.IdeaService;
import com.plainplanner.services.UserService;

@SpringBootTest
@EntityScan("com.plainplanner.entities")
public class BucketServiceTests {

	@Autowired
	private BucketService bucketService;

	@Test
	public void testAddBucket() {
		Bucket newBucket = new Bucket("bucketName");
		newBucket = bucketService.addBucket(newBucket);
		
		boolean actual = bucketService.containsBucket(newBucket);
		bucketService.removeBucket(newBucket.getId());
		Assert.assertTrue(actual);
	}
	
	@Test
	public void testRemoveBucket() {
		Bucket newBucket = new Bucket("bucketName");
		newBucket = bucketService.addBucket(newBucket);
		bucketService.removeBucket(newBucket.getId());
		
		boolean actual = bucketService.containsBucket(newBucket);
		Assert.assertFalse(actual);
	}
	
	@Test
	public void testGetBuckets() {
		Bucket newBucket = new Bucket("bucketName");
		newBucket = bucketService.addBucket(newBucket);
		
		List<Bucket> buckets = bucketService.getBuckets();
		boolean expected = true;
		boolean actual = buckets.size() > 0;
		bucketService.removeBucket(newBucket.getId());
		
		Assert.assertEquals(actual, expected);
	}
	
	@Test
	public void testGetBucketId() {
		Bucket expected = new Bucket("bucketName");
		expected = bucketService.addBucket(expected);
		
		Bucket actual = bucketService.getBucket(expected.getId());
		Assert.assertEquals(actual, expected);
	}
	
	@Test
	public void testGetBucketName() {
		Bucket expected = new Bucket("bucketName");
		expected = bucketService.addBucket(expected);
		
		Bucket actual = bucketService.getBucket(expected.getName());
		Assert.assertEquals(actual, expected);
	}
	
	@Test
	public void testContainsBucket() {
		Bucket newBucket = new Bucket("bucketName");
		newBucket = bucketService.addBucket(newBucket);
		
		boolean expected = true;
		boolean actual = bucketService.containsBucket(newBucket);
		Assert.assertEquals(actual, expected);
	}
} 
