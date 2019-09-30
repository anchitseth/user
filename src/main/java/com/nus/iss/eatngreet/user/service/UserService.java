package com.nus.iss.eatngreet.user.service;

import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import com.nus.iss.eatngreet.user.requestdto.UserSignupRequestDTO;
import com.nus.iss.eatngreet.user.responsedto.CommonResponseDTO;
import com.nus.iss.eatngreet.user.responsedto.DataResponseDTO;

public interface UserService {

	public CommonResponseDTO userSignup(UserSignupRequestDTO user);

	public DataResponseDTO getUserAddressAndNameFromEmailIds(Map<String, Set<String>> emailIdObj);

	public DataResponseDTO getUserInfoFromHeader(HttpServletRequest request);

	public DataResponseDTO getCompleteUserInfoFromHeader(HttpServletRequest request);

}
