package com.mie.main;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mie.main.aws.S3Uploader;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class SampleController {
	
	
	@RequestMapping(value= "/")
	public String index() {
		return "/index.html";
	}
	

}
