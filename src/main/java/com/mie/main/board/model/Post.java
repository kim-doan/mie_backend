package com.mie.main.board.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;

import com.mie.main.date.CommonDateEntity;
import com.mie.main.member.model.Member;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Post extends CommonDateEntity {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@GeneratedValue(strategy=GenerationType.SEQUENCE, generator = "post_seq")
	@SequenceGenerator(sequenceName = "SEQUENCE_POST", allocationSize = 1, name="post_seq")
	private Long postId;
	@Column(nullable = false, length = 255)
	private String author;
	@Column(nullable = false, length = 255)
	private String title;
	@Column(length = 2000)
	private String content;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "board_id")
	private Board board; // 게시글 - 게시판의 관계  N:1
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "msrl")
	private Member member; // 게시글 - 회원의 관계 N:1
	
	//JOIN 테이블이 Json 결과에 표시되지 않도록 처리.
	protected Board getBoard() {
		return board;
	}
	
	//생성자
	public Post(Member member, Board board, String author, String title, String content) {
		this.member = member;
		this.board = board;
		this.author = author;
		this.title = title;
		this.content = content;
	}
	
	//수정시 데이터 처리
	public Post setUpdate(String author, String title, String content) {
		this.author = author;
		this.title = title;
		this.content = content;
		return this;
	}

	@Override
	public String toString() {
		return "Post [postId=" + postId + ", author=" + author + ", title=" + title + ", content=" + content
				+ ", board=" + board.toString() + ", member=" + member.toString() + "]";
	}
}
