package com.nus.iss.eatngreet.user.restcontroller;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.nus.iss.eatngreet.user.requestdto.UserSignupRequestDTO;
import com.nus.iss.eatngreet.user.responsedto.CommonResponseDto;
import com.nus.iss.eatngreet.user.responsedto.DataResponseDto;
import com.nus.iss.eatngreet.user.service.UserService;
import com.nus.iss.eatngreet.user.util.Constants;
import com.nus.iss.eatngreet.user.util.ResponseUtil;

@RestController
@RequestMapping("/user")
public class UserRestController {

	@Autowired
	UserService userService;

	private static final Logger logger = LoggerFactory.getLogger(UserRestController.class);

	@PostMapping(value = "/signup")
	public CommonResponseDto signup(@RequestBody UserSignupRequestDTO user) {
		logger.info("signup() of UserRestController. Request Obj: {}.", user);
		return userService.userSignup(user);
	}

	@PostMapping(value = "/get-users-info")
	public DataResponseDto getUsersAddressAndName(@RequestBody Map<String, Set<String>> emailIdObj) {
		logger.info("getUsersAddressAndName() of UserRestController. Request Obj: {}.", emailIdObj);
		return userService.getUserAddressAndNameFromEmailIds(emailIdObj);
	}

	@PostMapping(value = "/get-user-info")
	public DataResponseDto getUserInfoFromHeader(HttpServletRequest request) {
		logger.info("getUserInfoFromHeader() of UserRestController.");
		return userService.getUserInfoFromHeader(request);
	}

	@PostMapping(value = "/complete-info")
	public DataResponseDto getCompleteUserInfoFromHeader(HttpServletRequest request) {
		logger.info("getCompleteUserInfoFromHeader() of UserRestController.");
		return userService.getCompleteUserInfoFromHeader(request);
	}

	@GetMapping("/success")
	public CommonResponseDto success() {
		CommonResponseDto response = new CommonResponseDto();
		ResponseUtil.prepareResponse(response, "Successfully logged in.", Constants.STATUS_SUCCESS, "Login success.",
				true);
		return response;
	}

	@GetMapping("/failure")
	public CommonResponseDto failure() {
		CommonResponseDto response = new CommonResponseDto();
		ResponseUtil.prepareResponse(response, "Failed to log in.", Constants.STATUS_FAILURE, "Login failed.", false);
		return response;
	}

	@GetMapping("/health-check")
	public String healthCheck() {
		logger.info("healthCheck() of UserRestController.");
		return "User microservice is up and running. :)";
	}

}
