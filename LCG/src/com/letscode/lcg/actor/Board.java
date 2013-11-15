package com.letscode.lcg.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.letscode.lcg.model.Map;

public class Board extends Group {
	Map map;
	
	
	public Board(Map map) {
		this.map = map;
	}
}
