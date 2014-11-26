package com.sina.weibo.sdk.utils;
public final class Base64 {
	private static char[] alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
			.toCharArray();

	private static byte[] codes = new byte[256];

	static {
		for (int i = 0; i < 256; i++) {
			Base64.codes[i] = -1;
		}
		for (int i = 'A'; i <= 'Z'; i++) {
			Base64.codes[i] = (byte) (i - 'A');
		}
		for (int i = 'a'; i <= 'z'; i++) {
			Base64.codes[i] = (byte) (26 + i - 'a');
		}
		for (int i = '0'; i <= '9'; i++) {
			Base64.codes[i] = (byte) (52 + i - '0');
		}
		Base64.codes['+'] = 62;
		Base64.codes['/'] = 63;
	}

	/**
    * 将base64编码的数据解码成原始数据
    */
	public static byte[] decode(byte[] data) {
		int len = ((data.length + 3) / 4) * 3;
		if ((data.length > 0) && (data[data.length - 1] == '=')) {
			--len;
		}
		if ((data.length > 1) && (data[data.length - 2] == '=')) {
			--len;
		}
		final byte[] out = new byte[len];
		int shift = 0;
		int accum = 0;
		int index = 0;
		for (int ix = 0; ix < data.length; ix++) {
			final int value = Base64.codes[data[ix] & 0xFF];
			if (value >= 0) {
				accum <<= 6;
				shift += 6;
				accum |= value;
				if (shift >= 8) {
					shift -= 8;
					out[index++] = (byte) ((accum >> shift) & 0xff);
				}
			}
		}
		if (index != out.length) {
			throw new RuntimeException("miscalculated data length!");
		}
		return out;
	}

	/**
     * 将原始数据编码为base64编码
     */
	public static char[] encode(byte[] data) {
		final char[] out = new char[((data.length + 2) / 3) * 4];

		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;
			int val = (0xFF & data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & data[i + 2]);
				quad = true;
			}
			out[index + 3] = Base64.alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = Base64.alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = Base64.alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = Base64.alphabet[val & 0x3F];
		}
		return out;
	}
	
	/**
     * 将原始数据编码为base64编码
     */
	public static byte[] encodebyte(byte[] data) {
		final byte[] out = new byte[((data.length + 2) / 3) * 4];

		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
			boolean quad = false;
			boolean trip = false;
			int val = (0xFF & data[i]);
			val <<= 8;
			if ((i + 1) < data.length) {
				val |= (0xFF & data[i + 1]);
				trip = true;
			}
			val <<= 8;
			if ((i + 2) < data.length) {
				val |= (0xFF & data[i + 2]);
				quad = true;
			}
			out[index + 3] = (byte)Base64.alphabet[(quad ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 2] = (byte)Base64.alphabet[(trip ? (val & 0x3F) : 64)];
			val >>= 6;
			out[index + 1] = (byte)Base64.alphabet[val & 0x3F];
			val >>= 6;
			out[index + 0] = (byte)Base64.alphabet[val & 0x3F];
		}
		return out;
	}
}