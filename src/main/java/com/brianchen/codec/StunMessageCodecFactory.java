package com.brianchen.codec;

import org.apache.mina.filter.codec.demux.DemuxingProtocolCodecFactory;
import org.apache.mina.filter.codec.demux.MessageDecoder;
import org.apache.mina.filter.codec.demux.MessageEncoder;

import com.brianchen.message.Message;

public class StunMessageCodecFactory extends DemuxingProtocolCodecFactory{
	private MessageDecoder decoder;
	
	private MessageEncoder<Message> encoder;
	
	public StunMessageCodecFactory(MessageDecoder decoder,MessageEncoder<Message> encoder){
		this.decoder = decoder;
		this.encoder = encoder;
		addMessageDecoder(this.decoder);
		addMessageEncoder(Message.class, this.encoder);
	}
}
