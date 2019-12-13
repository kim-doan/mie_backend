package com.mie.main.intro.controller;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mie.main.aws.S3Uploader;
import com.mie.main.intro.model.Activity;
import com.mie.main.intro.model.Activitys;
import com.mie.main.intro.model.CustomProfessor;
import com.mie.main.intro.model.History;
import com.mie.main.intro.model.Historys;
import com.mie.main.intro.model.Intro;
import com.mie.main.intro.model.Professor;
import com.mie.main.member.model.ApiResponseMessage;
import com.mie.main.member.model.ListResult;
import com.mie.main.member.model.Member;
import com.mie.main.member.model.SingleResult;
import com.mie.main.member.service.ResponseService;
import com.mie.main.repository.ActivityRepository;
import com.mie.main.repository.CustomProfessorRepository;
import com.mie.main.repository.HistoryRepository;
import com.mie.main.repository.IntroRepository;
import com.mie.main.repository.ProfessorRepository;
import com.mie.main.repository.ResourceNotFoundException;
import com.mie.main.security.exception.CUserNotFoundException;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class IntroController {
	
	@Autowired
	IntroRepository introRepository;
	
	@Autowired
	ProfessorRepository professorRepository;
	
	@Autowired
	HistoryRepository historyRepository;
	
	@Autowired
	ActivityRepository activityRepository;
	
	@Autowired
	CustomProfessorRepository customProfessorRepository;
	
	@Autowired
	ResponseService responseService;
	
	private final S3Uploader s3Uploader;
	
	//연구실 소개 단건 조회
	@CrossOrigin
	@GetMapping("/intro/profile/{language}")
	public SingleResult<Intro> getIntroById(@PathVariable(value = "language") String language) {
		return responseService.getSingleResult(introRepository.findById(language).orElseThrow(CUserNotFoundException::new));
	}
	
	//연구실 소개 수정
	  @ApiImplicitParams({
          @ApiImplicitParam(name = "X-AUTH-TOKEN", value = "로그인 성공 후 access_token", required = true, dataType = "String", paramType = "header")
	  })	
	  @ApiOperation(value = "소개 수정", notes = "연구실 소개를 수정한다")
	  @PutMapping(value = "/intro/update")
	  @CrossOrigin
	  public SingleResult<Intro> modify(
	          @ApiParam(value = "소개정보", required = true) @Valid @RequestBody Intro intro) {
		  
	      Intro voIntro = Intro.builder()
	              .language(intro.getLanguage())
	              .intro(intro.getIntro())
	              .build();
	      return responseService.getSingleResult(introRepository.save(voIntro));
	  }
	  
	  //연혁 수정
		@CrossOrigin
		@PutMapping("/intro/history/update/{langid}")
		public ResponseEntity<ApiResponseMessage> updateHistory(@Valid @RequestBody Historys historys, @PathVariable(value = "langid") int langId) {
			//db에 저장되어있는 교수님 정보 가져오기
			Professor dbprofessor = professorRepository.findByid(langId);
			//db에 저장되어있는 교수님 원래 정보 넣고 새로운 연혁 수정
			professorRepository.save(new Professor(langId, dbprofessor.getName() , dbprofessor.getPosition(), dbprofessor.getTel(), dbprofessor.getEmail(), dbprofessor.getHobby(), dbprofessor.getFamily()
					, new History(historys.getId(), historys.getHistory(), historys.getName())));
			ApiResponseMessage message = new ApiResponseMessage("Success", "연혁 변경 성공", "", "", "");
			return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
		}
	  
	  //연혁 추가
		@CrossOrigin
		@PostMapping("/intro/history/add/{langid}")
		public ResponseEntity<ApiResponseMessage> createHistory(@Valid @RequestBody Historys historys, @PathVariable(value = "langid") int langId) {
			//db에 저장되어있는 교수님 정보 가져오기
			Professor dbprofessor = professorRepository.findByid(langId);
			//db에 저장되어있는 교수님 원래 정보 넣고 새로운 연혁 추가
			professorRepository.save(new Professor(langId, dbprofessor.getName() , dbprofessor.getPosition(), dbprofessor.getTel(), dbprofessor.getEmail(), dbprofessor.getHobby(), dbprofessor.getFamily()
					, new History(historys.getHistory(), historys.getName())));
			
			ApiResponseMessage message = new ApiResponseMessage("Success", "연혁 추가 성공", "", "", "");
			return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
		}
		
		//연혁 단건 조회
		@CrossOrigin
		@GetMapping("/intro/history/{langid}")
		public ListResult<Historys> getHistoryByProfessorId(@PathVariable(value = "langid") int langId) {
			return responseService.getListResult(historyRepository.findByprofessorId(langId));
		}
		
		//연혁 삭제
		@DeleteMapping("/intro/history/{id}")
		@CrossOrigin
		public ResponseEntity<ApiResponseMessage> deleteHistory(@PathVariable(value = "id") int historyId){
			Historys historys = historyRepository.findById(historyId).orElseThrow(() -> new ResourceNotFoundException("Historys", "id", historyId));
			
			historyRepository.delete(historys);
			ApiResponseMessage message = new ApiResponseMessage("Success", "삭제 완료", "", "", "");
			return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
		}
		
		  //외부활동 수정
			@CrossOrigin
			@PutMapping("/intro/activity/update/{langid}")
			public ResponseEntity<ApiResponseMessage> updateActivity(@Valid @RequestBody Activitys activitys, @PathVariable(value = "langid") int langId) {
				//db에 저장되어있는 교수님 정보 가져오기
				Professor dbprofessor = professorRepository.findByid(langId);
				//db에 저장되어있는 교수님 원래 정보 넣고 새로운 외부활동 수정
				professorRepository.save(new Professor(langId, dbprofessor.getName() , dbprofessor.getPosition(), dbprofessor.getTel(), dbprofessor.getEmail(), dbprofessor.getHobby(), dbprofessor.getFamily()
						, new Activity(activitys.getId(), activitys.getActivity(), activitys.getName())));
				ApiResponseMessage message = new ApiResponseMessage("Success", "외부활동 변경 성공", "", "", "");
				return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
			}
		  
		  //외부활동 추가
			@CrossOrigin
			@PostMapping("/intro/activity/add/{langid}")
			public ResponseEntity<ApiResponseMessage> createActivity(@Valid @RequestBody Activitys activitys, @PathVariable(value = "langid") int langId) {
				//db에 저장되어있는 교수님 정보 가져오기
				Professor dbprofessor = professorRepository.findByid(langId);
				//db에 저장되어있는 교수님 원래 정보 넣고 새로운 연혁 추가
				professorRepository.save(new Professor(langId, dbprofessor.getName() , dbprofessor.getPosition(), dbprofessor.getTel(), dbprofessor.getEmail(), dbprofessor.getHobby(), dbprofessor.getFamily()
						, new Activity(activitys.getActivity(), activitys.getName())));
				
				ApiResponseMessage message = new ApiResponseMessage("Success", "외부활동 추가 성공", "", "", "");
				return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
			}
			
			//외부활동 조회
			@CrossOrigin
			@GetMapping("/intro/activity/{langid}")
			public ListResult<Activitys> getActivityByProfessorId(@PathVariable(value = "langid") int langId) {
				return responseService.getListResult(activityRepository.findByprofessorId(langId));
			}
			
			//외부활동 삭제
			@DeleteMapping("/intro/activity/{id}")
			@CrossOrigin
			public ResponseEntity<ApiResponseMessage> deleteActivity(@PathVariable(value = "id") int activityId){
				Activitys activitys = activityRepository.findById(activityId).orElseThrow(() -> new ResourceNotFoundException("Activitys", "id", activityId));
				
				activityRepository.delete(activitys);
				ApiResponseMessage message = new ApiResponseMessage("Success", "삭제 완료", "", "", "");
				return new ResponseEntity<ApiResponseMessage>(message, HttpStatus.OK);
			}
		// 교수님 프로필 조회
		@CrossOrigin
		@GetMapping("/intro/professor/{langid}")
		public SingleResult<CustomProfessor> getProfessorById(@PathVariable(value = "langid") int langId) {
			return responseService.getSingleResult(customProfessorRepository.findById(langId).orElseThrow(CUserNotFoundException::new));
		}
		
		// 교수님 프로필 수정
		@CrossOrigin
		@PutMapping("/intro/professor/update/{langid}")
		public SingleResult<CustomProfessor> updateProfessor(@Valid @RequestBody CustomProfessor professor, @PathVariable(value = "langid") int langId) {
			CustomProfessor customProfessor = CustomProfessor.builder()
					.id(langId)
					.name(professor.getName())
					.position(professor.getPosition())
					.tel(professor.getTel())
					.email(professor.getEmail())
					.hobby(professor.getHobby())
					.family(professor.getFamily())
					.build();
			return responseService.getSingleResult(customProfessorRepository.save(customProfessor));
		}
		// 교수님 프로필 사진 업로드
		@CrossOrigin
		@PostMapping("/intro/professor/update/image/{langid}")
		@ResponseBody
		public SingleResult<CustomProfessor> upload(@RequestParam("profileImg") MultipartFile multipartFile, @PathVariable(value = "langid") int langId) throws IOException {
			String imgDir = s3Uploader.upload(multipartFile, "static");
			CustomProfessor dbprofessor = customProfessorRepository.findByid(langId);
			CustomProfessor customProfessor = CustomProfessor.builder()
					.id(langId)
					.name(dbprofessor.getName())
					.position(dbprofessor.getPosition())
					.tel(dbprofessor.getTel())
					.email(dbprofessor.getEmail())
					.hobby(dbprofessor.getHobby())
					.family(dbprofessor.getFamily())
					.profile_img(imgDir)
					.build();
			return responseService.getSingleResult(customProfessorRepository.save(customProfessor));
		}
}
