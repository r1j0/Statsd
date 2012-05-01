package com.github.r1j0.statsd.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;

import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoConnector;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.textline.TextLineCodecFactory;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioDatagramConnector;

public class StatsdTestClient {

	private static final String HOST = "192.168.1.21";
	private static final int PORT = 2003;


	public static void main(String[] args) throws IOException, InterruptedException {
		StatsdClient statsdClient = new StatsdClient("192.168.1.21", 2003);

		while (true) {
			String message = "system.loadavg_1min 0.60 " + System.currentTimeMillis() / 1000L + "\nsystem.loadavg_5min 0.80 " + System.currentTimeMillis() / 1000L + "\nsystem.loadavg_15min 0.50 " + System.currentTimeMillis() / 1000L + "\n";

			for (int i = 0; i < 1; i++) {
				statsdClient.send(message);
//				new Automatic().start();
			}

			Thread.sleep(2000);
		}

		// System.exit(0);
	}


	public static class Automatic extends Thread {

		public Automatic() {
			super();
		}


		@Override
		public void run() {
			IoConnector connector = new NioDatagramConnector();
			connector.getSessionConfig().setReadBufferSize(2048);

			connector.getFilterChain().addLast("logger", new LoggingFilter());
			connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new TextLineCodecFactory(Charset.forName("UTF-8"))));

			String message = "\nsystem.loadavg_1min 0.60 " + System.currentTimeMillis() / 1000L + "\nsystem.loadavg_5min 0.80 " + System.currentTimeMillis() / 1000L + "\nsystem.loadavg_15min 0.50 " + System.currentTimeMillis() / 1000L + "\n\n";
			System.out.println("MESSAGE:" + message);
			connector.setHandler(new StatsdTestClientHandler(message));
			// connector.setHandler(new
			// StatsdTestClientHandler("bucket:value|type@sample RAND: " + new
			// Random().nextInt()));
			ConnectFuture future = connector.connect(new InetSocketAddress(HOST, PORT));

			if (!future.isConnected()) {
				return;
			}

			IoSession session = future.getSession();
			session.getConfig().setUseReadOperation(true);
			session.close(true);
			connector.dispose();
		}
	}
}
