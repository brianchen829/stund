package com.brianchen.message;
/*
 *@author chen.bin
 *@version 2014年12月12日下午2:41:17
 */
public interface MessageInterface {
	public enum MessageType { BindingRequest, BindingResponse, BindingErrorResponse, SharedSecretRequest, SharedSecretResponse, SharedSecretErrorResponse };
	//over udp
	public final static int BINDINGREQUEST = 0x0001;
	public final static int BINDINGRESPONSE = 0x0101;
	public final static int BINDINGERRORRESPONSE = 0x0111;
	//over tcp
	public final static int SHAREDSECRETREQUEST = 0x0002;
	public final static int SHAREDSECRETRESPONSE = 0x0102;
	public final static int SHAREDSECRETERRORRESPONSE = 0x0112;
}
