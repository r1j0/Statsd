package com.github.r1j0.statsd.server.framework;

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

import com.github.r1j0.statsd.configuration.StatsdConfiguration;
import com.github.r1j0.statsd.server.ServerThread;

public class ServerThreadMina extends Thread implements ServerThread {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final LinkedBlockingQueue<String> linkedBlockingQueue;
	private final int port;


	public ServerThreadMina(StatsdConfiguration configuration, LinkedBlockingQueue<String> linkedBlockingQueue) {
		this.linkedBlockingQueue = linkedBlockingQueue;
		this.port = configuration.getListeningPort();
	}


	@Override
	public void run() {
		log.info("ServerThreadMina started.");

		NioDatagramAcceptor acceptor = new NioDatagramAcceptor();
		acceptor.setHandler(new ServerHandlerMina(linkedBlockingQueue));

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
