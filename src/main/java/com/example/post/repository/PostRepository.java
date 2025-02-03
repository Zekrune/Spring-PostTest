package com.example.post.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.post.model.posts.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	
}
