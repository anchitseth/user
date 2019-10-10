package com.nus.iss.eatngreet.user.util;

import java.util.Base64;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

public class Util {

	public static boolean isStringEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}

	public static boolean isValidSGPhoneNo(String phoneNo) {
		return !phoneNo.equals("") && phoneNo != null && phoneNo.matches("^[3689]\\d{7}$");
	}

	public static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;

		return pat.matcher(email).matches();
	}

	public static String getDecryptedEmail(HttpServletRequest request) {
		String authToken = request.getHeader(Constants.AUTHORIZATION_HEADER_NAME).substring("Basic".length()).trim();
		return new String(Base64.getDecoder().decode(authToken)).split(":")[0];
	}

	private Util() {

	}

}
