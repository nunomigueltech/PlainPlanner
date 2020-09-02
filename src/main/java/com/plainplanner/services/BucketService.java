package com.plainplanner.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plainplanner.entities.Bucket;
import com.plainplanner.entities.Idea;
import com.plainplanner.main.repositories.BucketRepository;

@Service
@Transactional
public class BucketService implements IBucketService {
	
	@Autowired
	private BucketRepository repository;

	@Override
	public Bucket addBucket(Bucket bucket) {
		if (bucket == null) return null;
		
		Bucket existingBucket = getBucket(bucket.getName());
		if (existingBucket == null) {
			return repository.save(bucket);
		} else {
			return existingBucket;
		}
	}
	
	@Override
	public boolean removeBucket(Long id) {
		if (id == null) return false;
		
		repository.deleteById(id);
		return (!containsBucket(id));
	}

	@Override
	public List<Bucket> getBuckets() {
		return repository.findAll();
	}

	@Override
	public Bucket getBucket(Long bucketId) {
		if (bucketId == null) return null;
		
		return repository.findById(bucketId).get();
	}

	@Override
	public Bucket getBucket(String bucketName) {
		return repository.findFirstByName(bucketName);
	}

	@Override
	public boolean containsBucket(Long id) {
		return repository.existsById(id);
	}

	@Override
	public boolean containsBucket(Bucket bucket) {
		return (repository.findFirstByName(bucket.getName()) != null);
	}

	@Override
	@Transactional
	public void addIdea(Bucket bucket, Idea idea) {
		if (bucket == null || idea == null) return;
		
		bucket.getIdeas().add(idea);
	}

	@Override
	@Transactional
	public void removeIdea(Bucket bucket, Idea idea) {
		if (bucket == null || idea == null) return;
		
		if (bucket.getIdeas().contains(idea)) {
			bucket.getIdeas().remove(idea);
		}
	}
}
