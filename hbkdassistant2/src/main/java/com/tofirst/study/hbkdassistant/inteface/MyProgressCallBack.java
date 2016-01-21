package com.tofirst.study.hbkdassistant.inteface;

import org.xutils.common.Callback;

public abstract class MyProgressCallBack<ResultType> implements Callback.ProgressCallback<ResultType>{
	//根据公司业务需求，处理相应业务逻辑
	@Override
	public abstract void onSuccess(ResultType result);
	//根据公司业务需求，处理相应业务逻辑
	@Override
	public abstract void onError(Throwable ex, boolean isOnCallback);

	@Override
	public abstract  void onLoading(long total, long current, boolean isDownloading);
	@Override
	public void onCancelled(CancelledException cex) {
		
	}

	@Override
	public void onFinished() {
		
	}

	@Override
	public void onWaiting() {
		
	}

	@Override
	public void onStarted() {
		
	}


}
