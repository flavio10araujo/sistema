package com.polifono.domain.enums;

public enum Rank {
	WHITE(1, "Faixa Branca"), 
	YELLOW(2, "Faixa Amarela"), 
	ORANGE(3, "Faixa Laranjada"), 
	RED(4, "Faixa Vermelha"), 
	PURPLE(5, "Faixa Roxa"), 
	BROWN(6, "Faixa Marrom"), 
	BLACK(7, "Faixa Preta"), 
	COPPER(8, "Faixa Cobre"), 
	SILVER(9, "Faixa Prateada"), 
	GOLD(10, "Faixa Dourada");
	
	private int level;
	private String color;
	
	Rank(int level, String color) {
		this.level = level;
		this.color = color;
	}
	
	public int getLevel() {
		return level;
	}
	
	public String getColor() {
		return color;
	}
}