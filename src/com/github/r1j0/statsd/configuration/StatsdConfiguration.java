package com.github.r1j0.statsd.configuration;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.r1j0.statsd.backend.Backend;
import com.github.r1j0.statsd.backend.BackendConfiguration;
import com.github.r1j0.statsd.server.StatsdServer;

public class StatsdConfiguration {

	private static final String SERVER_DEBUG = "server.debug";
	private static final String SERVER_LISTEN_PORT = "server.listen_port";
	private static final String SERVER_FLUSH_INTERVAL = "server.flush_interval";
	private static final String BACKEND_USE = "backend.use";
	private static final String NETWORK_FRAMEWORK = "server.framework";

	private final Logger log = LoggerFactory.getLogger(getClass());

	private Options options;
	private Properties properties = new Properties();
	private List<Backend> backends = new ArrayList<Backend>();


	public StatsdConfiguration(String[] args) {
		options = OptionsBuilder.build();
		CommandLineParser parser = new PosixParser();
		CommandLine line = null;

		try {
			line = parser.parse(options, args);
			parseCommandLineOptions(line);
		} catch (org.apache.commons.cli.ParseException e) {
			System.err.println(e.getMessage());
			printHelp(options);
			System.exit(1);
		}
	}


	public String getValue(String key) {
		return properties.getProperty(key);
	}


	public boolean isDebugEnabled() {
		return Boolean.parseBoolean(getValue(SERVER_DEBUG));
	}


	public int getListeningPort() {
		return Integer.parseInt(getValue(SERVER_LISTEN_PORT));
	}


	public int getFlushInterval() {
		return Integer.parseInt(getValue(SERVER_FLUSH_INTERVAL));
	}


	public List<Backend> getBackends() {
		return backends;
	}


	public String getNetworkFramework() {
		return getValue(NETWORK_FRAMEWORK);
	}


	private void parseCommandLineOptions(final CommandLine line) {
		if (line.hasOption("h")) {
			printHelp(options);
			System.exit(0);
		}

		if (line.hasOption("version")) {
			log.info("Version is and will be infinite.");
			System.exit(0);
		}

		String propertiesFile = line.getOptionValue("c", "resources/statsd.properties");

		try {
			properties.load(new FileInputStream(propertiesFile));
		} catch (FileNotFoundException e) {
			log.error("File not found: " + propertiesFile);
			System.exit(1);
		} catch (IOException e) {
			log.error("Unable to read file: " + propertiesFile);
			System.exit(1);
		}

		if (line.hasOption("debug")) {
			properties.setProperty(SERVER_DEBUG, line.getOptionValue("debug"));
		}

		if (line.hasOption("p")) {
			properties.setProperty(SERVER_LISTEN_PORT, line.getOptionValue("p"));
		}

		initializeBackends();
	}


	private void initializeBackends() {
		String[] backendsToUse = properties.getProperty(BACKEND_USE).split(",");

		Map<String, String> backendTypes = new HashMap<String, String>();
		Set<Entry<Object, Object>> entrySet = properties.entrySet();

		for (Entry<Object, Object> entry : entrySet) {
			final String key = (String) entry.getKey();

			if (!key.equals(BACKEND_USE) && key.matches("backend.*")) {
				String identifier = key.substring(key.indexOf('.') + 1, key.lastIndexOf('.')).trim().toLowerCase();

				if (key.endsWith("type")) {
					backendTypes.put(identifier, getValue("backend." + identifier + ".type"));
				}
			}
		}

		if (isDebugEnabled()) {
			backendsToUse = new String[] { "debug" };
		}

		for (String backendToUse : backendsToUse) {
			final String backendIdentifier = backendToUse.trim();
			final String backendType = backendTypes.get(backendIdentifier);

			if (backendType != null) {
				try {
					Backend backend = (Backend) Class.forName("com.github.r1j0.statsd.backend." + StringUtils.capitalize(backendType) + "Backend").newInstance();
					BackendConfiguration configuration = backend.getConfiguration();
					configuration.setIdentifier(backendIdentifier);
					configuration.setConfiguration(this);
					backends.add(backend);

					log.info("Added backend: " + backendIdentifier + " as type " + backend.getClass().getSimpleName() + ".");
				} catch (Exception e) {
					e.printStackTrace();
					log.error(e.getMessage());
				}
			} else {
				log.error("Could not find a type for backend identifier: " + backendToUse);
			}
		}
	}


	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		formatter.printHelp(StatsdServer.class.getSimpleName(), options, true);
	}
}
