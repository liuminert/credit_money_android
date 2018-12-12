package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.FindStoreDescByUsridResponse;
import com.tesu.creditgold.response.GetUsrInfResponse;

public class FindStoreDescByUsridProtocol extends BaseProtocol<FindStoreDescByUsridResponse> {
	private Gson gson;

	public FindStoreDescByUsridProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected FindStoreDescByUsridResponse parseJson(String json) {
		FindStoreDescByUsridResponse findStoreDescByUsridResponse = gson.fromJson(json, FindStoreDescByUsridResponse.class);
		return findStoreDescByUsridResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/findStoreDescByUsrid.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
