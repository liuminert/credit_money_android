package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.LoginResponse;
import com.tesu.creditgold.response.UpdateStoreInfoResponse;

public class UpdateStoreInfoProtocol extends BaseProtocol<UpdateStoreInfoResponse> {
	private Gson gson;

	public UpdateStoreInfoProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected UpdateStoreInfoResponse parseJson(String json) {
		UpdateStoreInfoResponse updateStoreInfoResponse = gson.fromJson(json, UpdateStoreInfoResponse.class);
		return updateStoreInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/updateStoreInfo.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
