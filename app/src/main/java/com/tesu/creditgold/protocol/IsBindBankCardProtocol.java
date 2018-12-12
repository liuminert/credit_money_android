package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.IsBindBankCardResponse;
import com.tesu.creditgold.response.LoginResponse;

public class IsBindBankCardProtocol extends BaseProtocol<IsBindBankCardResponse> {
	private Gson gson;

	public IsBindBankCardProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected IsBindBankCardResponse parseJson(String json) {
		IsBindBankCardResponse isBindBankCardResponse = gson.fromJson(json, IsBindBankCardResponse.class);
		return isBindBankCardResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/is_bind_bank_card.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
