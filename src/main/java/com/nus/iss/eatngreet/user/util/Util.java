package com.nus.iss.eatngreet.user.util;

import java.util.regex.Pattern;

public class Util {

	public static boolean isStringEmpty(String str) {
		return (str == null || str.trim().length() == 0);
	}

	public static boolean isStringOnlyAlphabets(String str) {
		return ((!str.equals("")) && (str != null) && (str.matches("^[a-zA-Z]*$")));
	}

	public static boolean isValidSGPhoneNo(String phoneNo) {
		return !phoneNo.equals("") && phoneNo != null && phoneNo.matches("^[3689]\\d{7}$");
	}

	public static boolean isValidINPhoneNo(String phoneNo) {
		return !phoneNo.equals("") && phoneNo != null && phoneNo.matches("^[789]\\d{9}$");
	}

	public static boolean isValidEmail(String email) {
		String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\." + "[a-zA-Z0-9_+&*-]+)*@" + "(?:[a-zA-Z0-9-]+\\.)+[a-z"
				+ "A-Z]{2,7}$";
		Pattern pat = Pattern.compile(emailRegex);
		if (email == null)
			return false;

		return pat.matcher(email).matches();
	}

	public static boolean isNumeric(String strNum) {
		try {
			Double.parseDouble(strNum);
		} catch (NumberFormatException | NullPointerException nfe) {
			return false;
		}
		return true;
	}

	public static String appendISDCodeToPhoneNo(String phoneNo) {
		if (phoneNo.matches("^[789]\\d{9}$")) {
			return Constants.INDIA_ISD_CODE + "-" + phoneNo;
		} else if (phoneNo.matches("^[3689]\\d{7}$")) {
			return Constants.SINGAPORE_ISD_CODE + "-" + phoneNo;
		} else {
			return "";
		}
	}

}
