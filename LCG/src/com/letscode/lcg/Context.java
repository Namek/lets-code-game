package com.letscode.lcg;

import java.util.HashMap;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letscode.lcg.enums.BuildMode;
import com.letscode.lcg.model.Map;
import com.letscode.lcg.network.WebSocketClientInterface;
import com.letscode.lcg.network.NetworkComponent;
import com.letscode.lcg.ui.UiApp;

public class Context {
	public UiApp app;
	public ShapeRenderer shapeRenderer;
	public BuildMode currentBuildMode = BuildMode.None;
	public Map map;
	public NetworkComponent network;
	public Color[] colors = new Color[] {Color.WHITE, Color.ORANGE, Color.BLUE, Color.RED,
									   Color.GREEN, Color.CYAN, Color.MAGENTA,
									   Color.YELLOW};
	public HashMap<String, Color> colorsForPlayers = new HashMap<String, Color>();
	
	public Context(UiApp app, WebSocketClientInterface networkImpl) {
		this.app = app;
		shapeRenderer = new ShapeRenderer();
		colorsForPlayers.put(null, Color.WHITE);
		network = new NetworkComponent(networkImpl);
	}

	public String getPlayerNickname() {
		return network.getClientNickname();
	}
}
