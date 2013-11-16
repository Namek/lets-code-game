package com.letscode.lcg.actor;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;

import static com.badlogic.gdx.scenes.scene2d.actions.Actions.*;

import com.letscode.lcg.Assets;
import com.letscode.lcg.Context;
import com.letscode.lcg.model.Field;
import com.letscode.lcg.model.Map;



public class FieldActor extends Actor {
	Context context;
	
	// logic data
	Field field;
	int rowIndex;
	int colIndex;
	boolean isTriangleUpper;
	
		
	// render info
	public static final Vector2 tmpPos = new Vector2();
	Vector2 leftPoint = new Vector2();
	Vector2 rightPoint = new Vector2();
	Vector2 centerPoint = new Vector2();
	boolean isHovered = false;

		
	public FieldActor(Context context, Field field, int rowIndex, int colIndex) {
		this.context = context;
		this.field = field;
		this.rowIndex = rowIndex;
		this.colIndex = colIndex;
		this.isTriangleUpper = Map.isUpperTriangle(rowIndex, colIndex);
	}
	
	public Field getField() {
		return field;
	}

	public int getRowIndex() {
		return rowIndex;
	}
	
	public int getColIndex() {
		return colIndex;
	}

	@Override
	public void sizeChanged() {
		float w = getWidth() * getScaleX();
		float h = getHeight() * getScaleY();
		
		if (isTriangleUpper) {
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
		sizeChanged();
		localToStageCoordinates(tmpPos.set(0, 0));
		
		float x = tmpPos.x;
		float y = tmpPos.y;
		
		if (field == null)
			return;

		batch.end();
		
		Color fillColor = isHovered ? Color.ORANGE : context.colorsForPlayers.get(field.owner);
		shapeRenderer.begin(ShapeType.Filled);
		shapeRenderer.setColor(fillColor);
		shapeRenderer.triangle(x + leftPoint.x, y + leftPoint.y, x + centerPoint.x, y + centerPoint.y, x + rightPoint.x, y + rightPoint.y);
		shapeRenderer.end();
		
		shapeRenderer.begin(ShapeType.Line);
		shapeRenderer.setColor(Color.BLACK);
		shapeRenderer.triangle(x + leftPoint.x, y + leftPoint.y, x + centerPoint.x, y + centerPoint.y, x + rightPoint.x, y + rightPoint.y);
		shapeRenderer.end();

		batch.begin();
		
		if (field.type.equals(Field.TYPE_NORMAL)) {
			if (field.building == null) {
				
			}
			else if (field.building.equals(Field.BUILDING_TOWNHALL)) {
				drawTextureOnField(Assets.townhallTexture, batch);
			}
			else if (field.building.equals(Field.BUILDING_GOLDMINE)) {
				drawTextureOnField(Assets.goldmineTexture, batch);
			}
			else if (field.building.equals(Field.BUILDING_BARRICADE)) {
				drawTextureOnField(Assets.barricadeTexture, batch);
			}
		}
		else if (field.type.equals(Field.TYPE_GOLD)) {
			if (field.building == null) {
				drawTextureOnField(Assets.goldTexture, batch);
			}
			else if (field.building.equals(Field.BUILDING_GOLDMINE)) {
				drawTextureOnField(Assets.goldmineTexture, batch);
			}
		}
	}
	
	private void drawTextureOnField(TextureRegion texture, SpriteBatch batch) {
		float tw = texture.getRegionWidth();
		float th = texture.getRegionHeight();
		
		float w = getWidth() * getScaleX();
		float h = getHeight() * getScaleY();
		
		boolean isTextureBigger = tw*th > w*h;
		
		float scaleX = isTextureBigger ? w/tw : tw/w;
		float scaleY = isTextureBigger ? h/th : th/h;
		float scale = Math.min(scaleX, scaleY);
		
		if (isHovered) {
			scale *= 1.4f;
		}
		
		float centerX = (leftPoint.x + rightPoint.x)/2 - tw/2*scale;
		float centerY = (leftPoint.y + centerPoint.y)/2 - (isTriangleUpper ? th/2 : th/3)*scale;
		localToParentCoordinates(tmpPos.set(centerX, centerY));
		
		float x = tmpPos.x;
		float y = tmpPos.y;
		
		batch.setColor(Color.WHITE);
		batch.draw(texture, x, y, 0, 0, tw, th, scale, scale, getRotation());
	}
	
	

	@Override
	public void act(float delta) {
		super.act(delta);
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		if (super.hit(x, y, touchable) != null) {
			tmpPos.set(x, y);
			if (Intersector.isPointInTriangle(tmpPos, leftPoint, centerPoint, rightPoint)) 
				return this;
		}
		return null;
	}
	
	public void animateTouched() {
		if (field == null)
			return;
		
		int baseDirection = isTriangleUpper ? 1 : -1;
		float displacement = baseDirection * getHeight() * getScaleY() * 0.3f;
		float time = 0.2f;
		
		toFront();
		addAction(
			sequence(
				moveBy(0, displacement, time/2, Interpolation.sineOut),
				moveBy(0, -displacement, time/2, Interpolation.sineIn)
			)
		);
	}
}
