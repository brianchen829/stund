/*
 * This file is part of JSTUN. 
 * 
 * Copyright (c) 2005 Thomas King <king@t-king.de> - All rights
 * reserved.
 * 
 * This software is licensed under either the GNU Public License (GPL),
 * or the Apache 2.0 license. Copies of both license agreements are
 * included in this distribution.
 */

package com.brianchen.attribute;

import org.apache.mina.core.buffer.IoBuffer;

import com.brianchen.attribute.AttributeInterface.AttributeType;
import com.brianchen.util.Utility;
import com.brianchen.util.UtilityException;

public class ResponseAddress extends Attribute {
	/*  
	 *  0                   1                   2                   3
	 *  0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1 2 3 4 5 6 7 8 9 0 1
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * |x x x x x x x x|    Family     |           Port                |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 * |                             Address                           |
	 * +-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+
	 */
	private int port;
	private Address address;

	public ResponseAddress() {
		super(AttributeType.ResponseAddress);
	}

	@Override
	public IoBuffer getBuffer() {
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		buf.putShort(type2Short(getType()));
		buf.putShort((short) 8);
		buf.put((byte) 0);
		buf.put((byte) 0x01);// family
		buf.put(Utility.integerToTwoBytes(port));// port;
		buf.put(address.getBytes());
		buf.flip();
		return buf;
	}

	@Override
	public short getLength() {
		return 8;
	}

	@Override
	public void parse(IoBuffer buf) throws UtilityException{
		buf.getShort();//length;
		buf.get();//xxxx
		buf.get();//family
		byte[] portArray = new byte[2];
		buf.get(portArray);
		setPort(Utility.twoBytesToInteger(portArray));
		byte[] ipArray = new byte[4];
		buf.get(ipArray);
		setAddress(new Address(ipArray));
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}
}
