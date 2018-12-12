package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.bean.GetAreaListResponse;
import com.tesu.creditgold.response.GetArticleListResponse;

public class GetArticleListProtocol extends BaseProtocol<GetArticleListResponse> {
	private Gson gson;

	public GetArticleListProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetArticleListResponse parseJson(String json) {
		GetArticleListResponse getArticleListResponse = gson.fromJson(json, GetArticleListResponse.class);
		return getArticleListResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/getArticleList.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
