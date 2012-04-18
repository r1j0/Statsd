package com.github.r1j0.statsd.client;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class StatsdClientHandler extends IoHandlerAdapter {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final String values;
	private boolean finished;


	public StatsdClientHandler(String values) {
		this.values = values;
	}


	public boolean isFinished() {
		return finished;
	}


	@Override
	public void sessionOpened(IoSession session) {
		session.write(values);
	}


	@Override
	public void messageReceived(IoSession session, Object message) {
		log.info("Message received in the client..");
		log.info("Message is: " + message.toString());
	}


	@Override
	public void exceptionCaught(IoSession session, Throwable cause) {
		session.close(true);
	}
}
