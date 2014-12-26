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

public interface AttributeInterface {
	public enum AttributeType { MappedAddress, ResponseAddress, ChangeRequest, SourceAddress, ChangedAddress, Username, Password, MessageIntegrity, ErrorCode, UnknownAttribute, ReflectedFrom, ConnectionRequestBinding, BindingChange, Dummy };
	final static short MAPPEDADDRESS = 0x0001;
	final static short RESPONSEADDRESS = 0x0002;
	final static short CHANGEREQUEST = 0x0003;
	final static short SOURCEADDRESS = 0x0004;
	final static short CHANGEDADDRESS = 0x0005;
	final static short USERNAME = 0x0006;
	final static short PASSWORD = 0x0007;
	final static short MESSAGEINTEGRITY = 0x0008;
	final static short ERRORCODE = 0x0009;
	final static short UNKNOWNATTRIBUTE = 0x000a;
	final static short REFLECTEDFROM = 0x000b;
	final static short CONNECTIONREQUESTBINDING = (short) 0xC001;
	final static short BINDINGCHANGE = (short) 0xC002;
	final static short DUMMY = 0x0000;
}