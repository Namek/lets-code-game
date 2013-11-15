package com.letscode.lcg.network.messages;

import java.util.ArrayList;

import com.badlogic.gdx.utils.JsonValue;
import com.letscode.lcg.model.Field;

public class StateMessage extends MessageBase {
	ArrayList<Field> fieldModel;
	ArrayList<ArrayList<JsonValue>> fields;
	
	public void convertJsonToModel() {
		final int numRows = fields.size();
		for (int row = 0; row < numRows; ++row) {
			final int numCols = fields.get(0).size();
			for (int col = 0; col < numCols; ++col) {
				// TODO: conversion
			}
		}
	}
}
