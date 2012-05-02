package com.github.r1j0.statsd.backend;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.r1j0.statsd.server.StatsdConfiguration;

public class DebugBackend implements Backend {

	private final Logger log = LoggerFactory.getLogger(getClass());


	public boolean notify(List<String> messages) {
		log.info(getClass().getSimpleName() + " received messages: " + messages.toString());
		return true;
	}


	public void setConfiguration(StatsdConfiguration configuration) {
		// NoOp
	}
}
