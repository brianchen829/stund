package com.brianchen;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;

import com.brianchen.codec.StunMessageCodecFactory;
import com.brianchen.codec.StunMessageDecoder;
import com.brianchen.codec.StunMessageEncoder;
import com.brianchen.handler.StunServerHandler;

public class StunServer {
	private static Logger logger = Logger.getLogger(StunServer.class);
	public static InetSocketAddress receiverAddress;
	public static InetSocketAddress changedPort;
	public static InetSocketAddress changedIP;
	public static InetSocketAddress changedPortIP;

	static {	
		InputStream in = null;
		try {
			in = StunServer.class.getClassLoader().getResourceAsStream(
					"stund.properties");
			if (in != null) {
				Properties prop = new Properties();
				int primaryPort;
				InetAddress primary;
				int secondaryPort;
				InetAddress secondary;
				prop.load(in);
				primaryPort = Integer.parseInt(prop.getProperty("primary.port").trim());
				primary = InetAddress.getByName(prop.getProperty("primary.ip").trim());
				secondaryPort = Integer.parseInt(prop.getProperty("secondary.port").trim());
				secondary = InetAddress.getByName(prop.getProperty("secondary.ip").trim());
				receiverAddress = new InetSocketAddress(primary, primaryPort);
				changedPort =  new InetSocketAddress(primary, secondaryPort);
				changedIP = new InetSocketAddress(secondary, primaryPort);
				changedPortIP = new InetSocketAddress(secondary, secondaryPort);
				
				if (secondary.getHostAddress().equals("127.0.0.1")){
					//strun server只有一个公网IP，不适合检测完全锥型NAT
					System.out.println("STUN Server has started, secondary interface uses to 127.0.0.1 - not optimal for full STUN functionality");
				}else{
					System.out.println("STUN Server has started, all interfaces are operational");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e);
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					logger.error(e);
					e.printStackTrace();
				}
			}
		}
	}


	public static void main(String[] args) {
		IoAcceptor acceptor = null;
		try {
			acceptor = new NioDatagramAcceptor();
			acceptor.getFilterChain().addLast(
					"codec",
					new ProtocolCodecFilter(new StunMessageCodecFactory(
							new StunMessageDecoder(),
							new StunMessageEncoder())));
			LoggingFilter lf = new LoggingFilter();
			lf.setMessageReceivedLogLevel(LogLevel.DEBUG);
			acceptor.getFilterChain().addLast("logger", lf);
			//IoSessionConfig cfg = acceptor.getSessionConfig();
			//cfg.setIdleTime(IdleStatus.BOTH_IDLE, 100);
			acceptor.setHandler(new StunServerHandler());
			List<InetSocketAddress> sockets = new ArrayList<InetSocketAddress>();	
			sockets.add(receiverAddress);
			sockets.add(changedPortIP);
			acceptor.bind(sockets);
			//logger.info("服务器启动成功。。。 端口号为："+PORT);
		} catch (Exception e) {
			logger.error("stund start fail",e);
			e.printStackTrace();
		}
	}
}
