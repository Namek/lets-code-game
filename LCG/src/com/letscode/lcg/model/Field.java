package com.letscode.lcg.model;

public class Field {
	public static final String TYPE_NORMAL = "normal";
	public static final String TYPE_GOLD = "gold";
	
	public static final String BUILDING_TOWNHALL = "townhall";
	public static final String BUILDING_BARRICADE = "barricade";
	public static final String BUILDING_GOLDMINE = "goldmine";
	
	
	public String type = TYPE_NORMAL;	// normal, gold
	public String building = null;		// townhall, mine, barricade
	public int resourceSize = 0;		// count of times the gold can be... dig out
	public String owner = null;
	
	
	public Field() {
	}
	
	public static Field createNormalField(String owner) {
		Field field = new Field();
		field.owner = owner;
		
		return field;
	}
	
	public static Field createGoldField(int resourceSize) {
		Field field = new Field();
		field.type = TYPE_GOLD;
		field.resourceSize = resourceSize;
		
		return field;
	}
	
	public Field owns(String owner) {
		this.owner = owner;
		return this;
	}
}
