package com.letscode.lcg;

import java.util.Random;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		String hostname = args.length > 0 ? args[0] : "localhost";
		String nickname = args.length > 1 ? args[1] : "Player " + (new Random().nextInt(12415124));
		
		cfg.title = "Lambda Cipher Genesis - " + nickname;
		cfg.useGL20 = false;
		
		{
			cfg.width = 1024;
			cfg.height = 600;
		}
		new LwjglApplication(new LcgApp(hostname, nickname), cfg);
	}
}
