package com.project.trip4u.data;

public enum DayLoad {
	CALM(2),
	MODERATE(3),
	INTENSE(4);
	
private int value;
	
	DayLoad(int value){
		this.value = value;
	}
	
	public int getValue() {
		return this.value;
	}
};
