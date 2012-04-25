package com.github.r1j0.statsd.client;

import java.io.IOException;
import java.net.InetSocketAddress;
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
		return increment(key, 1, 1.0);
	}


	public boolean increment(final String key, final int value) {
		return increment(key, value, 1.0);
	}


	public boolean increment(final String key, final int value, final double unixTimestamp) {
		return createMessage(key, value, unixTimestamp);
	}


	public boolean decrement(final String key) {
		return decrement(key, -1, 1.0);
	}


	public boolean decrement(final String key, final int value) {
		return decrement(key, value, 1.0);
	}


	public boolean decrement(final String key, final int value, final double unixTimestamp) {
		int negativeValue = value;

		if (value > 0) {
			negativeValue = -value;
		}

		return createMessage(key, negativeValue, unixTimestamp);
	}


	private boolean createMessage(final String key, final int value, final double unixTimestamp) {
		final StringBuilder message = new StringBuilder();
		message.append(key).append(KEY_SEPARATOR).append(value).append(VALUE_SEPARATOR).append(unixTimestamp).append(LINE_ENDING);
		return send(message.toString());
	}


	public boolean send(final String message) {
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

			channel.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}
}
