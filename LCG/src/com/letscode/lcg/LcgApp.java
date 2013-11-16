package com.letscode.lcg;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.letscode.lcg.screens.ConnectingScreen;
import com.letscode.ui.BaseScreen;
import com.letscode.ui.Styles;
import com.letscode.ui.UiApp;

public class LcgApp extends UiApp {
	String hostname;
	int port;
	String nickname;
	
	public LcgApp(String hostname, int port, String nickname) {
		this.hostname = hostname;
		this.port = port;
		this.nickname = nickname;
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
		Context context = new Context(this);
		
		return new ConnectingScreen(context, nickname, hostname, port);
	}
}
