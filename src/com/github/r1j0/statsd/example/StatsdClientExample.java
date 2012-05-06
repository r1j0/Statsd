package com.github.r1j0.statsd.example;

import com.github.r1j0.statsd.client.StatsdClient;

public class StatsdClientExample {

	public static void main(String[] args) {
		StatsdClient statsdClient = new StatsdClient("localhost", 39390);
		statsdClient.send("system.loadavg_1min 0.3 " + System.currentTimeMillis() / 1000L + "\n" + "system.loadavg_5min 0.4 " + System.currentTimeMillis() / 1000L + "\n");
	}
}
