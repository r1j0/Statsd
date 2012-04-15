package com.github.r1j0.statsd.server;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.r1j0.statsd.backend.Backend;

public class FlushThread extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private final int flushIntervall;
	private final List<Backend> backends;
	private final LinkedBlockingQueue<String> queue;


	public FlushThread(StatsdConfiguration configuration, LinkedBlockingQueue<String> linkedBlockingQueue) {
		super();

		queue = linkedBlockingQueue;
		flushIntervall = configuration.getFlushIntervall();
		backends = configuration.getBackends();
	}


	@Override
	public void run() {
		logger.info("WorkerThread started.");

		while (true) {
			if (!queue.isEmpty()) {
				logger.info("Queue size is: " + queue.size());
			} else {
				logger.info("Queue is empty");
			}

			String message = "";

			while ((message = queue.poll()) != null) {
				logger.info("Message taken from the queue: " + message);
				
				for (Backend backend : backends) {
					backend.send(message);
					logger.info("Message send to backend: " + backend.getName());
				}
			}

			try {
				Thread.sleep(flushIntervall);
			} catch (InterruptedException e) {
				// Sleeping time is over, go back to work
			}
		}
	}
}
