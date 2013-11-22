package com.letscode.lcg.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.google.gwt.core.shared.GWT;
import com.letscode.lcg.LcgApp;
import com.letscode.lcg.network.WebSocketClientInterface;

public class GwtLauncher extends GwtApplication {
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(1024, 600);
		cfg.antialiasing = true;
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () {
		WebSocketClientInterface networkImpl = new GwtWebSocketClient();
		return new LcgApp(networkImpl, LcgApp.DEFAULT_HOSTNAME, LcgApp.DEFAULT_PORT);
	}

	@Override
	public void log(String tag, String message) {
		super.log(tag, message);
		GWT.log(message);
		consoleLog(message);
	}

	@Override
	public void error(String tag, String message) {
		super.error(tag, message);
		GWT.log(message);
		consoleLog(message);
	}

	@Override
	public void error(String tag, String message, Throwable exception) {
		super.error(tag, message, exception);
		GWT.log(message);
		GWT.log(getStackTrace(exception));
		consoleLog(message + exception.toString());
		consoleLog(getStackTrace(exception));
	}

	@Override
	public void debug(String tag, String message) {
		super.debug(tag, message);
		GWT.log(message);
		consoleLog(message);
	}

	@Override
	public void debug(String tag, String message, Throwable exception) {
		super.debug(tag, message, exception);
		GWT.log(message);
		GWT.log(getStackTrace(exception));
		consoleLog(message + exception.toString());
		consoleLog(getStackTrace(exception));
	}
	

	private String getStackTrace (Throwable e) {
		StringBuffer buffer = new StringBuffer();
		for (StackTraceElement trace : e.getStackTrace()) {
			buffer.append(trace.toString() + "\n");
		}
		return buffer.toString();
	}

}