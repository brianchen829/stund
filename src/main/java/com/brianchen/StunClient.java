package com.brianchen;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import com.brianchen.attribute.Attribute;
import com.brianchen.attribute.ConnectionRequestBinding;
import com.brianchen.attribute.Password;
import com.brianchen.attribute.AttributeInterface.AttributeType;
import com.brianchen.codec.StunMessageCodecFactory;
import com.brianchen.codec.StunMessageDecoder;
import com.brianchen.codec.StunMessageEncoder;
import com.brianchen.handler.StunClientHandler;
import com.brianchen.message.BindingRequest;

public class StunClient {
	private static Logger logger = Logger.getLogger(StunClient.class);
	private static String HOST = "127.0.0.1";
	private static int PORT = 3478;

	public static void main(String[] args) {
		NioDatagramConnector connector=new NioDatagramConnector();  
		//connector.setConnectTimeoutMillis(30000);
		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new StunMessageCodecFactory(
						new StunMessageDecoder(),
						new StunMessageEncoder())));
		connector.setHandler(new StunClientHandler());
		IoSession session = null;
		try {
			ConnectFuture future = connector.connect(new InetSocketAddress(
					HOST, PORT));
			future.awaitUninterruptibly();
			session = future.getSession();
			BindingRequest req = new BindingRequest();
			Map<AttributeType,Attribute> attributes = new HashMap<AttributeType, Attribute>();
			Password password = new Password();
			password.setPassword("123456");
			attributes.put(password.getType(),password);
			ConnectionRequestBinding crb = new ConnectionRequestBinding();
			crb.setValue("dslforum.org/TR-111");
			attributes.put(crb.getType(),crb);
			req.setAttributes(attributes);
			session.write(req);
			session.getCloseFuture().awaitUninterruptibly();
			connector.dispose();
		} catch (Exception e) {
			logger.error("客户端连接异常", e);
		}
	}

}
