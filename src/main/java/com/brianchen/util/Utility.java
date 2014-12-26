package com.brianchen.util;
/*
 *@author chen.bin
 *@version 2014年12月12日下午5:59:54
 */
public class Utility {
	public static final byte integerToOneByte(int value){
		return (byte)(value & 0xFF);
	}
	
	public static final byte[] integerToTwoBytes(int value){
		byte[] result = new byte[2];
        result[0] = (byte)((value >>> 8) & 0xFF);
        result[1] = (byte)(value & 0xFF);
		return result; 
	}
	
	public static final byte[] integerToFourBytes(int value){
		byte[] result = new byte[4];
        result[0] = (byte)((value >>> 24) & 0xFF);
		result[1] = (byte)((value >>> 16) & 0xFF);
		result[2] = (byte)((value >>> 8) & 0xFF);
        result[3] = (byte)(value & 0xFF);
		return result; 
	}
	
	public static final int oneByteToInteger(byte value){
		return (int)value & 0xFF;
	}
	
	public static final int twoBytesToInteger(byte[] value){
        int temp0 = value[0] & 0xFF;
        int temp1 = value[1] & 0xFF;
        return ((temp0 << 8) + temp1);
	}
	
	public static final long fourBytesToLong(byte[] value){
        int temp0 = value[0] & 0xFF;
        int temp1 = value[1] & 0xFF;
		int temp2 = value[2] & 0xFF;
		int temp3 = value[3] & 0xFF;
        return (((long)temp0 << 24) + (temp1 << 16) + (temp2 << 8) + temp3);
	}	 
}
