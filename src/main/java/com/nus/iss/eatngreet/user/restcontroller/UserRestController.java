package com.nus.iss.eatngreet.user.restcontroller;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nus.iss.eatngreet.user.requestdto.UserSignupRequestDTO;
import com.nus.iss.eatngreet.user.responsedto.CommonResponseDTO;
import com.nus.iss.eatngreet.user.responsedto.DataResponseDTO;
import com.nus.iss.eatngreet.user.service.UserService;
import com.nus.iss.eatngreet.user.util.ResponseUtil;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserRestController {

	@Autowired
	UserService userService;

	@PostMapping(value = "/signup")
	public CommonResponseDTO signup(@RequestBody UserSignupRequestDTO user) throws Exception {
		return userService.userSignup(user);
	}
	
	@PostMapping(value = "/get-users-info")
	public DataResponseDTO getUsersAddressAndName(@RequestBody Map<String, Set<String>> emailIdObj) throws Exception {
		return userService.getUserAddressAndNameFromEmailIds(emailIdObj);
	}
	
	@PostMapping(value = "/get-user-info")
	public DataResponseDTO getUserInfoFromHeader(HttpServletRequest request) throws Exception {
		return userService.getUserInfoFromHeader(request);
	}
	
	@PostMapping(value = "/complete-info")
	public DataResponseDTO getCompleteUserInfoFromHeader(HttpServletRequest request) throws Exception {
		return userService.getCompleteUserInfoFromHeader(request);
	}

	@GetMapping("/success")
	public CommonResponseDTO success() {
		CommonResponseDTO response = new CommonResponseDTO();
		ResponseUtil.prepareResponse(response, "Successfully logged in.", "SUCCESS", "Login success.", true);
		return response;
	}

	@GetMapping("/failure")
	public CommonResponseDTO failure() {
		CommonResponseDTO response = new CommonResponseDTO();
		ResponseUtil.prepareResponse(response, "Failed to log in.", "FAILURE", "Login failed.", false);
		return response;
	}
	
	@GetMapping("/health-check")
	public String healthCheck() {
		return "User microservice is up and running. :)";
	}

}

