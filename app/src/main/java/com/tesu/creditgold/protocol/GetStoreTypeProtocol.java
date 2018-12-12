package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.bean.GetUserStatusResponse;
import com.tesu.creditgold.response.GetStoreTypeResponse;
import com.tesu.creditgold.response.GetUsrInfResponse;

public class GetStoreTypeProtocol extends BaseProtocol<GetStoreTypeResponse> {
	private Gson gson;

	public GetStoreTypeProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetStoreTypeResponse parseJson(String json) {
		GetStoreTypeResponse getStoreTypeResponse = gson.fromJson(json, GetStoreTypeResponse.class);
		return getStoreTypeResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/getStoreType.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
