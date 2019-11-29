package com.mie.main.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mie.main.board.model.Board;

public interface BoardRepository extends JpaRepository<Board, Long>{
	Board findByName(String name);
	Board findByboardId(Long boardId);
	List<Board> findByCategory(int category);
}
