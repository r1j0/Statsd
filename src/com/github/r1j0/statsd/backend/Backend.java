package com.github.r1j0.statsd.backend;

import java.util.List;

public interface Backend {

	boolean send(List<String> messages);

}
