/**
 * TODO: License
 */
package com.itgfirm.docengine;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.itgfirm.docengine.util.Constants;

/**
 * @author Justin Scott
 * TODO: Description
 */
@SpringBootApplication
@PropertySources({
	@PropertySource( value = Constants.PROPERTY_DEFAULT ),
	@PropertySource( value = Constants.PROPERTY_CUSTOM, ignoreResourceNotFound = true)
})
public class DocEngine {
	private static final Class<DocEngine> app = DocEngine.class;
	private static final Logger LOG = LogManager.getLogger(app);
	private static volatile ConfigurableApplicationContext ctx;
	private static volatile AtomicBoolean running = new AtomicBoolean(false);
	
	public static void main() {
		DocEngine.main(null);
	}
	
	public static synchronized void main(String[] args) {
		if(args != null) {
			if (Arrays.asList(args).contains("stop")) {
				stop();
			} else {
				start(args);
			}
		} else {
			args = new String[]{};
			start(args);
		}
	}
	
	public static synchronized boolean running() {
		return running.get();
	}	
	
	public static synchronized int stop() {
		if (running()) {
			ExitCodeGenerator exit = new ExitCodeGenerator() {
				@Override public int getExitCode() { return 1; }
			};
			int code = SpringApplication.exit(ctx, exit);
			running.set(false);
			ctx = null;
			return code;
		} else {
			ctx = null;
			LOG.debug("Document Engine Is Not Running.");
			return 0;
		}
	}
	
	private static void start(String[] args) {
		if (!running()) {
			LOG.debug("Starting Document Engine...");
			ctx = SpringApplication.run(app, args);
			if (ctx.isActive()) {
				running.set(true);
				LOG.debug("Document Engine Started...");
			}
		} else {
			LOG.info("Document Engine Is Already Running.");
		}
	}
}