package me.pedrokaua.minesweeper.model;

public class ResultEvent {
	
	private final boolean result;
	
	public ResultEvent(boolean result) {
		this.result = result;
	}
	
	public boolean isWinner() {
		return this.result;
	}
}
