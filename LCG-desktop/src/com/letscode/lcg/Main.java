package com.letscode.lcg;

import java.util.Random;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		String hostname = args.length > 0 ? args[0] : LcgApp.DEFAULT_HOSTNAME;
		int port = args.length > 1 ? Integer.parseInt(args[1]) : LcgApp.DEFAULT_PORT;
		
		final String gameTitle = "Lambda Cipher Genesis";
		
		cfg.title = gameTitle;
		cfg.useGL20 = false;
		cfg.width = 1024;
		cfg.height = 600;

		new LwjglApplication(new LcgApp(new WebSocketJavaClient(), hostname, port), cfg);
	}
}
