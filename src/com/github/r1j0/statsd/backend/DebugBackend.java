package com.github.r1j0.statsd.backend;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class DebugBackend implements Backend {

	private final Logger logger = LoggerFactory.getLogger(getClass());


	public boolean send(List<String> messages) {
		logger.info("Received messages: " + messages.toString());
		return true;
	}
}
