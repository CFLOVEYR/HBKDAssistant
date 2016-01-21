package com.tofirst.study.hbkdassistant.inteface;

import org.xutils.common.Callback;

public abstract class MyCallBack<ResultType> implements Callback.CommonCallback<ResultType>{
	//可以根据公司的需求进行统一的请求成功的逻辑处理
	@Override
	public abstract void onSuccess(ResultType result);
	//可以根据公司的需求进行统一的请求网络失败的逻辑处理
	@Override
	public abstract void onError(Throwable ex, boolean isOnCallback);

	@Override
	public void onCancelled(CancelledException cex) {

	}

	@Override
	public void onFinished() {
		
	}


}
