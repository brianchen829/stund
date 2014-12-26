package com.brianchen.message;

import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;

import com.brianchen.attribute.Attribute;
import com.brianchen.attribute.AttributeInterface.AttributeType;
import com.brianchen.message.MessageInterface.MessageType;

/*
 *@author chen.bin
 *@version 2014年12月17日下午4:28:13
 */
public class BindingRequest extends Message {

	public BindingRequest() {
		generateTransactionID();
		setType(MessageType.BindingRequest);
	}

	@Override
	public short getLength() {
		short len = 0;
		if (getAttributes() != null && getAttributes().size() > 0) {
			for (Attribute attribute : getAttributes().values()) {
				len += 2 + 2 + attribute.getLength();
			}
		}
		return len;
	}

	@Override
	public IoBuffer getBuffer() {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		buf.putShort(type2Short(getType()));
		IoBuffer databuf = IoBuffer.allocate(100).setAutoExpand(true);
		if (getAttributes() != null) {
			for (Attribute attribute : getAttributes().values()) {
				databuf.put(attribute.getBuffer());
			}
		}
		databuf.flip();
		buf.putShort(getLength());
		buf.put(getId());
		buf.put(databuf);
		buf.flip();
		return buf;
	}

	@Override
	public void parse(IoBuffer buf) {
		byte[] idArray = new byte[16];
		buf.get(idArray);
		setId(idArray);
		Map<AttributeType, Attribute> attributes = Attribute.parseAttributes(buf);
		setAttributes(attributes);
	}

}
