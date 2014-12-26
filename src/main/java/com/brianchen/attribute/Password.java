package com.brianchen.attribute;

import org.apache.mina.core.buffer.IoBuffer;

import com.brianchen.attribute.AttributeInterface.AttributeType;

/*
 *@author chen.bin
 *@version 2014年12月12日下午2:17:03
 */
public class Password extends Attribute {
	private String password;

	public Password() {
		super(AttributeType.Password);
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public IoBuffer getBuffer() {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		buf.putShort(type2Short(getType()));
		buf.putShort((short) password.getBytes().length);
		buf.put(password.getBytes());
		buf.flip();
		return buf;
	}

	@Override
	public short getLength() {
		return (short) password.getBytes().length;
	}

	@Override
	public void parse(IoBuffer buf) {
		int length = buf.getShort();
		byte[] array = new byte[length];
		buf.get(array);	
		setPassword(new String(array));
	}

}
