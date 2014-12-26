package com.brianchen.message;

import java.util.HashMap;
import java.util.Map;

import org.apache.mina.core.buffer.IoBuffer;

import com.brianchen.attribute.Attribute;
import com.brianchen.attribute.AttributeInterface.AttributeType;
import com.brianchen.message.MessageInterface.MessageType;
import com.brianchen.util.Utility;

/*
 *@author chen.bin
 *@version 2014年12月17日下午4:27:42
 */
public abstract class Message {
	private MessageType type;

	public Message() {
	}

	public Message(MessageType type) {
		setType(type);
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}

	public abstract short getLength();

	public abstract IoBuffer getBuffer();

	public abstract void parse(IoBuffer buf);

	private Map<AttributeType, Attribute> attributes = new HashMap<AttributeType, Attribute>();
	private byte[] id = new byte[16];

	public byte[] getId() {
		return id;
	}

	public void setId(byte[] id) {
		this.id = id;
	}

	public void generateTransactionID() {
		System.arraycopy(
				Utility.integerToTwoBytes((int) (Math.random() * 65536)), 0,
				id, 0, 2);
		System.arraycopy(
				Utility.integerToTwoBytes((int) (Math.random() * 65536)), 0,
				id, 2, 2);
		System.arraycopy(
				Utility.integerToTwoBytes((int) (Math.random() * 65536)), 0,
				id, 4, 2);
		System.arraycopy(
				Utility.integerToTwoBytes((int) (Math.random() * 65536)), 0,
				id, 6, 2);
		System.arraycopy(
				Utility.integerToTwoBytes((int) (Math.random() * 65536)), 0,
				id, 8, 2);
		System.arraycopy(
				Utility.integerToTwoBytes((int) (Math.random() * 65536)), 0,
				id, 10, 2);
		System.arraycopy(
				Utility.integerToTwoBytes((int) (Math.random() * 65536)), 0,
				id, 12, 2);
		System.arraycopy(
				Utility.integerToTwoBytes((int) (Math.random() * 65536)), 0,
				id, 14, 2);
	}

	public Map<AttributeType, Attribute> getAttributes() {
		return attributes;
	}

	public void setAttributes(Map<AttributeType, Attribute> attributes) {
		this.attributes = attributes;
	}

	public void addAttribute(Attribute attribute) {
		attributes.put(attribute.getType(), attribute);
	}

	public Attribute getAttribute(AttributeType type) {
		return attributes.get(type);
	}
	
	public short type2Short(MessageType type) {
		if (type == MessageType.BindingResponse) {
			return MessageInterface.BINDINGRESPONSE;
		}
		if (type == MessageType.BindingRequest) {
			return MessageInterface.BINDINGREQUEST;
		}
		if (type == MessageType.BindingErrorResponse) {
			return MessageInterface.BINDINGERRORRESPONSE;
		}
		if (type == MessageType.SharedSecretRequest) {
			return MessageInterface.SHAREDSECRETREQUEST;
		}
		if (type == MessageType.SharedSecretResponse) {
			return MessageInterface.SHAREDSECRETRESPONSE;
		}
		if (type == MessageType.SharedSecretErrorResponse) {
			return MessageInterface.SHAREDSECRETERRORRESPONSE;
		}
		return -1;
	}

}
