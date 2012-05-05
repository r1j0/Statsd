package com.github.r1j0.statsd.configuration;

import com.github.r1j0.statsd.backend.BackendConfiguration;

public class GraphiteBackendConfiguration implements BackendConfiguration {

	private StatsdConfiguration configuration;
	private static final String HOST = "backend.graphite.host";
	private static final String PORT = "backend.graphite.port";


	public String getHost() {
		return configuration.getValue(HOST);
	}


	public int getPort() {
		return Integer.parseInt(configuration.getValue(PORT));
	}


	public void setConfiguration(StatsdConfiguration configuration) {
		this.configuration = configuration;
	}

}
