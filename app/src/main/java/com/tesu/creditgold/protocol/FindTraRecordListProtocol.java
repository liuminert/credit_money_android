package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.FindStoreDescByUsridResponse;
import com.tesu.creditgold.response.FindTraRecordListResponse;
import com.tesu.creditgold.response.LoginResponse;

public class FindTraRecordListProtocol extends BaseProtocol<FindTraRecordListResponse> {
	private Gson gson;

	public FindTraRecordListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected FindTraRecordListResponse parseJson(String json) {
		FindTraRecordListResponse findTraRecordListResponse = gson.fromJson(json, FindTraRecordListResponse.class);
		return findTraRecordListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/findTraRecordList.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
