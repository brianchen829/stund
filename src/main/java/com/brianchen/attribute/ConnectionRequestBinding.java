package com.brianchen.attribute;

import org.apache.mina.core.buffer.IoBuffer;

import com.brianchen.attribute.AttributeInterface.AttributeType;

/*
 *@author chen.bin
 *@version 2014年12月12日下午2:07:34
 */
public class ConnectionRequestBinding extends Attribute {
	private String value;

	public ConnectionRequestBinding() {
		super(AttributeType.ConnectionRequestBinding);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public IoBuffer getBuffer() {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		buf.putShort(type2Short(getType()));
		buf.putShort((short) value.getBytes().length);
		buf.put(value.getBytes());
		buf.flip();
		return buf;
	}

	@Override
	public short getLength() {
		return (short) value.getBytes().length;
	}

	@Override
	public void parse(IoBuffer buf) {
		int length = buf.getShort();
		byte[] array = new byte[length];
		buf.get(array);	
		setValue(new String(array));
	}

}
