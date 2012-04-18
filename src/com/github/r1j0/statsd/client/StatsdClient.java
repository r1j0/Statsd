package com.github.r1j0.statsd.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StatsdClient {

	private final Logger log = LoggerFactory.getLogger(getClass());

	private final String host;
	private final int port;


	public StatsdClient(String host, int port) {
		this.host = host;
		this.port = port;
	}


	/**
	 * bucket:value|type@sample RAND
	 */
	public boolean send(final String bucket, final String value, final String type, final String sample, final String random) {
		final DatagramChannel channel;

		try {
			channel = DatagramChannel.open();
			channel.connect(new InetSocketAddress(host, port));

			final StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append(bucket).append(":").append(value).append("|").append(type).append("@").append(sample).append(" ").append(random).append("\n");
			final String message = stringBuilder.toString();

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
