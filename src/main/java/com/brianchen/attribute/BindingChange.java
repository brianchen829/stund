package com.brianchen.attribute;

import org.apache.mina.core.buffer.IoBuffer;

import com.brianchen.attribute.AttributeInterface.AttributeType;

/*
 *@author chen.bin
 *@version 2014年12月12日下午2:01:31
 */
public class BindingChange extends Attribute {

	private byte[] data;

	public BindingChange() {
		super(AttributeType.BindingChange);
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}

	@Override
	public IoBuffer getBuffer() {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		buf.putShort(type2Short(getType()));
		buf.putShort((short) getLength());
		buf.put(data);
		buf.flip();
		return buf;
	}

	@Override
	public short getLength() {
		return (short) data.length;
	}

	@Override
	public void parse(IoBuffer buf) {
		int length = buf.getShort();
		byte[] array = new byte[length];
		buf.get(array);	
		setData(array);
	}

}
