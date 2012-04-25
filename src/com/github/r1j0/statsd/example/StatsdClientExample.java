package com.github.r1j0.statsd.example;

import com.github.r1j0.statsd.client.StatsdClient;

public class StatsdClientExample {

	public static void main(String[] args) {
		StatsdClient statsdClient = new StatsdClient("127.0.0.1", 39390);
		statsdClient.increment("my.bucket.test", 1);
		statsdClient.increment("my.bucket.test", 1);
		statsdClient.increment("my.bucket.test", 1);

		statsdClient.decrement("my.bucket.test", 1);
		statsdClient.decrement("my.bucket.test", 5);
	}
}
