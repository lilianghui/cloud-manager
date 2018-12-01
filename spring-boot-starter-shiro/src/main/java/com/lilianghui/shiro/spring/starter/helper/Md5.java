package com.lilianghui.shiro.spring.starter.helper;

import java.security.MessageDigest;

/**
 * MD5加密工具类
 *
 * @author 李亮辉
 */
public final class Md5 {
	private static final char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };

	public String generate(String s) {
		return generate(s.getBytes());
	}

	public String generate(byte[] btInput) {
		try {
			// 获得MD5摘要算法的 MessageDigest 对象
			MessageDigest mdInst = MessageDigest.getInstance("MD5");
			// 使用指定的字节更新摘要
			mdInst.update(btInput);
			// 获得密文
			byte[] md = mdInst.digest();
			// 把密文转换成十六进制的字符串形式
			int j = md.length;
			char str[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				str[k++] = hexDigits[byte0 >>> 4 & 0xf];
				str[k++] = hexDigits[byte0 & 0xf];
			}// A1816C8BB61E1D7D2A99BE726F13694E
			return new String(str);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

}