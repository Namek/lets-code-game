package com.letscode.lcg.actor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.letscode.lcg.model.Field;
import com.letscode.lcg.model.Map;

public class FieldActor extends Actor {
	// logic data
	Field field;
	int rowIndex;
	int colIndex;
	boolean isUpperTriangle;
		
		// render info
	public static final Vector2 tmpPos = new Vector2();
	Vector2 leftPoint = new Vector2();
	Vector2 rightPoint = new Vector2();
	Vector2 centerPoint = new Vector2();

		
	public FieldActor(Field field, int rowIndex, int colIndex) {
		this.field = field;
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
		this.isUpperTriangle = Map.isUpperTriangle(rowIndex, colIndex);
	}
		
	@Override
	public void sizeChanged() {
		float w = getWidth() * getScaleX();
		float h = getHeight() * getScaleY();
		
		if (isUpperTriangle) {
			leftPoint.set(0, 0);
			centerPoint.set(w/2, h);
			rightPoint.set(w, 0);
		}
		else {
			leftPoint.set(0, h);
			centerPoint.set(w/2, 0);
			rightPoint.set(w, h);
		}
	}
		
	@Override
	public void draw(SpriteBatch batch, float parentAlpha) {
	
		
	}
}
