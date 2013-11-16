package com.letscode.lcg.network.messages;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;
import com.letscode.lcg.model.Field;

public class StateMessage extends MessageBase {
	public Field[][] fields;
	private ArrayList<ArrayList<JsonValue>> map;
	
	public void convertJsonToModel() {
		final int numRows = map.size();
		fields = new Field[numRows][map.get(0).size()];
		for (int row = 0; row < numRows; ++row) {
			final int numCols = map.get(0).size();
			for (int col = 0; col < numCols; ++col) {
				if (map.get(row).get(col) != null) {
					Field fld = new Field();
					fld.building = map.get(row).get(col).getString("building");
					fld.owner = map.get(row).get(col).getString("owner");
					fld.resourceSize = Integer.parseInt(map.get(row).get(col).getString("resourceSize"));
					fld.type = map.get(row).get(col).getString("type");
					fields[row][col] = fld;
				}
			}
		}
	}
}
