package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.CreateUsrResponse;
import com.tesu.creditgold.response.UserWithdrawalsResponse;

public class UserWithdrawalsProtocol extends BaseProtocol<UserWithdrawalsResponse> {
	private Gson gson;

	public UserWithdrawalsProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected UserWithdrawalsResponse parseJson(String json) {
		UserWithdrawalsResponse userWithdrawalsResponse = gson.fromJson(json, UserWithdrawalsResponse.class);
		return userWithdrawalsResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/userWithdrawals.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
