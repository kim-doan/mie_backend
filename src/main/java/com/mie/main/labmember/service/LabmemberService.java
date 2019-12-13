package com.mie.main.labmember.service;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mie.main.aws.S3Uploader;
import com.mie.main.board.model.Board;
import com.mie.main.board.model.ParamsPost;
import com.mie.main.board.model.Post;
import com.mie.main.intro.model.CustomProfessor;
import com.mie.main.labmember.model.Activities;
import com.mie.main.labmember.model.Labmember;
import com.mie.main.labmember.model.LabmemberParams;
import com.mie.main.repository.ActivitiesRepository;
import com.mie.main.repository.BoardRepository;
import com.mie.main.repository.LabmemberRepository;
import com.mie.main.repository.MemberRepository;
import com.mie.main.repository.PostRepository;
import com.mie.main.security.exception.CResourceNotExistException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class LabmemberService {

	private final LabmemberRepository labmemberRepository;
	private final ActivitiesRepository activitiesRepository;
	private final S3Uploader s3Uploader;
	
	// 연구실구성원 번호로 구성원 찾기
	public Labmember findLabmember(Long memberId) {
		return Optional.ofNullable(labmemberRepository.findByid(memberId)).orElseThrow(CResourceNotExistException::new); 
	}
	
	// type으로 구성원 찾기
	public List<Labmember> findLabmember(String type) {
		if(type.equals("phd")) {
			type = "박사과정";
		} else if (type.equals("phm")) {
			type = "석사과정";
		} else if (type.equals("master")) {
			type = "(졸업생)석사";
		} else if (type.equals("doctor")) {
			type = "(졸업생)박사";
		}
		return labmemberRepository.findBytype(type);
	}
	
	// 연구실 프로필사진 업로드
	public String proflieUpload(MultipartFile multipartFile) throws IOException {
		String imgDir = s3Uploader.upload(multipartFile, "profile");
		return imgDir;
	}
	
	// 연구실구성원 삭제
	public boolean deleteLabmember(Long memberId) {
		Labmember labmember = findLabmember(memberId);
		labmemberRepository.delete(labmember);
		return true;
	}
	
	// 연구실구성원 수정
	public Labmember updateLabmember(Labmember labmember) {
		Labmember dblabmember = Labmember.builder()
				.id(labmember.getId())
				.name(labmember.getName())
				.enname(labmember.getEnname())
				.admission(labmember.getAdmission())
				.email(labmember.getEmail())
				.field(labmember.getField())
				.workplace(labmember.getWorkplace())
				.type(labmember.getType())
				.profile_img(labmember.getProfile_img())
				.oneword(labmember.getOneword())
				.build();
		return labmemberRepository.save(dblabmember);
	}
	
	// 연구실구성원 전체 조회
	public List<Labmember> findAllLabmember() {
		return labmemberRepository.findAll();
	}
	
	// 연구실구성원 생성
	public Labmember createLabmember(String name, String enname, String admission, String email, String field, String workplace, String type, String profile_img, String oneword) {
		Labmember labmember = new Labmember(name, enname, admission, email, field, workplace, type, profile_img, oneword);
		System.out.println(labmember.toString());
		return labmemberRepository.save(labmember);
	}
	
	// 활동내역 등록
	public Labmember writeActivities(Long memberId, LabmemberParams labParams) {
		Labmember labmember = findLabmember(memberId);

		return labmemberRepository.save(new Labmember(labmember.getId(),labmember.getName(), labmember.getEnname(), labmember.getAdmission(),
				labmember.getEmail(), labmember.getField(), labmember.getWorkplace(), labmember.getType(), labmember.getProfile_img(), labmember.getOneword(),
				new Activities(labParams.getTitle(), labParams.getType())));
	}
	
	// 연구실구성원 번호로 활동내역 찾기
	public List<Activities> findActivities(Long memberId) {
		return activitiesRepository.findByLabmember(findLabmember(memberId));
	}
}
