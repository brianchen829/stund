package com.brianchen.attribute;

import org.apache.mina.core.buffer.IoBuffer;

import com.brianchen.attribute.AttributeInterface.AttributeType;
import com.brianchen.util.Utility;


/*
 *@author chen.bin
 *@version 2014年12月12日下午2:16:18
 */
public class ChangeRequest extends Attribute{
   /* 
    *  0                   1                   2                   3
    *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
    * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    * |0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 0 A B 0|
    * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
    */
	public ChangeRequest(){
		super(AttributeType.ChangeRequest);
	}
	
	private boolean changeIP = false;
	private boolean changePort = false;

	public boolean isChangeIP() {
		return changeIP;
	}

	public void setChangeIP(boolean changeIP) {
		this.changeIP = changeIP;
	}

	public boolean isChangePort() {
		return changePort;
	}

	public void setChangePort(boolean changePort) {
		this.changePort = changePort;
	}

	@Override
	public IoBuffer getBuffer() {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		buf.putShort(type2Short(getType()));
		buf.putShort((short) 4);
		byte[] bytes = new byte[4];
		if (changeIP) bytes[3] = Utility.integerToOneByte(4);
		if (changePort) bytes[3] = Utility.integerToOneByte(2);
		if (changeIP && changePort) bytes[3] = Utility.integerToOneByte(6);
		buf.put(bytes);
		buf.flip();
		return buf;
	}

	@Override
	public short getLength() {
		return 4;
	}

	@Override
	public void parse(IoBuffer buf) {
		buf.getShort();//length
		buf.skip(3);
		byte status = buf.get();
		switch (status) {
		case 0:
			break;
		case 2:
			setChangePort(true);
			break;
		case 4:
			setChangeIP(true);
			break;
		case 6:
			setChangeIP(true);
			setChangePort(true);
			break;
		default:
			break;
		}
	}
}
