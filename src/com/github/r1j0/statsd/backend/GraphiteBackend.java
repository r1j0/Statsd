package com.github.r1j0.statsd.backend;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.r1j0.statsd.configuration.GraphiteBackendConfiguration;

public class GraphiteBackend implements Backend {

	private final Logger log = LoggerFactory.getLogger(getClass());
	private final GraphiteBackendConfiguration configuration;


	public GraphiteBackend() {
		this.configuration = new GraphiteBackendConfiguration();
	}


	public boolean notify(List<String> messages) {
		log.info(getClass().getSimpleName() + " received messages: " + messages.toString());
		final String message = createMessage(messages);
		return doSend(message);
	}


	public BackendConfiguration getConfiguration() {
		return configuration;
	}


	private String createMessage(final List<String> messages) {
		StringBuilder stringBuilder = new StringBuilder();

		for (String message : messages) {
			stringBuilder.append(message);
		}

		return stringBuilder.toString();
	}


	private boolean doSend(final String message) {
		try {
			Socket socket = new Socket(configuration.getHost(), configuration.getPort());
			Writer writer = new OutputStreamWriter(socket.getOutputStream());
			writer.write(message);
			writer.flush();
			writer.close();
			socket.close();
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

		return true;
	}
}