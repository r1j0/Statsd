package com.github.r1j0.statsd.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;


public class StatsdClient {

	private static final String HOST = "127.0.0.1";
	private static final int PORT = 39390;


	public static void main(String[] args) throws IOException, InterruptedException {
		IoConnector connector = new NioDatagramConnector();
		connector.getSessionConfig().setReadBufferSize(2048);

		connector.getFilterChain().addLast("logger", new LoggingFilter());
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

		connector.setHandler(new StatsdClientHandler("Hello Server.. äöüß"));
		ConnectFuture future = connector.connect(new InetSocketAddress(HOST, PORT));
		future.awaitUninterruptibly();

		if (!future.isConnected()) {
			return;
		}

		IoSession session = future.getSession();
		session.getConfig().setUseReadOperation(true);
		session.getCloseFuture().awaitUninterruptibly();

		System.out.println("After Writing");
		connector.dispose();
	}
}
