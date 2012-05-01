package com.github.r1j0.statsd.backend;

import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphiteBackend implements Backend {

	private final Logger log = LoggerFactory.getLogger(getClass());


	public boolean notify(List<String> messages) {
		log.info(getClass().getSimpleName() + " received messages: " + messages.toString());
		final String message = createMessage(messages);
		return doSend(message);
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
			Socket socket = new Socket("192.168.1.21", 2003);
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