package com.brianchen.handler;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

import com.brianchen.StunServer;
import com.brianchen.attribute.Address;
import com.brianchen.attribute.ChangeRequest;
import com.brianchen.attribute.ChangedAddress;
import com.brianchen.attribute.ConnectionRequestBinding;
import com.brianchen.attribute.MappedAddress;
import com.brianchen.attribute.ResponseAddress;
import com.brianchen.attribute.SourceAddress;
import com.brianchen.attribute.AttributeInterface.AttributeType;
import com.brianchen.codec.StunMessageCodecFactory;
import com.brianchen.codec.StunMessageDecoder;
import com.brianchen.codec.StunMessageEncoder;
import com.brianchen.message.BindingRequest;
import com.brianchen.message.BindingResponse;
import com.brianchen.message.Message;

public class StunServerHandler extends IoHandlerAdapter {
	private static Logger logger = Logger.getLogger(StunServerHandler.class);

	@Override
	public void exceptionCaught(IoSession session, Throwable cause)
			throws Exception {
		logger.error(cause);
	}

	@Override
	public void messageReceived(IoSession session, Object message)
			throws Exception {
		if (message instanceof BindingRequest) {
			BindingRequest request = (BindingRequest) message;
			// ResponseAddres optional,include in bindingrequest
			ResponseAddress ra = (ResponseAddress) request
					.getAttribute(AttributeType.ResponseAddress);
			BindingResponse resp = new BindingResponse();
			resp.setId(request.getId());
			// MappedAddress, the request raw IP address
			MappedAddress ma = new MappedAddress();
			ma.setAddress(new Address(((InetSocketAddress) session
					.getRemoteAddress()).getAddress().getHostAddress()));
			ma.setPort(((InetSocketAddress) session.getRemoteAddress())
					.getPort());
			resp.addAttribute(ma);
			// ChangedAddress
			ChangedAddress ca = new ChangedAddress();
			ca.setAddress(new Address(StunServer.changedPortIP.getAddress()
					.getAddress()));
			ca.setPort(StunServer.changedPortIP.getPort());
			resp.addAttribute(ca);

			ChangeRequest cr = (ChangeRequest) request
					.getAttribute(AttributeType.ChangeRequest);
			ConnectionRequestBinding crb = (ConnectionRequestBinding) request
					.getAttribute(AttributeType.ConnectionRequestBinding);

			if (crb != null) {
				// Source address attribute
				SourceAddress sa = new SourceAddress();
				sa.setAddress(new Address(StunServer.receiverAddress
						.getAddress().getAddress()));
				sa.setPort(StunServer.receiverAddress.getPort());
				resp.addAttribute(sa);
				if (ra != null) {
					// response address,echo to another client address
					SocketAddress remoteAddress = new InetSocketAddress(ra
							.getAddress().toString(), ra.getPort());
					session.write(resp, remoteAddress);
				} else {
					// echo to the same client address
					logger.info(" echo to the same client address");
					session.write(resp);
				}
			} else if (cr != null) {
				// need echo from different IP or port
				processChangeRequest(session,cr, resp, ra);
			} else {
				// Unknown
				logger.error("Message attribute change request is not set.");
			}
		} else {
			logger.warn("unkown request:"+message);
		}
	}

	private void processChangeRequest(IoSession session,ChangeRequest cr, Message response,
			ResponseAddress ra) throws Exception {
		// 检查需要改变的IP或端口标志位
		if (cr.isChangePort() && (!cr.isChangeIP())) {
			// Source address attribute，预示将要改变的Port
			SourceAddress sa = new SourceAddress();
			sa.setAddress(new Address(StunServer.receiverAddress.getAddress()
					.getAddress()));
			sa.setPort(StunServer.changedPort.getPort());
			response.addAttribute(sa);
			send(response, sa, ra);
		} else if ((!cr.isChangePort()) && cr.isChangeIP()) {
			// Source address attribute，预示将要改变的IP
			SourceAddress sa = new SourceAddress();
			sa.setAddress(new Address(StunServer.changedIP.getAddress()
					.getAddress()));
			sa.setPort(StunServer.receiverAddress.getPort());
			response.addAttribute(sa);
			send(response, sa, ra);
		} else if ((!cr.isChangePort()) && (!cr.isChangeIP())) {
			// Source address attribute
			SourceAddress sa = new SourceAddress();
			sa.setAddress(new Address(StunServer.receiverAddress.getAddress()
					.getAddress()));
			sa.setPort(StunServer.receiverAddress.getPort());
			response.addAttribute(sa);	
			session.write(response);
		} else if (cr.isChangePort() && cr.isChangeIP()) {
			// Source address attribute
			SourceAddress sa = new SourceAddress();
			sa.setAddress(new Address(StunServer.changedPortIP.getAddress()
					.getAddress()));
			sa.setPort(StunServer.changedPortIP.getPort());
			response.addAttribute(sa);
			send(response, sa, ra);
		}
	}

	private void send(final Message response, final SourceAddress sa,
			final ResponseAddress ra) {
		NioDatagramConnector connector = new NioDatagramConnector();
		connector.getFilterChain().addLast(
				"codec",
				new ProtocolCodecFilter(new StunMessageCodecFactory(
						new StunMessageDecoder(), new StunMessageEncoder())));
		connector.setHandler(new IoHandlerAdapter() {
			@Override
			public void sessionOpened(IoSession session) throws Exception {
				if (ra != null) {
					session.write(response);
				} else {
					session.write(response);
				}
			}
		});
		connector.setConnectTimeoutCheckInterval(30);
		SocketAddress localAddress = new InetSocketAddress(sa.getAddress()
				.toString(), sa.getPort());
		SocketAddress remoteAddress = null;
		if (ra != null) {
			remoteAddress = new InetSocketAddress(ra.getAddress().toString(),
					ra.getPort());
		} else {
			MappedAddress ma = (MappedAddress) response
					.getAttribute(AttributeType.MappedAddress);
			remoteAddress = new InetSocketAddress(ma.getAddress().toString(),
					ma.getPort());
		}
		connector.connect(remoteAddress, localAddress);
		connector.dispose();
	}

}
