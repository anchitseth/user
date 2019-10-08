package com.nus.iss.eatngreet.user.responsedto;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@SuppressWarnings("rawtypes")
@JsonInclude(Include.NON_NULL)
public class CommonResponseDto {

	private String status;
	private String message;
	private String info;
	private Boolean success;
	private Map data;

	public CommonResponseDto(String status, String message, String info, Boolean success) {
		this.status = status;
		this.message = message;
		this.info = info;
		this.success = success;
	}

}
