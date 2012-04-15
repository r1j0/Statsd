package com.github.r1j0.statsd.server;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

public class StatsdServer {

	private static LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<String>();;

	public static void main(String[] args) throws IOException {
		final StatsdConfiguration configuration = new StatsdConfiguration(args);

		new ServerThread(configuration, linkedBlockingQueue).start();
		new FlushThread(configuration, linkedBlockingQueue).start();
	}
}
