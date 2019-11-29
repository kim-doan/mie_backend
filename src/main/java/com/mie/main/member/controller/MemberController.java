package com.mie.main.member.controller;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.hibernate.loader.plan.build.spi.MetamodelDrivenLoadPlanBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Controller;
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
import org.springframework.web.util.UriComponentsBuilder;

import com.mie.main.security.exception.CUserNotFoundException;
import com.mie.main.member.model.ApiResponseMessage;
import com.mie.main.member.model.AuthenticationRequest;
import com.mie.main.member.model.AuthenticationToken;
import com.mie.main.member.model.CommonResult;
import com.mie.main.member.model.ListResult;
import com.mie.main.member.model.Member;
import com.mie.main.member.model.SingleResult;
import com.mie.main.member.service.ResponseService;
import com.mie.main.repository.MemberRepository;
import com.mie.main.repository.ResourceNotFoundException;
import com.mie.main.security.JwtTokenProvider;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/api")
public class MemberController {
	
	@Autowired
	MemberRepository memberRepository;
	
	@Autowired 
	AuthenticationManager authenticationManager;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private ResponseService responseService;
	
	@Autowired 
	private JwtTokenProvider jwtTokenProvider;
	boolean iderror = false;
	
	// 모든 회원 조회
	@CrossOrigin
	@ApiImplicitParams({
		@ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	})
	@GetMapping("members")
	public List<Member> getAllMember() {
		return memberRepository.findAll();
	}
	
	//회원 단건 조회
	@CrossOrigin
	@GetMapping("members/{id}")
	public SingleResult<Member> getMemberById(@PathVariable(value = "id") Long memberId) {
		return responseService.getSingleResult(memberRepository.findById(memberId).orElseThrow(CUserNotFoundException::new));
	}
	
	//회원 삭제
	@DeleteMapping("members/{id}")
	@CrossOrigin
	public ResponseEntity<ApiResponseMessage> deleteMember(@PathVariable(value = "id") Long memberId){
		Member member = memberRepository.findById(memberId).orElseThrow(() -> new ResourceNotFoundException("Member", "id", memberId));
		
		memberRepository.delete(member);
		ApiResponseMessage message = new ApiResponseMessage("Success", "삭제 완료", "", "", "");
		return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
	}
	
	// 회원가입
	@CrossOrigin
	@PostMapping("members/register")
	public ResponseEntity<ApiResponseMessage> createMember(@Valid @RequestBody Member member) {
		List<Member> allmember = memberRepository.findAll();
		for(int i=0;i<allmember.size();i++) {
			if(allmember.get(i).getUsername().equals(member.getUsername())) {
				iderror = true;
				break;
			}
		}
		if(iderror == true) {
			iderror = false;
			ApiResponseMessage message = new ApiResponseMessage("Fail", "", "", "아이디 중복", "");
			return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
		} else if (iderror == false) {
			
			memberRepository.save(Member.builder()
					.username(member.getUsername())
					.password(passwordEncoder.encode(member.getPassword()))
					.name(member.getName())
					.email(member.getEmail())
					.phone(member.getPhone())
					.type(member.getType())
					.roles(Collections.singletonList("ROLE_USER"))
					.build());
			ApiResponseMessage message = new ApiResponseMessage("Success", "회원가입 성공", "", "", "");
			return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
		}
		return null;
	}
	// 로그인
	@CrossOrigin
	@PostMapping("members/login")
	public ResponseEntity<ApiResponseMessage> login(@Valid @RequestBody AuthenticationRequest member) throws Exception {
		Member dbMember = memberRepository.findByusername(member.getUsername());
		
		if(passwordEncoder.matches(member.getPassword(), dbMember.getPassword())) {
			String token = jwtTokenProvider.createToken(String.valueOf(dbMember.getId()), dbMember.getRoles());
			ApiResponseMessage message = new ApiResponseMessage("Success", "로그인 성공", "", "", token);
			return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
		} else {
			ApiResponseMessage message = new ApiResponseMessage("Fail", "", "", "로그인 실패", "");
			return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
		}
	}
	
	// 유저정보 불러오기
	@CrossOrigin
	@GetMapping("members/profile/{username}")
	public Member getMemberByUsername(@PathVariable(value = "username") String memberUsername) {
		return memberRepository.findByusername(memberUsername);
	}
	
	// 회원 정보 수정 ( 관리자 모드 ) 비밀번호 필요없음
	  @ApiImplicitParams({
          @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	  })
	  @ApiOperation(value = "회원 수정", notes = "회원정보를 수정한다")
	  @PutMapping(value = "members/update")
	  @CrossOrigin
	  public SingleResult<Member> modify(
	          @ApiParam(value = "회원번호", required = true) @Valid @RequestBody Member member) {
		  
		  //회원 비밀번호 없이 수정하기 위해 현재 db 정보 불러오기
		  Member dbMember = memberRepository.findById(member.getId()).orElseThrow(CUserNotFoundException::new);
		  
	      Member vomember = Member.builder()
	              .id(member.getId())
	              .username(member.getUsername())
	              .password(dbMember.getPassword()) // 기존 db에 저장된 정보를 조회후 넣음
	              .email(member.getEmail())
	              .name(member.getName())
	              .phone(member.getPhone())
	              .type(member.getType())
	              .roles(member.getRoles()) // 권한 업데이트
	              .build();
	      return responseService.getSingleResult(memberRepository.save(vomember));
	  }
}
