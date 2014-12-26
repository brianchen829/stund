package com.brianchen.codec;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import com.brianchen.message.Message;

public class StunMessageEncoder implements MessageEncoder<Message> {
	private Logger logger = Logger.getLogger(StunMessageEncoder.class);
	

	@Override
	public void encode(IoSession session, Message message,
			ProtocolEncoderOutput out) throws Exception {
		IoBuffer buf = message.getBuffer();
		logger.info("message encode" + buf.toString());
		out.write(buf);
	}

}
