package com.github.r1j0.statsd.server;

import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FlushThread extends Thread {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	private static final int FLUSH_INTERVALL = 5000;
	private final LinkedBlockingQueue<String> queue;


	public FlushThread(LinkedBlockingQueue<String> linkedBlockingQueue) {
		super();

		queue = linkedBlockingQueue;
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
			}

			try {
				Thread.sleep(FLUSH_INTERVALL);
			} catch (InterruptedException e) {
				// Sleeping time is over, go back to work
			}
		}
	}
}
