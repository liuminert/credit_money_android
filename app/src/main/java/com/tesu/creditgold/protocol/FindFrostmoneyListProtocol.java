package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.FindFrostmoneyListResponse;
import com.tesu.creditgold.response.FindStoreDescByUsridResponse;

public class FindFrostmoneyListProtocol extends BaseProtocol<FindFrostmoneyListResponse> {
	private Gson gson;

	public FindFrostmoneyListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected FindFrostmoneyListResponse parseJson(String json) {
		FindFrostmoneyListResponse findFrostmoneyListResponse = gson.fromJson(json, FindFrostmoneyListResponse.class);
		return findFrostmoneyListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/findFrostmoneyList.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
