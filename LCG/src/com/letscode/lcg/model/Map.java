package com.letscode.lcg.model;

public class Map {
	private Field[][] fields;
	
	
	public Map(Field[][] fields) {
		this.fields = fields;
	}
	
	public Field getField(int rowIndex, int colIndex) {
		return areThoseIndicesProper(rowIndex, colIndex) ? fields[rowIndex][colIndex] : null;
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

	public boolean isFieldOwnedBy(String playerName, int rowIndex, int colIndex) {
		Field field = getField(rowIndex, colIndex);
		return field != null && field.owner != null && field.owner.equals(playerName);
	}

	public boolean canPlayerAttackField(String player, int rowIndex, int colIndex) {
		// does this field even exist?
		if (!areThoseIndicesProper(rowIndex, colIndex))
			return false;
		
		// check upper and lower neighbour
		if (isUpperTriangle(rowIndex, colIndex)) {
			if (rowIndex < getHeight() && isFieldOwnedBy(player, rowIndex+1, colIndex))
				return true;
		}
		else {
			if (rowIndex > 0 && isFieldOwnedBy(player, rowIndex-1, colIndex))
				return true;
		}
		
		// check side neighbours
		if (colIndex > 0 && isFieldOwnedBy(player, rowIndex, colIndex-1))
			return true;
		
		if (colIndex < getWidth() && isFieldOwnedBy(player, rowIndex, colIndex+1))
			return true;
		
		return false;
	}
	
	private boolean areThoseIndicesProper(int rowIndex, int colIndex) {
		return !(rowIndex < 0 || rowIndex >= getHeight() || colIndex < 0 || colIndex >= getWidth());
	}
}
