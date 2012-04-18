package com.github.r1j0.statsd.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.concurrent.LinkedBlockingQueue;

import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.DatagramSessionConfig;
import org.apache.mina.transport.socket.nio.NioDatagramAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerThread extends Thread {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final int port;
	private final LinkedBlockingQueue<String> linkedBlockingQueue;


	public ServerThread(StatsdConfiguration configuration, LinkedBlockingQueue<String> linkedBlockingQueue) {
		this.linkedBlockingQueue = linkedBlockingQueue;

		port = configuration.getListeningPort();
	}


	@Override
	public void run() {
		log.info("ServerThread started.");

		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		acceptor.setHandler(new StatsdServerHandler(linkedBlockingQueue));

		acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

		DatagramSessionConfig sessionConfig = acceptor.getSessionConfig();
		sessionConfig.setReuseAddress(true);
		acceptor.getSessionConfig().setReadBufferSize(2048);
		acceptor.getSessionConfig().setIdleTime(IdleStatus.BOTH_IDLE, 5);

		log.info("Starting server...");

		try {
			acceptor.bind(new InetSocketAddress(port));
			log.info("Listening on port: " + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
