package com.github.r1j0.statsd.server.netty;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.jboss.netty.bootstrap.ConnectionlessBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.FixedReceiveBufferSizePredictorFactory;
import org.jboss.netty.channel.socket.DatagramChannelFactory;
import org.jboss.netty.channel.socket.nio.NioDatagramChannelFactory;
import org.jboss.netty.handler.codec.string.StringDecoder;
import org.jboss.netty.handler.codec.string.StringEncoder;
import org.jboss.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.r1j0.statsd.configuration.StatsdConfiguration;
import com.github.r1j0.statsd.server.ServerThread;

public class ServerThreadNetty extends Thread implements ServerThread {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final LinkedBlockingQueue<String> linkedBlockingQueue;
	private final int port;


	public ServerThreadNetty(StatsdConfiguration configuration, LinkedBlockingQueue<String> linkedBlockingQueue) {
		this.linkedBlockingQueue = linkedBlockingQueue;
		this.port = configuration.getListeningPort();
	}


	@Override
	public void run() {
		log.info("ServerThreadNetty started.");

		DatagramChannelFactory f = new NioDatagramChannelFactory(Executors.newCachedThreadPool());
		ConnectionlessBootstrap b = new ConnectionlessBootstrap(f);

		b.setPipelineFactory(new ChannelPipelineFactory() {

			public ChannelPipeline getPipeline() throws Exception {
				return Channels.pipeline(new StringEncoder(CharsetUtil.UTF_8), new StringDecoder(CharsetUtil.UTF_8), new ServerHandlerNetty(linkedBlockingQueue));
			}
		});

		b.setOption("broadcast", "false");
		b.setOption("receiveBufferSizePredictorFactory", new FixedReceiveBufferSizePredictorFactory(1024));

		log.info("Starting server...");
		b.bind(new InetSocketAddress(port));
		log.info("Listening on port: " + port);
	}
}