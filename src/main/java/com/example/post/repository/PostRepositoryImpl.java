package com.example.post.repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.example.post.model.Post;

@Repository
public class PostRepositoryImpl implements PostRepository{

	private static Map<Long, Post> posts = new HashMap<>();
	private static long sequence = 0;
	
	@Override
	public void savePost(Post post) {
		post.setId(++sequence);
		posts.put(post.getId(), post);
		
	}

	@Override
	public List<Post> findAllPosts() {
		return new ArrayList<>(posts.values());
		// 복사본을 넘기는 방식
	}

	@Override
	public Post findPostById(Long postId) {
		return posts.get(postId); // 원본을 넘기는 방식 *원본을 건드리는 위험한 방식
	}

	@Override
	public void updatePost(Post updatePost) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removePost(Long postId) {
		posts.remove(postId);
		
	}

}
