package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.UpdateOrderInfoResponse;
import com.tesu.creditgold.response.UpdateStoreInfoResponse;

public class UpdateOrderInfoProtocol extends BaseProtocol<UpdateOrderInfoResponse> {
	private Gson gson;

	public UpdateOrderInfoProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected UpdateOrderInfoResponse parseJson(String json) {
		UpdateOrderInfoResponse updateOrderInfoResponse = gson.fromJson(json, UpdateOrderInfoResponse.class);
		return updateOrderInfoResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "tsfkxt/user/updateOrderInfo.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
