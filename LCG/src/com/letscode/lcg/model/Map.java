package com.letscode.lcg.model;

public class Map {
	private Field[][] fields;
	
	
	public Map(Field[][] fields) {
		this.fields = fields;
	}
	
	public Field getField(int rowIndex, int colIndex) {
		return fields[rowIndex][colIndex];
	}
	
	public int getWidth() {
		return fields[0].length;
	}
	
	public int getHeight() {
		return fields.length;
	}
	
	public static boolean isUpperTriangle(int rowIndex, int colIndex) {
		return rowIndex % 2 != colIndex % 2;
	}
}
