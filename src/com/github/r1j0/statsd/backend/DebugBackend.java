package com.github.r1j0.statsd.backend;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugBackend implements Backend {

	private final Logger logger = LoggerFactory.getLogger(getClass());


	public String getName() {
		return getClass().getSimpleName();
	}


	public boolean send(String message) {
		logger.info("Received message: " + message);

		return true;
	}
}
