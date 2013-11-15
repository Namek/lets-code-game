package com.letscode.lcg.actor;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.letscode.lcg.model.Field;
import com.letscode.lcg.model.Map;

public class Board extends Group {
	Map map;
	FieldActor[][] fields;
	
	
	public Board(Map map) {
		this.map = map;
		
		int rowsCount = map.getHeight();
		int colsCount = map.getWidth();
		fields = new FieldActor[rowsCount][colsCount];
		
		
		for (int row = rowsCount - 1; row >= 0; --row) {
			for (int col = 0; col < colsCount; ++col) {
				Field fieldInfo = map.getField(row, col);
				fields[row][col] = new FieldActor(fieldInfo, row, col);
			}
		}
		updateSizes();
	}
	
	
	@Override
	public void sizeChanged() {
		updateSizes();
	}
	
	public void updateSizes() {
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
}
