package com.github.r1j0.statsd.server;

import java.io.IOException;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sun.misc.Signal;
import sun.misc.SignalHandler;

import com.github.r1j0.statsd.configuration.StatsdConfiguration;

public class StatsdServer {

	private static final Logger log = LoggerFactory.getLogger(StatsdServer.class);

	private static LinkedBlockingQueue<String> linkedBlockingQueue = new LinkedBlockingQueue<String>();;
	private static StatsdConfiguration configuration = null;
	private static FlushThread flushThread = null;


	public static void main(String[] args) throws IOException {
		configuration = new StatsdConfiguration(args);

		Signal.handle(new Signal("HUP"), new SignalHandler() {

			public void handle(Signal signal) {
				log.info("SIGHUP received. Reloading configuration.");
				configuration = new StatsdConfiguration(null);
				
				flushThread.forceFlush(configuration);
			}
		});

		ServerThread serverThread = ServerFactory.getInstance(configuration.getNetworkFramework(), configuration, linkedBlockingQueue);
		serverThread.start();
		
		flushThread = new FlushThread(configuration, linkedBlockingQueue);
		flushThread.start();
	}
}
