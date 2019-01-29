package me.zhaotb.framework.util;

import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

public class StringUtil extends StringUtils {
	
	public static boolean isEmpty(String s) {
		return s == null || s.length() < 1;
	}

	public static boolean isNotEmpty(String s) {
		return !isEmpty(s);
	}

	public static boolean isBlank(String s) {
		if (isEmpty(s)){
		    return true;
        }
        char[] chars = s.toCharArray();
		for (char c : chars) {
		    if (Character.isSpaceChar(c)){
		        return true;
            }
		}
        return false;
	}
	
	public static boolean isNotBlank(String s) {
		return !isBlank(s);
	}

	public static String uuid(){
		return UUID.randomUUID().toString().replace("-","");
	}

	public static String classNameToAppName(String className){
		char[] chars = className.toCharArray();
		char c = Character.toLowerCase(chars[0]);
		chars[0] = c;
		return new String(chars);
	}


	public static String normalToHexString(String name) {
		char[] source = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder();
		byte[] bytes = name.getBytes();
		int idxF;
		int idxS;
		for (byte b : bytes) {
			idxF = (b & 0x0f0) >> 4;
			idxS = b & 0x00f;
			sb.append(source[idxF]).append(source[idxS]);
		}
		return sb.toString();
	}

	public static String hexToNormalString(String name) {
		String source = "0123456789ABCDEF";
		byte[] bytes = name.getBytes();
		int length = name.length() / 2;
		byte[] buffer = new byte[length];
		int n;
		for (int i = 0; i < length; i++) {
			n = source.indexOf(bytes[2 * i]) << 4;
			n += source.indexOf(bytes[2 * i + 1]);
			buffer[i] = (byte) (n & 0xff);
		}
		return new String(buffer);
	}

}
