package com.github.r1j0.statsd.backend.configuration;

import com.github.r1j0.statsd.backend.BackendConfiguration;
import com.github.r1j0.statsd.configuration.StatsdConfiguration;

public class GraphiteBackendConfiguration implements BackendConfiguration {

	private static final String BACKEND_PREFIX = "backend";
	private static final String HOST = "host";
	private static final String PORT = "port";

	private StatsdConfiguration configuration;
	private String backendIdentifier;


	public String getHost() {
		return configuration.getValue(BACKEND_PREFIX + "." + backendIdentifier + "." + HOST);
	}


	public int getPort() {
		return Integer.parseInt(configuration.getValue(BACKEND_PREFIX + "." + backendIdentifier + "." + PORT));
	}


	public void setConfiguration(StatsdConfiguration configuration) {
		this.configuration = configuration;
	}


	public void setIdentifier(String backendIdentifier) {
		this.backendIdentifier = backendIdentifier;

	}
}
