package com.github.r1j0.statsd.backend;

public interface Backend {

	String getName();

	boolean send(String message);

}
