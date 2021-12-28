package com.sicnu.community.util;

/**
 * @author Tangliyi (2238192070@qq.com)
 */
public class ByteUtil {
    public static byte[] int2Byte(int value) {
        byte[] byteArray = new byte[4];
        byteArray[3] = (byte) ((value >> 24) & 0xFF);
        byteArray[2] = (byte) ((value >> 16) & 0xFF);
        byteArray[1] = (byte) ((value >> 8) & 0xFF);
        byteArray[0] = (byte) (value & 0xFF);
        return byteArray;
    }

    public static int bytes2Int(byte[] bytes) {
        int value = 0;
        value = ((bytes[3] & 0xff) << 24) | ((bytes[2] & 0xff) << 16) | ((bytes[1] & 0xff) << 8) | (bytes[0] & 0xff);
        return value;
    }
}
