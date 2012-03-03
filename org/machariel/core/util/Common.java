package org.machariel.core.util;

public final class Common {
	private Common() {}
	
	public static String hex(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		int i = 0;
		for (byte b : bytes) {
			sb.append(String.format("%1$02X ", b));
			if (i++ % 4 == 3) sb.append("\n");
		}

		return sb.toString();
	}
}