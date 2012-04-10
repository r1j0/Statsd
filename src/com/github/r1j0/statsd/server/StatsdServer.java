package com.github.r1j0.statsd.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StatsdServer {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final int PORT = 39390;


	public void init() throws IOException {
		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		acceptor.setHandler(new StatsdServerHandler());

		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

		DatagramSessionConfig sessionConfig = acceptor.getSessionConfig();
		sessionConfig.setReuseAddress(true);
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);

		logger.info("Starting server...");

		acceptor.bind(new InetSocketAddress(PORT));

		logger.info("Listening on port: " + PORT);
	}


	public static void main(String[] args) throws IOException {
		StatsdServer server = new StatsdServer();
		server.init();
	}
}
