package com.github.r1j0.statsd.server;

import java.util.concurrent.LinkedBlockingQueue;

import com.github.r1j0.statsd.configuration.StatsdConfiguration;
import com.github.r1j0.statsd.server.framework.ServerThreadMina;
import com.github.r1j0.statsd.server.framework.ServerThreadNetty;

public class ServerFactory {

	public static ServerThread getInstance(String networkFramework, StatsdConfiguration configuration, LinkedBlockingQueue<String> linkedBlockingQueue) {
		final String framework = networkFramework.toLowerCase();

		if (framework.equals("netty")) {
			return new ServerThreadNetty(configuration, linkedBlockingQueue);
		} else if (framework.equals("mina")) {
			return new ServerThreadMina(configuration, linkedBlockingQueue);
		}

		throw new IllegalArgumentException("Network framework: " + networkFramework + " could not be initialized.");
	}
}
