package com.mie.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mie.main.board.model.Board;
import com.mie.main.board.model.Post;

public interface PostRepository extends JpaRepository<Post, Long>{
	List<Post> findByBoard(Board board);
}
