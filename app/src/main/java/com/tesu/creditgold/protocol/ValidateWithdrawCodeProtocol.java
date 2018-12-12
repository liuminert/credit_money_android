package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.LoginResponse;
import com.tesu.creditgold.response.ValidateWithdrawCodeResponse;

public class ValidateWithdrawCodeProtocol extends BaseProtocol<ValidateWithdrawCodeResponse> {
	private Gson gson;

	public ValidateWithdrawCodeProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected ValidateWithdrawCodeResponse parseJson(String json) {
		ValidateWithdrawCodeResponse validateWithdrawCodeResponse = gson.fromJson(json, ValidateWithdrawCodeResponse.class);
		return validateWithdrawCodeResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/validateWithdrawCode.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
