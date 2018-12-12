package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.AppSendMsgResponse;
import com.tesu.creditgold.response.LoginResponse;

public class AppSendMsgProtocol extends BaseProtocol<AppSendMsgResponse> {
	private Gson gson;

	public AppSendMsgProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected AppSendMsgResponse parseJson(String json) {
		AppSendMsgResponse appSendMsgResponse = gson.fromJson(json, AppSendMsgResponse.class);
		return appSendMsgResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/appSendMsg.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
