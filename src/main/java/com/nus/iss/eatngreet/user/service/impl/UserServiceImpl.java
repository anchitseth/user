package com.nus.iss.eatngreet.user.service.impl;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.nus.iss.eatngreet.user.dao.entity.AddressEntity;
import com.nus.iss.eatngreet.user.dao.entity.RoleEntity;
import com.nus.iss.eatngreet.user.dao.entity.UserEntity;
import com.nus.iss.eatngreet.user.dao.repository.RoleRepository;
import com.nus.iss.eatngreet.user.dao.repository.UserRepository;
import com.nus.iss.eatngreet.user.requestdto.UserSignupRequestDTO;
import com.nus.iss.eatngreet.user.responsedto.CommonResponseDTO;
import com.nus.iss.eatngreet.user.responsedto.DataResponseDTO;
import com.nus.iss.eatngreet.user.service.UserService;
import com.nus.iss.eatngreet.user.util.ResponseUtil;
import com.nus.iss.eatngreet.user.util.Util;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	UserRepository userRepository;

	@Autowired
	RoleRepository roleRepository;

	@Value("${eatngreet.paymentmicroservice.url.domain}")
	String paymentMicroserviceDomain;

	@Value("${eatngreet.paymentmicroservice.url.port}")
	String paymentMicroservicePort;

	@Override
	public CommonResponseDTO userSignup(UserSignupRequestDTO user) {
		CommonResponseDTO response = new CommonResponseDTO();
		if (!Util.isValidEmail(user.getEmailId())) {
			ResponseUtil.prepareResponse(response, "Invalid email id.", "FAILURE", "Incorrect Email-id.", false);
		} else if (!Util.isValidSGPhoneNo(user.getPhoneNo())) {
			ResponseUtil.prepareResponse(response, "Invalid mobile number.", "FAILURE", "Incorrect phone no.", false);
		} else if (userRepository.findByEmailId(user.getEmailId()).isPresent()) {
			ResponseUtil.prepareResponse(response, "Email-id already registered.", "FAILURE",
					"Email-id already registered.", false);
		} else if (userRepository.findByPhoneNo(user.getPhoneNo()).isPresent()) {
			ResponseUtil.prepareResponse(response, "Phone number already registered.", "FAILURE",
					"Mobile Number already registered.", false);
		} else if (!user.getPassword().equals(user.getConfirmPassword())) {
			ResponseUtil.prepareResponse(response, "Password and confirm password must be the same.", "FAILURE",
					"Password and confirm password are different.", false);
		} else {
			AddressEntity newUserAddress = new AddressEntity();
			BeanUtils.copyProperties(user.getAddress(), newUserAddress);
			if (Util.isStringEmpty(newUserAddress.getUnitNo()) || Util.isStringEmpty(newUserAddress.getFloorNo())
					|| Util.isStringEmpty(newUserAddress.getBuildingName())
					|| Util.isStringEmpty(newUserAddress.getPincode())
					|| Util.isStringEmpty(newUserAddress.getBlockNo())) {
				ResponseUtil.prepareResponse(response,
						"Level no., unit no., block no., building name and pincode are mandatory fields.", "FAILURE",
						"Mandatory fields missing..", false);
			} else {
				newUserAddress.setIsActive(true);
				newUserAddress.setIsDeleted(false);
				Set<AddressEntity> addresses = new HashSet<AddressEntity>();
				addresses.add(newUserAddress);
				PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
				UserEntity newUser = new UserEntity();
				BeanUtils.copyProperties(user, newUser);
				newUser.setPassword(encoder.encode(user.getPassword()));
				newUser.setAddresses(addresses);
				Set<RoleEntity> roles = new HashSet<RoleEntity>();
				RoleEntity userRole = roleRepository.findByRole("USER").get();
				roles.add(userRole);
				newUser.setRoles(roles);
				newUser.setIsActive(true);
				newUser.setIsDeleted(false);
				userRepository.save(newUser);
				// call to payment ms for setting up balance
				String signupBonusUrl = paymentMicroserviceDomain + ":" + paymentMicroservicePort
						+ "/eatngreet/paymentms/pay/signup-bonus";
				RestTemplate restTemplate = new RestTemplate();
				HttpHeaders headers = new HttpHeaders();
				headers.setContentType(MediaType.APPLICATION_JSON);
				Map<String, String> signUpReqObj = new HashMap<String, String>();
				signUpReqObj.put("emailId", newUser.getEmailId());
				Map<String, Object> paymentResponse = restTemplate.postForObject(signupBonusUrl, signUpReqObj,
						Map.class);
				if ((Boolean) paymentResponse.get("success")) {
					// log that bonus credited successfully
				} else {
					// log that bonus was not credited
				}
				ResponseUtil.prepareResponse(response, "Successfully registered.", "SUCCESS",
						"Successfully registered.", true);
			}
		}
		return response;
	}

	@Override
	public DataResponseDTO getUserAddressAndNameFromEmailIds(Map<String, Set<String>> emailIdObj) {
		DataResponseDTO response = new DataResponseDTO();
		HashMap<Object, Object> data = new HashMap<>();
		Set<String> emailIds = emailIdObj.get("emailIds");
		List<String> emailIdList = new ArrayList<String>();
		emailIdList.addAll(emailIds);
		List<UserEntity> users = userRepository.findByEmailIds(emailIdList);
		ResponseUtil.prepareResponse(response, "Successfully fetched User details from email id.", "SUCCESS",
				"Successfully fetched User details from email id.", true);
		Map<String, Object> infoMap = new HashMap<String, Object>();
		for (UserEntity user : users) {
			Map<String, Object> userInfoMap = new HashMap<String, Object>();
			userInfoMap.put("firstName", user.getFirstName());
			userInfoMap.put("lastName", user.getLastName());
			Set<AddressEntity> addresses = user.getAddresses();
			List<Map> formattedAddresses = new ArrayList<>();
			for (AddressEntity address : addresses) {
				Map<String, String> addressInfo = new HashMap<String, String>();
				addressInfo.put("blockNo", address.getBlockNo());
				addressInfo.put("floorNo", address.getFloorNo());
				addressInfo.put("unitNo", address.getUnitNo());
				addressInfo.put("buildingName", address.getBuildingName());
				addressInfo.put("streetName", address.getStreetName());
				addressInfo.put("pincode", address.getPincode());
				addressInfo.put("latitude", address.getLatitude());
				addressInfo.put("longitude", address.getLongitude());
				formattedAddresses.add(addressInfo);
			}
			userInfoMap.put("addresses", formattedAddresses);
			infoMap.put(user.getEmailId(), userInfoMap);
		}
		data.put("userInfo", infoMap);
		response.setData(data);
		return response;
	}

	@Override
	public DataResponseDTO getUserInfoFromHeader(HttpServletRequest request) {
		DataResponseDTO response = new DataResponseDTO();
		try {
			String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();
			String decryptedEmail = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
			if (Util.isValidEmail(decryptedEmail)) {
				Optional<UserEntity> userObj = userRepository.findByEmailId(decryptedEmail);
				if (userObj.isPresent()) {
					Map<String, String> data = new HashMap<>();
					data.put("firstName", userObj.get().getFirstName());
					data.put("lastName", userObj.get().getLastName());
					response.setData(data);
					ResponseUtil.prepareResponse(response, "Successfully fetched user info.", "SUCCESS",
							"Successfully fetched user info.", true);
				} else {
					ResponseUtil.prepareResponse(response, "Please try again.", "FAILURE",
							"No record found for email id: " + decryptedEmail, false);
				}
			} else {
				ResponseUtil.prepareResponse(response, "Unable to fetch valid email id from headers.", "FAILURE",
						"Unable to fetch valid email id from headers.", false);
			}
		} catch (Exception e) {
			ResponseUtil.prepareResponse(response, "Some problem occurred, please try again.", "FAILURE",
					"Exception occurred while trying fetch user info from headers. Exception msg: " + e.getMessage(),
					false);
		}
		return response;

	}

	@Override
	public DataResponseDTO getCompleteUserInfoFromHeader(HttpServletRequest request) {
		DataResponseDTO response = new DataResponseDTO();
		try {
			String authToken = request.getHeader("Authorization").substring("Basic".length()).trim();
			String decryptedEmail = new String(Base64.getDecoder().decode(authToken)).split(":")[0];
			if (Util.isValidEmail(decryptedEmail)) {
				Optional<UserEntity> userObj = userRepository.findByEmailId(decryptedEmail);
				if (userObj.isPresent()) {
					Map<String, Object> data = new HashMap<>();
					data.put("info", userObj.get());
					response.setData(data);
					ResponseUtil.prepareResponse(response, "Successfully fetched user info.", "SUCCESS",
							"Successfully fetched user info.", true);
				} else {
					ResponseUtil.prepareResponse(response, "Please try again.", "FAILURE",
							"No record found for email id: " + decryptedEmail, false);
				}
			} else {
				ResponseUtil.prepareResponse(response, "Unable to fetch valid email id from headers.", "FAILURE",
						"Unable to fetch valid email id from headers.", false);
			}
		} catch (Exception e) {
			ResponseUtil.prepareResponse(response, "Some problem occurred, please try again.", "FAILURE",
					"Exception occurred while trying fetch user info from headers. Exception msg: " + e.getMessage(),
					false);
		}
		return response;
	}

}
