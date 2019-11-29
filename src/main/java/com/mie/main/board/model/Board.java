package com.mie.main.board.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;

import com.mie.main.date.CommonDateEntity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@NoArgsConstructor
public class Board extends CommonDateEntity{
	@Id
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "board_seq")
	@SequenceGenerator(sequenceName = "SEQUENCE_BOARD", allocationSize = 1, name="board_seq")
	private Long boardId;
	@Column(nullable = false, length = 255)
	private String name;
	private int category;
	private String title;
	@Override
	public String toString() {
		return "Board [boardId=" + boardId + ", name=" + name + "]";
	}
	public Board(Long boardId, String name, int category, String title) {
		this.boardId = boardId;
		this.name = name;
		this.category = category;
		this.title = title;
	}
	public Board(String name, int category, String title) {
		this.name = name;
		this.category = category;
		this.title = title;
	}
}
