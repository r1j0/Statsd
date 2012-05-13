package com.github.r1j0.statsd.server;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.r1j0.statsd.backend.Backend;
import com.github.r1j0.statsd.backend.BackendWorker;
import com.github.r1j0.statsd.configuration.StatsdConfiguration;
import com.github.r1j0.statsd.utils.ThreadUtility;

public class FlushThread extends Thread {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private StatsdConfiguration configuration;
	private final LinkedBlockingQueue<String> queue;
	private List<Backend> backends;
	private int flushInterval;
	private ExecutorService executor;


	public FlushThread(StatsdConfiguration configuration, LinkedBlockingQueue<String> linkedBlockingQueue) {
		super();

		this.configuration = configuration;
		this.queue = linkedBlockingQueue;
		this.backends = configuration.getBackends();
		this.flushInterval = configuration.getFlushInterval();
	}


	@Override
	public void run() {
		log.info("FlushThread started.");

		final int backendsSize = backends.size();
		executor = Executors.newFixedThreadPool(backendsSize);

		while (true) {
			queueInformation();

			List<String> messages = pollQueue();

			if (!messages.isEmpty()) {
				notifyBackends(executor, messages);
			}

			ThreadUtility.doSleep(flushInterval);
		}
	}


	public void forceFlush(StatsdConfiguration statsdConfiguration) {
		log.info("Forced flushing of messages.");

		ExecutorService oldExecutor = executor;
		executor = Executors.newFixedThreadPool(backends.size());

		queueInformation();

		List<String> messages = pollQueue();

		if (!messages.isEmpty()) {
			notifyBackends(oldExecutor, messages);
		}

		configuration = statsdConfiguration;
		backends = configuration.getBackends();
		flushInterval = configuration.getFlushInterval();
		oldExecutor.shutdown();

		log.info("Finished flushing.");
	}


	private List<String> pollQueue() {
		final List<String> messages = new ArrayList<String>();
		String message = "";

		while ((message = queue.poll()) != null) {
			log.info("Message taken from the queue: " + message);
			messages.add(message);
		}

		return messages;
	}


	private void notifyBackends(ExecutorService executor, List<String> messages) {
		final List<String> unmodifiableMessages = Collections.unmodifiableList(messages);

		for (Backend backend : backends) {
			log.info("Notifying backend: " + backend.getClass().getSimpleName());

			final Runnable backendWorker = new BackendWorker(backend, unmodifiableMessages);
			executor.execute(backendWorker);
		}

		log.info("Finished backend threads");
	}


	private void queueInformation() {
		if (!queue.isEmpty()) {
			log.info("Queue size is: " + queue.size());
		} else {
			log.info("Queue is empty");
		}
	}
}
