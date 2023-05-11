package me.pedrokaua.minesweeper.model;

public interface FieldObserver {
	
	public void action(Field field, FieldEvent event);
}
