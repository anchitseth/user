package com.nus.iss.eatngreet.user.responsedto;

import java.util.Map;

@SuppressWarnings("rawtypes")
public class DataResponseDto extends CommonResponseDto {

	private Map data;

	public Map getData() {
		return data;
	}

	public void setData(Map data) {
		this.data = data;
	}

}
