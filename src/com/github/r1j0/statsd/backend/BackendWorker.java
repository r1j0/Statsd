package com.github.r1j0.statsd.backend;

import java.util.List;

public class BackendWorker implements Runnable {

	private final Backend backend;
	private final List<String> messages;


	public BackendWorker(Backend backend, List<String> messages) {
		this.backend = backend;
		this.messages = messages;
	}


	public void run() {
		backend.notify(messages);
	}
}
