package com.mie.main.labmember.controller;

import java.io.IOException;
import java.util.List;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mie.main.board.model.Board;
import com.mie.main.board.model.Post;
import com.mie.main.intro.model.CustomProfessor;
import com.mie.main.labmember.model.Activities;
import com.mie.main.labmember.model.Labmember;
import com.mie.main.labmember.model.LabmemberParams;
import com.mie.main.labmember.service.LabmemberService;
import com.mie.main.member.model.CommonResult;
import com.mie.main.member.model.ListResult;
import com.mie.main.member.model.SingleResult;
import com.mie.main.member.service.ResponseService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/api/labmember")
public class LabmemberController {
	
	private final LabmemberService labmemberService;
	private final ResponseService responseService;
	
	//연구실 구성원 정보 추가
	@CrossOrigin
	@PostMapping(value = "/add")
	public SingleResult<Labmember> labmembers(@Valid @RequestBody Labmember labmember) {
		return responseService.getSingleResult(labmemberService.createLabmember(labmember.getName(), labmember.getEnname(), labmember.getAdmission(), labmember.getEmail(),
				labmember.getField(), labmember.getWorkplace(), labmember.getType(), labmember.getProfile_img(), labmember.getOneword()));
	}
	//연구실 구성원 type으로 조회
	@CrossOrigin
	@GetMapping(value ="/info/{type}")
	public ListResult<Labmember> activities(@PathVariable String type) {
		return responseService.getListResult(labmemberService.findLabmember(type));
	}
	//연구실 구성원 전체 리스트 조회
	@CrossOrigin
	@GetMapping(value = "/info")
	public List<Labmember> findlabmembers() {
		return labmemberService.findAllLabmember();
	}
	
	//연구실구성원 삭제
	@CrossOrigin
	@DeleteMapping(value = "/delete/{memberId}")
	public CommonResult deleteLabmember(@PathVariable Long memberId) {
		labmemberService.deleteLabmember(memberId);
		return responseService.getSuccessResult();
	}
	
	// 연구실구성원 수정
	@CrossOrigin
	@PutMapping("/update")
	public SingleResult<Labmember> updateProfessor(@Valid @RequestBody Labmember labmember) {
		return responseService.getSingleResult(labmemberService.updateLabmember(labmember));
	}
	
	// 연구실구성원 프로필 사진 추가
	@CrossOrigin
	@PostMapping("/profile/add")
	public String profileUpload(@RequestParam("profileImg") MultipartFile multipartFile) throws IOException {
		return labmemberService.proflieUpload(multipartFile);
	}
	
	//활동내역 추가
	@CrossOrigin
	@PostMapping(value = "/activities/add/{memberId}")
	public SingleResult<Labmember> activities(@PathVariable Long memberId, @Valid @RequestBody LabmemberParams labParams) {
		return responseService.getSingleResult(labmemberService.writeActivities(memberId, labParams));
	}
	
	//활동내역 조회
	@CrossOrigin
	@GetMapping(value ="/activities/{memberId}")
	public ListResult<Activities> activities(@PathVariable Long memberId) {
		return responseService.getListResult(labmemberService.findActivities(memberId));
	}
}
