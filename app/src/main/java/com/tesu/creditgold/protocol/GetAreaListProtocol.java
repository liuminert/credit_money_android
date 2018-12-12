package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.bean.GetAreaListResponse;
import com.tesu.creditgold.response.GetStoreTypeResponse;

public class GetAreaListProtocol extends BaseProtocol<GetAreaListResponse> {
	private Gson gson;

	public GetAreaListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetAreaListResponse parseJson(String json) {
		GetAreaListResponse getAreaListResponse = gson.fromJson(json, GetAreaListResponse.class);
		return getAreaListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/getAreaList.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
