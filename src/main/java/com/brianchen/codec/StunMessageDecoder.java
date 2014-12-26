package com.brianchen.codec;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageDecoderResult;

import com.brianchen.message.BindingRequest;
import com.brianchen.message.BindingResponse;
import com.brianchen.message.Message;
import com.brianchen.message.MessageInterface;

public class StunMessageDecoder implements MessageDecoder {
	private Logger logger = Logger.getLogger(StunMessageDecoder.class);
	
	@Override
	public MessageDecoderResult decodable(IoSession session, IoBuffer in) {
		//header length
		if (in.remaining() < 20) {
			return MessageDecoderResult.NEED_DATA;
		}
		//message type
		short type = in.getShort();
		if (type == MessageInterface.BINDINGREQUEST || type == MessageInterface.BINDINGRESPONSE) {
			logger.info("message type:"+type);
		} else {
			logger.error("unknown message type:"+type);
			return MessageDecoderResult.NOT_OK;
		}
		//attribute length
		short len = in.getShort();
		if (in.remaining() < len+16) {
			return MessageDecoderResult.NEED_DATA;
		}
		return MessageDecoderResult.OK;
	}

	@Override
	public MessageDecoderResult decode(IoSession session, IoBuffer in,
			ProtocolDecoderOutput out) throws Exception {
		logger.info("message decode:" + in.toString());
		Message message = null;
		//decode header
		short type = in.getShort();
		short len = in.getShort();
		byte[] temp = new byte[len+16];
		in.get(temp);
		//undecode buf
		IoBuffer buf = IoBuffer.allocate(100).setAutoExpand(true);
		buf.put(temp);
		buf.flip();
		if (type == MessageInterface.BINDINGREQUEST) {
			BindingRequest req = new BindingRequest();	
			req.parse(buf);
			message = req;
		} else if (type == MessageInterface.BINDINGRESPONSE) {
			BindingResponse resp = new BindingResponse();
			resp.parse(buf);
			message = resp;
		}else {
			logger.error("unknown decode type");
		}
		out.write(message);
		return MessageDecoderResult.OK;
	}

	@Override
	public void finishDecode(IoSession arg0, ProtocolDecoderOutput arg1)
			throws Exception {
	}

}
