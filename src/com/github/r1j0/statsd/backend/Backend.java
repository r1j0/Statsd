package com.github.r1j0.statsd.backend;

import java.util.List;

import com.github.r1j0.statsd.server.StatsdConfiguration;

public interface Backend {

	boolean notify(List<String> messages);


	void setConfiguration(StatsdConfiguration statsdConfiguration);

}
