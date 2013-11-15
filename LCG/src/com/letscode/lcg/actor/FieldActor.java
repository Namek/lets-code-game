package com.letscode.lcg.actor;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.letscode.lcg.Context;
import com.letscode.lcg.model.Field;
import com.letscode.lcg.model.Map;

public class FieldActor extends Actor {
	Context context;
	
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
	boolean isHovered;

		
	public FieldActor(Context context, Field field, int rowIndex, int colIndex) {
		this.context = context;
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
		ShapeRenderer shapeRenderer = context.shapeRenderer;
		
		float x = getX();
		float y = getY();
		
		if (field == null)
			return;
		
		
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(context.colorsForPlayers.get(field.owner));
		shapeRenderer.triangle(x + leftPoint.x, y + leftPoint.y, x + centerPoint.x, y + centerPoint.y, x + rightPoint.x, y + rightPoint.y);
		shapeRenderer.end();
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		localToStageCoordinates(tmpPos.set(x, y));
		return touchable && Intersector.isPointInTriangle(tmpPos, leftPoint, centerPoint, rightPoint) ? this : null;
	}
	
	
}
