package com.github.r1j0.statsd.server;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;

public class OptionsBuilder {

	public static Options build() {
		Options options = new Options();
		addHelpOption(options);
		addVersionOption(options);
		addPropertiesOption(options);
		addDebugOption(options);
		
		return options;
	}


	private static void addPropertiesOption(Options options) {
		OptionBuilder.withDescription("location of the configuration file");
		OptionBuilder.hasArg();
		OptionBuilder.withArgName("config");
		options.addOption(OptionBuilder.create("c"));
	}


	private static void addDebugOption(Options options) {
		OptionBuilder.withDescription("enables debug mode.");
		OptionBuilder.withArgName("debug");
		options.addOption(OptionBuilder.create("debug"));
	}


	private static void addVersionOption(Options options) {
		options.addOption(new Option("version", "print the version information and exit"));
	}


	private static void addHelpOption(Options options) {
		options.addOption(new Option("h", "help", false, "print this message"));
	}
}
