package com.tofirst.study.hbkdassistant.utils.common;


import com.tofirst.study.hbkdassistant.utils.common.LogUtils;

import java.io.Closeable;
import java.io.IOException;

public class IOUtils {
	/** 关闭流 */
	public static boolean close(Closeable io) {
		if (io != null) {
			try {
				io.close();
			} catch (IOException e) {
				LogUtils.e(e);
			}
		}
		return true;
	}
}
