package com.github.r1j0.statsd.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServerHandlerNetty extends SimpleChannelUpstreamHandler {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final LinkedBlockingQueue<String> linkedBlockingQueue;


	public ServerHandlerNetty(LinkedBlockingQueue<String> linkedBlockingQueue) {
		this.linkedBlockingQueue = linkedBlockingQueue;
	}


	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) throws Exception {
		final String message = (String) e.getMessage();

		log.info("Message received in the server..");
		linkedBlockingQueue.put(message);
	}


	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
		e.getCause().printStackTrace();
		// We don't close the channel because we can keep serving requests.
	}
}
