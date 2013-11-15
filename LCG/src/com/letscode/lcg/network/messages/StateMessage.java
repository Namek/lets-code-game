package com.letscode.lcg.network.messages;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;
import com.letscode.lcg.model.Field;

public class StateMessage extends MessageBase {
	public Field[][] fields;
	private ArrayList<ArrayList<JsonValue>> state;
	
	public void convertJsonToModel() {
		final int numRows = state.size();
		fields = new Field[numRows][state.get(0).size()];
		for (int row = 0; row < numRows; ++row) {
			final int numCols = state.get(0).size();
			for (int col = 0; col < numCols; ++col) {
				Field fld = new Field();
				fld.building = state.get(row).get(col).getString("building");
				fld.owner = state.get(row).get(col).getString("owner");
				fld.resourceSize = Integer.parseInt(state.get(row).get(col).getString("resourceSize"));
				fld.type = state.get(row).get(col).getString("type");
				fields[row][col] = fld;
			}
		}
	}
}
