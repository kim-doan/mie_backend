package com.mie.main.board.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import com.mie.main.board.model.Board;
import com.mie.main.board.model.ParamsPost;
import com.mie.main.board.model.Post;
import com.mie.main.member.model.Member;
import com.mie.main.repository.BoardRepository;
import com.mie.main.repository.MemberRepository;
import com.mie.main.repository.PostRepository;
import com.mie.main.security.exception.CNotOwnerException;
import com.mie.main.security.exception.CResourceNotExistException;
import com.mie.main.security.exception.CUserNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardService {
	
	private final BoardRepository boardRepository;
	private final PostRepository postRepository;
	private final MemberRepository memberRepository;
	
	// 게시판 이름으로 게시판 조회
	public Board findBoard(String boardName) {
		return Optional.ofNullable(boardRepository.findByName(boardName)).orElseThrow(CResourceNotExistException::new);
	}
	// 게시판 번호로 게시판 조회
	public Board findBoard(Long boardId) {
		return Optional.ofNullable(boardRepository.findByboardId(boardId)).orElseThrow(CResourceNotExistException::new); 
	}
	// 카테고리 번호로 게시판 조회
	public List<Board> findByCategory(int category) {
		return boardRepository.findByCategory(category);
	}
	// 게시판 전체 조회
	public List<Board> findAllBoard() {
		return boardRepository.findAll();
	}
	// 게시판 정보 수정
	public Board updateBoard(Board board) {
		Board voboard = Board.builder()
				.boardId(board.getBoardId())
				.name(board.getName())
				.category(board.getCategory())
				.title(board.getTitle())
				.build();
		
		return boardRepository.save(voboard);
	}
	// 게시판 삭제
	public boolean deleteBoard(String boardName) {
		Board board = findBoard(boardName);
		boardRepository.delete(board);
		return true;
	}
	// 게시판 이름으로 게시판 리스트 조회.
	public List<Post> findPosts(String boardName) {
		return postRepository.findByBoard(findBoard(boardName));
	}
	// 게시판 번호로 게시판 리스트 조회
	public List<Post> findPostsById(Long boardId) {
		return postRepository.findByBoard(findBoard(boardId));
	}
	//게시물 ID로 게시물 단건 조회
	public Post getPost(long postId) {
		return postRepository.findById(postId).orElseThrow(CResourceNotExistException::new);
	}
	// 게시판 생성
	public Board createBoard(String boardName, int category, String title) {
		Board board = new Board(boardName, category, title);
		return boardRepository.save(board);
	}
	//게시물 등록
	public Post writePost(String userName, String boardName, ParamsPost paramsPost) {
		Board board = findBoard(boardName);
		Post post = new Post(memberRepository.findByusername(userName), board, paramsPost.getAuthor(),
				paramsPost.getTitle(), paramsPost.getContent());
		return postRepository.save(post);
	}
	//게시물 등록 -- boardId
	public Post writePost(String userName, Long boardId, ParamsPost paramsPost) {
		Board board = findBoard(boardId);
		Post post = new Post(memberRepository.findByusername(userName), board, paramsPost.getAuthor(),
				paramsPost.getTitle(), paramsPost.getContent());
		return postRepository.save(post);
	}
	// 게시물 수정
	public Post updatePost(long postId, String userName, ParamsPost paramsPost) {
		Post post = getPost(postId);
		Member member = post.getMember();
		
		if(!userName.equals(member.getUsername()))
			throw new CNotOwnerException();
		
		post.setUpdate(paramsPost.getAuthor(), paramsPost.getTitle(), paramsPost.getContent());
		return post;
	}
	
	// 게시물 삭제
	public boolean deletePost(long postId, String userName) {
		Post post = getPost(postId);
		Member member = post.getMember();
		if (!userName.equals(member.getUsername()))
			throw new CNotOwnerException();
		postRepository.delete(post);
		return true;
	}
}
