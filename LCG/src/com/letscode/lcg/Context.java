package com.letscode.lcg;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.letscode.lcg.enums.BuildMode;
import com.letscode.lcg.model.Map;
import com.letscode.lcg.network.NetworkComponent;
import com.letscode.ui.UiApp;

public class Context {
	public UiApp app;
	public ShapeRenderer shapeRenderer;
	public BuildMode currentBuildMode = BuildMode.None;
	public Map map;
	public NetworkComponent network = new NetworkComponent();
	
	public Context(UiApp app) {
		this.app = app;
		shapeRenderer = new ShapeRenderer(); 
	}
}
