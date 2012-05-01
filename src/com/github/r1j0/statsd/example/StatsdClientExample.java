package com.github.r1j0.statsd.example;

import com.github.r1j0.statsd.client.StatsdClient;

public class StatsdClientExample {

	public static void main(String[] args) {
//		StatsdClient statsdClient = new StatsdClient("192.168.1.21", 2003);
//		statsdClient.send("system.loadavg_1min 0.2 " + System.currentTimeMillis() / 1000L + "\n" + "system.loadavg_5min 0.7 " + System.currentTimeMillis() / 1000L + "\n");
		
		StatsdClient statsdClient = new StatsdClient("192.168.1.21", 2003);
		statsdClient.increment("my.bucket.test", 1);
		statsdClient.increment("my.bucket.test", 1);
		statsdClient.increment("my.bucket.test", 1);

		statsdClient.decrement("my.bucket.test", 1);
		statsdClient.decrement("my.bucket.test", 5);
	}
}
