package com.letscode.lcg;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class Main {
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Lambda Cipher Genesis";
		cfg.useGL20 = false;
		
		{
			cfg.width = 1024;
			cfg.height = 600;
		}
		new LwjglApplication(new LcgApp(), cfg);
	}
}
