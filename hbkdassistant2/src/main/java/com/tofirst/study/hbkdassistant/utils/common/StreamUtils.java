package com.tofirst.study.hbkdassistant.utils.common;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtils {

	/**
	 * 解析字符串的操作
	 * @param is
	 * @return
	 */
	
	public static String readInputStream(InputStream is){
		try {
			ByteArrayOutputStream baos=new ByteArrayOutputStream();
			int len=0;
			byte[]buffer=new byte[1024];
			
			while((len=is.read(buffer))!=-1){
				baos.write(buffer,0,len);
			}
			is.close();
			baos.close();
			byte[] result=baos.toByteArray();
			//���Ž���result�еĴ���
			String temp =new String(result);
			if(temp.contains("utf-8")){
				return temp;
			}else if(temp.contains("gb2312")){
				return new String(result,"gb2312");
			}
			return temp;
			//return new String(result);//Ĭ�ϵ�utf-8�����ʽ��������ʽ�Ļ���ַ�������
		} catch (Exception e) {
			// TODO �Զ����ɵ� catch ��
			e.printStackTrace();
			return "��ȡԴ����ʧ��";
		}
	}
}
