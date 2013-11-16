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

	public boolean isFieldOwnedBy(String thisPlayerName, int rowIndex, int colIndex) {
		return fields[rowIndex][colIndex].isOwnedBy(thisPlayerName);
	}

	public boolean canPlayerAttackField(String attacker, int row, int col) {
		if (!(row > 0 && row < getHeight()-1 && col > 0 && col < getWidth()-1))
			return false;
		
		Field target = fields[row][col];
		if (target == null) return false;
		if (target.isOwnedBy(attacker)) return false;
		
		int rightAdj = col + 1;
		int leftAdj = col - 1;
		boolean canAttackAdjacentHorz = false;
		if (leftAdj >= 0) {
			canAttackAdjacentHorz = fields[row][leftAdj] != null && fields[row][leftAdj].isOwnedBy(attacker); 
		}
		if (rightAdj < getWidth()) {
			canAttackAdjacentHorz |= fields[row][rightAdj] != null && fields[row][rightAdj].isOwnedBy(attacker);
		}
		
		boolean canAttackAdjacentVert = false;
		int vertAdj = (isUpperTriangle(row, col) ? row - 1 : row + 1);
		if (vertAdj < getWidth() && vertAdj >= 0) {
			canAttackAdjacentVert = fields[vertAdj][col] != null && fields[vertAdj][col].isOwnedBy(attacker);
		}
		
		return canAttackAdjacentHorz || canAttackAdjacentVert;
	}
}
