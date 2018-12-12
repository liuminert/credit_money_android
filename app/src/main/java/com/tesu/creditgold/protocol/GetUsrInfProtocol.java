package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.GetUsrInfResponse;
import com.tesu.creditgold.response.LoginResponse;

public class GetUsrInfProtocol extends BaseProtocol<GetUsrInfResponse> {
	private Gson gson;

	public GetUsrInfProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetUsrInfResponse parseJson(String json) {
		GetUsrInfResponse getUsrInfResponse = gson.fromJson(json, GetUsrInfResponse.class);
		return getUsrInfResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "tsfkxt/user/getUsrInf.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
