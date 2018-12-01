package com.lilianghui.shiro.spring.starter.helper;

import java.net.InetAddress;
import java.security.SecureRandom;

public final class UUID {

	private static int IP;
	private static final SecureRandom random = new SecureRandom();
	private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final String sep = "";
	private static short counter = (short) 0;
	private static final int JVM = (int) (System.currentTimeMillis() >>> 8);

	public UUID() {
		try {
			IP = IptoInt(InetAddress.getLocalHost().getAddress());
		} catch (Exception e) {
			IP = 0;
		}
	}

	private int IptoInt(byte[] bytes) {
		int result = 0;
		for (int i = 0; i < 4; i++) {
			result = (result << 8) - Byte.MIN_VALUE + (int) bytes[i];
		}
		return result;
	}

	/**
	 * Unique across JVMs on this machine (unless they load this class in the
	 * same quater second - very unlikely)
	 */
	private int getJVM() {
		return JVM;
	}

	/**
	 * Unique in a millisecond for this JVM instance (unless there are >
	 * Short.MAX_VALUE instances created in a millisecond)
	 */
	private short getCount() {
		synchronized (UUID.class) {
			if (counter < 0)
				counter = 0;
			return counter++;
		}
	}

	/**
	 * Unique in a local network
	 */
	private int getIP() {
		return IP;
	}

	/**
	 * Unique down to millisecond
	 */
	private short getHiTime() {
		return (short) (System.currentTimeMillis() >>> 32);
	}

	private int getLoTime() {
		return (int) System.currentTimeMillis();
	}

	private String format(int intval) {
		String formatted = Integer.toHexString(intval);
		StringBuffer buf = new StringBuffer("00000000");
		buf.replace(8 - formatted.length(), 8, formatted);
		return buf.toString();
	}

	private String format(short shortval) {
		String formatted = Integer.toHexString(shortval);
		StringBuffer buf = new StringBuffer("0000");
		buf.replace(4 - formatted.length(), 4, formatted);
		return buf.toString();
	}

	public String generate() {
		return new StringBuffer(36).append(format(getIP())).append(sep).append(format(getJVM())).append(sep).append(format(getHiTime())).append(sep).append(format(getLoTime())).append(sep).append(format(getCount())).toString();
	}

	public String randomBase62() {
		return randomBase62(50);
	}

	public String randomBase62(int length) {
		byte[] randomBytes = new byte[length];
		random.nextBytes(randomBytes);
		return encodeBase62(randomBytes);
	}

	/**
	 * Base62编码。
	 */
	private String encodeBase62(byte[] input) {
		char[] chars = new char[input.length];
		for (int i = 0; i < input.length; i++) {
			chars[i] = BASE62[((input[i] & 0xFF) % BASE62.length)];
		}
		return new String(chars);
	}
}