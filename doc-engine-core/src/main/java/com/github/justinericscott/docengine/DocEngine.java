/**
 * TODO: License
 */
package com.github.justinericscott.docengine;

import static com.github.justinericscott.docengine.util.Utils.Constants.*;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

/**
 * @author Justin Scott<br>
 *         TODO: Description
 */
@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class, HibernateJpaAutoConfiguration.class })
@PropertySources({ @PropertySource(value = PROPERTY_DEFAULT),
		@PropertySource(value = PROPERTY_CUSTOM, ignoreResourceNotFound = true) })
public class DocEngine {
	private static final Class<DocEngine> APPLICATION = DocEngine.class;
	private static final Logger LOG = LoggerFactory.getLogger(APPLICATION);
	private static volatile ConfigurableApplicationContext ctx;
	private static volatile AtomicBoolean running = new AtomicBoolean(false);

	public static void main() {
		DocEngine.main(null);
	}

	public static void main(final String[] args) {
		if (args != null && Arrays.asList(args).contains(ENGINE_CONTROL_STOP)) {
			stop();
		} else {
			start(args);
		}
	}

	public static synchronized boolean running() {
		return running.get();
	}

	private static void start(final String[] args) {
		if (!running()) {
			LOG.info("Starting Document Engine...");
			if (args != null) {
				ctx = SpringApplication.run(APPLICATION, args);
			} else {
				ctx = SpringApplication.run(APPLICATION, new String[] {});
			}
			if (ctx.isActive()) {
				running.set(true);
				LOG.info("Document Engine started.");
			}
		} else {
			LOG.info("Document Engine is already running!");
		}
	}

	private static synchronized void stop() {
		if (running()) {
			LOG.info("Shutting down the Document Engine...");
			final ExitCodeGenerator exit = new ExitCodeGenerator() {
				@Override
				public int getExitCode() {
					return 1;
				}
			};
			final int code = SpringApplication.exit(ctx, exit);
			if (code == 1) {
				running.set(false);
				ctx = null;
				LOG.info("Document Engine is shutdown.");
			}
		} else {
			LOG.info("Document Engine is not running!");
		}
	}
}