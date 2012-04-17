package com.github.r1j0.statsd.utils;

public class ThreadUtility {

	public static void doSleep(long milliseconds) {
		try {
			Thread.sleep(milliseconds);
		} catch (InterruptedException e) {
			// sleep interrupted
		}
	}

}
