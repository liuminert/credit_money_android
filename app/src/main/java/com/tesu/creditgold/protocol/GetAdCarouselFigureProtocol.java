package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.GetAdCarouselFigureResponse;
import com.tesu.creditgold.response.LoginResponse;

public class GetAdCarouselFigureProtocol extends BaseProtocol<GetAdCarouselFigureResponse> {
	private Gson gson;

	public GetAdCarouselFigureProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected GetAdCarouselFigureResponse parseJson(String json) {
		GetAdCarouselFigureResponse getAdCarouselFigureResponse = gson.fromJson(json, GetAdCarouselFigureResponse.class);
		return getAdCarouselFigureResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/getAdCarouselFigure.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
