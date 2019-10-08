package com.nus.iss.eatngreet.user.service;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.nus.iss.eatngreet.user.requestdto.UserSignupRequestDTO;
import com.nus.iss.eatngreet.user.responsedto.CommonResponseDto;
import com.nus.iss.eatngreet.user.responsedto.DataResponseDto;

public interface UserService {

	public CommonResponseDto userSignup(UserSignupRequestDTO user);

	public DataResponseDto getUserAddressAndNameFromEmailIds(Map<String, Set<String>> emailIdObj);

	public DataResponseDto getUserInfoFromHeader(HttpServletRequest request);

	public DataResponseDto getCompleteUserInfoFromHeader(HttpServletRequest request);

}
