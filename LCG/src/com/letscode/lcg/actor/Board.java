package com.letscode.lcg.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.letscode.lcg.Context;
import com.letscode.lcg.model.Field;
import com.letscode.lcg.model.Map;

public class Board extends Group {
	Context context;
	Map map;
	FieldActor[][] fields;
	
	FieldActor hoveredFieldActor;//currently hovered. May be none (null)
	
	
	public Board(Context context) {
		this.context = context;
		this.map = context.map;
	}

	public void init() {
		int rowsCount = map.getHeight();
		int colsCount = map.getWidth();
		fields = new FieldActor[rowsCount][colsCount];
		
		for (int row = 0; row < rowsCount; ++row) {
			for (int col = 0; col < colsCount; ++col) {
				Field fieldInfo = map.getField(row, col);
				FieldActor fieldActor = fields[row][col] = new FieldActor(context, fieldInfo, row, col);
				
				this.addActor(fieldActor);
			}
		}
		updateSizes();
	}
	
	
	@Override
	public void sizeChanged() {
		updateSizes();
	}
	
	private void updateSizes() {
		if (fields == null)
			return;
		
		int rowsCount = map.getHeight();
		int colsCount = map.getWidth();
		
		float totalWidth = getWidth() * getScaleX();
		float totalHeight = getHeight() * getScaleY();
		float fieldWidth = totalWidth / (float)(colsCount%2 == 0 ? colsCount/2+0.5f : colsCount/2+1);
		float fieldHeight = totalHeight / (float)rowsCount;
		
		float x;
		float y = getY();
		
		for (int row = rowsCount - 1; row >= 0; --row) {
			x = 0;
			for (int col = 0; col < colsCount; ++col) {
//				Field fieldInfo = map.getField(row, col);
				FieldActor fieldActor = fields[row][col];
				
				fieldActor.setPosition(x, y);
				fieldActor.setSize(fieldWidth, fieldHeight);
				fieldActor.sizeChanged();
				
				x += fieldWidth / 2f;
			}
			y += fieldHeight;
		}
	}
	
	@Override
	public void act(float delta) {
		super.act(delta);
		
	}

	@Override
	public Actor hit(float x, float y, boolean touchable) {
		Actor actor = super.hit(x, y, touchable);
		
		if (actor != null) {
			if (actor instanceof FieldActor) {
				FieldActor fieldActor = (FieldActor)actor; 
				fieldActor.isHovered = true;
				
				if (fieldActor != hoveredFieldActor) {
					if (hoveredFieldActor != null) {
						hoveredFieldActor.isHovered = false;
					}
					hoveredFieldActor = fieldActor;
				}
			}
		}
		else if (hoveredFieldActor != null) {
			hoveredFieldActor.isHovered = false;
			hoveredFieldActor = null;
		}
		
		return actor;
	}
}
