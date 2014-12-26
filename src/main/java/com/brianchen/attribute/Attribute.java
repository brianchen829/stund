package com.brianchen.attribute;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

import com.brianchen.attribute.AttributeInterface.AttributeType;

/*
 *@author chen.bin
 *@version 2014年12月18日上午10:53:55
 */
public abstract class Attribute {
	private static Logger logger = Logger.getLogger(Attribute.class);
	private AttributeType type;

	public abstract IoBuffer getBuffer();

	public abstract short getLength();

	public Attribute() {
	}

	public Attribute(AttributeType type) {
		setType(type);
	}

	public AttributeType getType() {
		return type;
	}

	public void setType(AttributeType type) {
		this.type = type;
	}

	public short type2Short(AttributeType type) {
		if (type == AttributeType.MappedAddress)
			return AttributeInterface.MAPPEDADDRESS;
		if (type == AttributeType.ResponseAddress)
			return AttributeInterface.RESPONSEADDRESS;
		if (type == AttributeType.ChangeRequest)
			return AttributeInterface.CHANGEREQUEST;
		if (type == AttributeType.SourceAddress)
			return AttributeInterface.SOURCEADDRESS;
		if (type == AttributeType.ChangedAddress)
			return AttributeInterface.CHANGEDADDRESS;
		if (type == AttributeType.Username)
			return AttributeInterface.USERNAME;
		if (type == AttributeType.Password)
			return AttributeInterface.PASSWORD;
		if (type == AttributeType.MessageIntegrity)
			return AttributeInterface.MESSAGEINTEGRITY;
		if (type == AttributeType.ErrorCode)
			return AttributeInterface.ERRORCODE;
		if (type == AttributeType.UnknownAttribute)
			return AttributeInterface.UNKNOWNATTRIBUTE;
		if (type == AttributeType.ReflectedFrom)
			return AttributeInterface.REFLECTEDFROM;
		if (type == AttributeType.ConnectionRequestBinding)
			return AttributeInterface.CONNECTIONREQUESTBINDING;
		if (type == AttributeType.BindingChange)
			return AttributeInterface.BINDINGCHANGE;
		if (type == AttributeType.Dummy)
			return AttributeInterface.DUMMY;
		return -1;
	}

	public abstract void parse(IoBuffer buf) throws Exception;

	public static Map<AttributeType,Attribute> parseAttributes(IoBuffer buf) {
		Map<AttributeType,Attribute> attributes = new HashMap<AttributeInterface.AttributeType, Attribute>();
		while (buf.remaining() > 0) {
			int type = buf.getShort();
			Attribute attribute = null;
			try{
			if (type == AttributeInterface.MAPPEDADDRESS) {
				attribute = new MappedAddress();
				attribute.parse(buf);
			}else if (type == AttributeInterface.SOURCEADDRESS) {
				attribute = new SourceAddress();
				attribute.parse(buf);
			}else if (type == AttributeInterface.CHANGEDADDRESS) {
				attribute = new ChangedAddress();
				attribute.parse(buf);
			}else if (type == AttributeInterface.PASSWORD) {
				attribute = new Password();
				attribute.parse(buf);
			}else if (type == AttributeInterface.CONNECTIONREQUESTBINDING) {
				attribute = new ConnectionRequestBinding();
				attribute.parse(buf);
			}else if (type == AttributeInterface.BINDINGCHANGE) {
				attribute = new BindingChange();
				attribute.parse(buf);
			}else if (type == AttributeInterface.CHANGEREQUEST) {
				attribute = new ChangeRequest();
				attribute.parse(buf);
			}else {
				logger.error("MessageAttribute with type " + type
						+ " unkown.");
			}
			}catch(Exception e){
				logger.error("attribute parse error:"+e.getMessage());
			}
			if(attribute!=null){
				attributes.put(attribute.getType(),attribute);
			}
		}
		return attributes;
	}

}
