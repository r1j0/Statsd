package com.github.r1j0.statsd.backend;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugBackend implements Backend {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final DebugBackendConfiguration configuration;


	public DebugBackend() {
		this.configuration = new DebugBackendConfiguration();
	}


	public boolean notify(List<String> messages) {
		log.info(getClass().getSimpleName() + " received messages: " + messages.toString());
		return true;
	}


	public DebugBackendConfiguration getConfiguration() {
		return configuration;
	}
}
