package com.github.r1j0.statsd.backend;

import java.util.List;

public interface Backend {

	boolean notify(List<String> messages);


	BackendConfiguration getConfiguration();

}
