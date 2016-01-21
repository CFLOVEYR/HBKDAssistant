package com.tofirst.study.hbkdassistant.utils.xutils3;

import com.tofirst.study.hbkdassistant.inteface.MyCallBack;

import org.json.JSONObject;
import org.xutils.common.Callback.Cancelable;
import org.xutils.common.Callback.CommonCallback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.util.Map;

public class XUtil {
	/**
	 * 发送get请求
	 * @param <T>
	 */
	public static <T> Cancelable Get(String url,Map<String,String> map,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		if(null!=map){
			for(Map.Entry<String, String> entry : map.entrySet()){
				params.addQueryStringParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}
	/**
	 * 发送get请求一个json字符串
	 *
	 * @param <T>
	 */
	public static <T> Cancelable GetJson(String url, MyCallBack<String> callback) {
		RequestParams params = new RequestParams(url);
		//params.setSslSocketFactory(...); // 设置ssl
		// params.addQueryStringParameter("wd", "xUtils");
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}
	/**
	 * 发送get请求一个JSONObject
	 *
	 * @param <T>
	 */
	public static <T> Cancelable getJsonObject(String url, MyCallBack<JSONObject> callback) {
		RequestParams params = new RequestParams(url);
		//params.setSslSocketFactory(...); // 设置ssl
		// params.addQueryStringParameter("wd", "xUtils");
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}
	/**
	 * 发送post请求
	 * @param <T>
	 */
	public static <T> Cancelable Post(String url,Map<String,Object> map,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		if(null!=map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}


	/**
	 * 上传文件
	 * @param <T>
	 */
	public static <T> Cancelable UpLoadFile(String url,Map<String,Object> map,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		if(null!=map){
			for(Map.Entry<String, Object> entry : map.entrySet()){
				params.addParameter(entry.getKey(), entry.getValue());
			}
		}
		params.setMultipart(true);
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}

	/**
	 * 下载文件
	 * @param <T>
	 */
	public static <T> Cancelable DownLoadFile(String url,String filepath,CommonCallback<T> callback){
		RequestParams params=new RequestParams(url);
		//设置断点续传
		params.setAutoResume(true);
		params.setSaveFilePath(filepath);
		Cancelable cancelable = x.http().get(params, callback);
		return cancelable;
	}
}
