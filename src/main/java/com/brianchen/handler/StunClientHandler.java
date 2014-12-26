package com.brianchen.handler;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.brianchen.attribute.Attribute;
import com.brianchen.message.BindingResponse;

public class StunClientHandler extends IoHandlerAdapter {
	private static Logger logger = Logger.getLogger(StunClientHandler.class);
	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof BindingResponse) {
			BindingResponse resp = (BindingResponse) message;

			if (resp.getAttributes() != null) {
				for (Attribute attribute:resp.getAttributes().values()) {
					logger.debug("resp:"+attribute);
				}
			}
			session.close(true);
		}else {
			logger.warn("unknown response:"+message);
		}
	}
	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error(cause.getMessage());
	}
	
	
	
	

}
