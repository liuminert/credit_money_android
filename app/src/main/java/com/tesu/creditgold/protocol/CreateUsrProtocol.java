package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.CreateUsrResponse;
import com.tesu.creditgold.response.LoginResponse;

public class CreateUsrProtocol extends BaseProtocol<CreateUsrResponse> {
	private Gson gson;

	public CreateUsrProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected CreateUsrResponse parseJson(String json) {
		CreateUsrResponse createUsrResponse = gson.fromJson(json, CreateUsrResponse.class);
		return createUsrResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/createUsr.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
