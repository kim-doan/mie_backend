package com.mie.main.board.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mie.main.board.model.Board;
import com.mie.main.board.model.ParamsPost;
import com.mie.main.board.model.Post;
import com.mie.main.board.service.BoardService;
import com.mie.main.member.model.CommonResult;
import com.mie.main.member.model.ListResult;
import com.mie.main.member.model.SingleResult;
import com.mie.main.member.service.ResponseService;
import com.mie.main.repository.BoardRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/board")
public class BoardController {
	
	private final BoardService boardService;
	private final ResponseService responseService;
	
	//게시판 정보 조회
	@CrossOrigin
	@GetMapping(value = "/info/{boardName}")
	public SingleResult<Board> boardInfo(@PathVariable String boardName) {
		return responseService.getSingleResult(boardService.findBoard(boardName));
	}
	//게시판 리스트 조회
	@CrossOrigin
	@GetMapping(value = "/info")
	public List<Board> boards() {
		return boardService.findAllBoard();
	}
	//게시판 리스트 카테고리 조회
	@CrossOrigin
	@GetMapping(value = "/list/{categoryNo}")
	public List<Board> boards2(@PathVariable int categoryNo) {
		return boardService.findByCategory(categoryNo);
	}
	//게시판 생성
	@CrossOrigin
	@PostMapping(value = "/add")
	public SingleResult<Board> board(@Valid @RequestBody Board board) {
		return responseService.getSingleResult(boardService.createBoard(board.getName(), board.getCategory(), board.getTitle()));
	}
	// 게시판 수정
	@CrossOrigin
	@PutMapping(value = "/update")
	public SingleResult<Board> updateBoard(@Valid @RequestBody Board board) {
		return responseService.getSingleResult(boardService.updateBoard(board));
	}
	//게시판 삭제
	@CrossOrigin
	@DeleteMapping(value = "/delete/{boardName}")
	public CommonResult deleteBoard(@PathVariable String boardName) {
		boardService.deleteBoard(boardName);
		return responseService.getSuccessResult();
	}
	//게시글 리스트 조회 게시판 이름
	@CrossOrigin
	@GetMapping(value ="/info/{boardName}/posts")
	public ListResult<Post> posts(@PathVariable String boardName) {
		return responseService.getListResult(boardService.findPosts(boardName));
	}
	//게시글 리스트 조회 게시판 번호
	@CrossOrigin
	@GetMapping(value ="/info/id/{boardId}/posts")
	public ListResult<Post> posts(@PathVariable Long boardId) {
		return responseService.getListResult(boardService.findPostsById(boardId));
	}
	//게시글 작성
	@CrossOrigin
	@PostMapping(value = "/add/{boardName}")
	public SingleResult<Post> post(@PathVariable String boardName, @Valid @RequestBody ParamsPost post) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		return responseService.getSingleResult(boardService.writePost(userName, boardName, post));
	}
	
	//게시글 상세보기
	@CrossOrigin
	@GetMapping(value = "/post/{postId}")
	public SingleResult<Post> post(@PathVariable long postId) {
		return responseService.getSingleResult(boardService.getPost(postId));
	}
	
	//게시글 수정
	@CrossOrigin
	@PutMapping(value = "/post/{postId}")
	public SingleResult<Post> post(@PathVariable long postId, @Valid @RequestBody ParamsPost post) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		return responseService.getSingleResult(boardService.updatePost(postId, userName, post));
	}
	
	//게시글 삭제
	@CrossOrigin
	@DeleteMapping(value = "/post/{postId}")
	public CommonResult deletePost(@PathVariable long postId) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		boardService.deletePost(postId, userName);
		return responseService.getSuccessResult();
	}
}
