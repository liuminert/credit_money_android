package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.GetRepaymentListResponse;
import com.tesu.creditgold.response.LoginResponse;

public class GetRepaymentListProtocol extends BaseProtocol<GetRepaymentListResponse> {
	private Gson gson;

	public GetRepaymentListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetRepaymentListResponse parseJson(String json) {
		GetRepaymentListResponse getRepaymentListResponse = gson.fromJson(json, GetRepaymentListResponse.class);
		return getRepaymentListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/getRepaymentList.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
