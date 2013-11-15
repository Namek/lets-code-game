package com.letscode.lcg.model;

public class Map {
	private Field[][] fields;
	
	
	public Field getField(int rowIndex, int colIndex) {
		return fields[rowIndex][colIndex];
	}
	
	public static boolean isUpperTriangle(int rowIndex, int colIndex) {
		return rowIndex % 2 != colIndex % 2;
	}
}
