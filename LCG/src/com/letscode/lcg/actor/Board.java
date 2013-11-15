package com.letscode.lcg.actor;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
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
		
		int rowsCount = map.getHeight();
		int colsCount = map.getWidth();
		fields = new FieldActor[rowsCount][colsCount];
		
		
		for (int row = rowsCount - 1; row >= 0; --row) {
			for (int col = 0; col < colsCount; ++col) {
				Field fieldInfo = map.getField(row, col);
				FieldActor fieldActor = fields[row][col] = new FieldActor(context, fieldInfo, row, col);
				
				this.addActor(fieldActor);
			}
		}
		updateSizes();
		
		addListener(boardClickListener);
	}
	
	
	@Override
	public void sizeChanged() {
		updateSizes();
	}
	
	private void updateSizes() {
		int rowsCount = map.getHeight();
		int colsCount = map.getWidth();
		
		float totalWidth = getWidth() * getScaleX();
		float totalHeight = getHeight() * getScaleY();
		float fieldWidth = totalWidth / (float)(colsCount%2 == 0 ? colsCount/2 : colsCount/2+1);
		float fieldHeight = totalHeight / (float)(rowsCount%2 == 0 ? rowsCount/2 : rowsCount/2+1);
		
		float x;
		float y = 0;
		
		for (int row = rowsCount - 1; row >= 0; --row) {
			x = 0;
			for (int col = 0; col < colsCount; ++col) {
//				Field fieldInfo = map.getField(row, col);
				FieldActor fieldActor = fields[row][col];
				
				fieldActor.setSize(fieldWidth, fieldHeight);
				fieldActor.setPosition(x, y);
				
				x += fieldWidth/2;
			}
			y += fieldHeight;
		}
	}
	
	private ClickListener boardClickListener = new ClickListener() {

		@Override
		public boolean isOver(Actor actor, float x, float y) {
			if (actor instanceof FieldActor) {
				FieldActor fieldActor = (FieldActor)actor; 
				fieldActor.isHovered = true;
				
				if (hoveredFieldActor != null) {
					fieldActor.isHovered = false;
				}
				hoveredFieldActor = fieldActor;
			}
			return super.isOver(actor, x, y);
		}
		
		
		
	};
}
