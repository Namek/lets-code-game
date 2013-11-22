package com.letscode.lcg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.letscode.lcg.network.EventsInterface;
import com.letscode.lcg.network.WebSocketClientInterface;
import com.letscode.lcg.network.WebSocketClientInterface.Listener;
import com.letscode.lcg.screens.ConnectingScreen;
import com.letscode.lcg.screens.ErrorScreen;
import com.letscode.lcg.ui.BaseScreen;
import com.letscode.lcg.ui.Styles;
import com.letscode.lcg.ui.UiApp;

public class LcgApp extends UiApp {
	WebSocketClientInterface networkImpl;
	String hostname;
	int port;
	
	// TODO would be better to have it in asset file
	public static final String DEFAULT_HOSTNAME = "178.32.225.209";
	public static final int DEFAULT_PORT = 34567;
	
	
	public LcgApp(WebSocketClientInterface networkImpl, String hostname, int port) {
		this.networkImpl = networkImpl;
		this.hostname = hostname;
		this.port = port;
	}
	
	@Override
	public void create()
	{
		super.create();
		
		inputs.addProcessor(new InputAdapter() {
			@Override
			public boolean keyDown(int keycode) {
				if (keycode == Keys.ESCAPE) {
					Gdx.app.exit();
				}
				
				return false;
			}
		});
				
		setClearColor(Color.LIGHT_GRAY);
	}

	@Override
	public void render() {
		EventsInterface.update();
		super.render();
	}

	@Override
	protected String atlasPath() {
		return "data/tex.atlas";
	}

	@Override
	protected String skinPath() {
		return null;
	}

	@Override
	protected void styleSkin(Skin skin, TextureAtlas atlas) {
		new Styles().styleSkin(skin, atlas);
	}

	@Override
	protected BaseScreen getFirstScreen() {
		final Context context = new Context(this, networkImpl);
		
		networkImpl.addListener(new Listener() {
			@Override
			public void onMessageReceived(String message) {	
			}
			
			@Override
			public void onError() {
				context.app.switchScreens(new ErrorScreen(context));
			}
			
			@Override
			public void onDisconnected() {
				context.app.switchScreens(new ErrorScreen(context));
			}
			
			@Override
			public void onConnected() {
			}
		});
		
		BaseScreen screen = new ConnectingScreen(context, hostname, port);
		return screen;
	}
}
