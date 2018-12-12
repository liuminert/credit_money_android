package com.tesu.creditgold.protocol;

import com.google.gson.Gson;
import com.tesu.creditgold.base.BaseProtocol;
import com.tesu.creditgold.response.AppSendMsgResponse;
import com.tesu.creditgold.response.SetWithdrawPwdResponse;

public class SetWithdrawPwdProtocol extends BaseProtocol<SetWithdrawPwdResponse> {
	private Gson gson;

	public SetWithdrawPwdProtocol() {
		gson = new Gson();
	}


	// 1. 封装请求参数
	// 2. 处理请求返回结果
	@Override
	protected SetWithdrawPwdResponse parseJson(String json) {
		SetWithdrawPwdResponse setWithdrawPwdResponse = gson.fromJson(json, SetWithdrawPwdResponse.class);
		return setWithdrawPwdResponse;
	}

	@Override
	public String getApiFun() {
		// TODO Auto-generated method stub
		return "credit_money_background/user/setWithdrawPwd.do";
	}

	@Override
	public String getReqMethod() {
		// TODO Auto-generated method stub
		return "post";
	}
}
