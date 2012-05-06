package com.github.r1j0.statsd.client;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatsdClient {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private static final String KEY_SEPARATOR = ":";
	private static final String VALUE_SEPARATOR = "|";
	private static final String LINE_ENDING = "\n";

	private final String host;
	private final int port;


	public StatsdClient(String host, int port) {
		this.host = host;
		this.port = port;
	}


	public boolean increment(final String key) {
		return increment(key, 1, getTimeStamp());
	}


	public boolean increment(final String key, final int value) {
		return increment(key, value, getTimeStamp());
	}


	public boolean increment(final String key, final int value, final long timestamp) {
		return createMessage(key, value, timestamp);
	}


	public boolean decrement(final String key) {
		return decrement(key, -1, getTimeStamp());
	}


	public boolean decrement(final String key, final int value) {
		return decrement(key, value, getTimeStamp());
	}


	public boolean decrement(final String key, final int value, final long timestamp) {
		int negativeValue = value;

		if (value > 0) {
			negativeValue = -value;
		}

		return createMessage(key, negativeValue, timestamp);
	}


	private boolean createMessage(final String key, final int value, final long timestamp) {
		final StringBuilder message = new StringBuilder();
		message.append(key).append(KEY_SEPARATOR).append(value).append(VALUE_SEPARATOR).append(timestamp).append(LINE_ENDING);
		return send(message.toString());
	}


	private long getTimeStamp() {
		return System.currentTimeMillis() / 1000L;
	}


	public boolean send(final String message) {
		return send(message, true);
	}


	public boolean send(final String message, final boolean useUdp) {
		boolean result = false;

		if (useUdp) {
			result = sendUdp(message);
		} else {
			result = sendSocket(message);
		}

		return result;
	}


	private boolean sendSocket(final String message) {
		try {
			Socket socket = new Socket(host, port);
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


	private boolean sendUdp(final String message) {
		final DatagramChannel channel;

		try {
			channel = DatagramChannel.open();
			channel.connect(new InetSocketAddress(host, port));

			final byte[] messageBytes = message.getBytes("utf-8");
			final int messageLength = messageBytes.length;

			final ByteBuffer buffer = ByteBuffer.wrap(messageBytes);
			final int bytesWritten = channel.write(buffer);

			if (bytesWritten == messageLength) {
				log.info("Bytes written: " + messageLength + " Message: " + message);
			} else {
				log.warn("Bytes written: " + bytesWritten + " but message had: " + messageLength + " bytes.");
			}

			log.debug("MESSAGE:" + message);

			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
}
