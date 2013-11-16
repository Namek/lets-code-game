package com.letscode.lcg;

import java.util.Random;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		String hostname = args.length > 0 ? args[0] : "localhost";
		int port = args.length > 1 ? Integer.parseInt(args[1]) : 80;
		String nickname = args.length > 2 ? args[2] : "Player " + (new Random().nextInt(12415124));
		
		cfg.title = "Lambda Cipher Genesis - " + nickname;
		cfg.useGL20 = false;
		
		{
			cfg.width = 1024;
			cfg.height = 600;
		}
		new LwjglApplication(new LcgApp(hostname, port, nickname), cfg);
	}
}
