package com.mie.main.aws.controller;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.amazonaws.auth.profile.internal.securitytoken.STSProfileCredentialsServiceProvider;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.mie.main.aws.S3Uploader;
import com.mie.main.intro.model.Intro;
import com.mie.main.member.model.SingleResult;
import com.mie.main.security.exception.CUserNotFoundException;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class AwsController {
	
	private final S3Uploader s3Uploader;

	@CrossOrigin
	@GetMapping("/aws/board/file/{postId}")
	public List<String> fileInfo(@PathVariable String postId) {
		return s3Uploader.getObjectslistFromFolder(postId);
	}
	@CrossOrigin
	@PostMapping("/aws/board/folder/delete/{postId}")
	public void boardFolderDelete(@PathVariable String postId) {
		String dirName = postId;
		System.out.println(dirName);
		s3Uploader.removeFolder(dirName);
	}
	// 아마존 s3에 게시판 번호로된 폴더 생성후 첨부파일 추가
	@CrossOrigin
	@PostMapping("/aws/board/upload/{postId}")
	public void boardFileUpload(HttpServletRequest request, @PathVariable String postId) throws IOException {
		MultipartHttpServletRequest multi = (MultipartHttpServletRequest) request;
		
		s3Uploader.addDirectory(postId); // 아마존 s3에 폴더 생성
		
		Iterator<String>itr = multi.getFileNames();
		MultipartFile mfile = null;
		
		while(itr.hasNext()) {
			String filename = itr.next();
			mfile = multi.getFile(filename);
			
			String origName;
			origName = new String(mfile.getOriginalFilename());
			if("".equals(origName)) {
				continue;
			}
			
			System.out.println(postId);
			s3Uploader.upload(mfile, postId);
		}
	}
}
