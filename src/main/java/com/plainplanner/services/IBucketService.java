package com.plainplanner.services;

import java.util.List;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.entities.User;

public interface IBucketService {
	Bucket addBucket(Bucket bucket);
	boolean removeBucket(Long bucketId);
	Bucket getBucket(Long bucketId);
	Bucket getBucket(String bucketName);
	List<Bucket> getBuckets();
	boolean containsBucket(Long id);
	boolean containsBucket(Bucket bucket);
	void addIdea(Bucket bucket, Idea idea);
	void removeIdea(Bucket bucket, Idea idea);
}