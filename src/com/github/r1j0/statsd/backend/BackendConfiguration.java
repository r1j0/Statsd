package com.github.r1j0.statsd.backend;

import com.github.r1j0.statsd.configuration.StatsdConfiguration;

public interface BackendConfiguration {

	void setConfiguration(StatsdConfiguration configuration);


	void setIdentifier(String backendIdentifier);

}
