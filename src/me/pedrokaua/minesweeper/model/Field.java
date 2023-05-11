package me.pedrokaua.minesweeper.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Field {
	private int line;
	private int column;
	
	private boolean isEmpty;
	private boolean isOpen;
	private boolean isMarked = false;
	private boolean isMined;
	
	private List<Field> nextFields = new ArrayList<>();
	private Set<FieldObserver> observers = new HashSet<>();
	
	Field(int line, int column){
		this.setLine(line);
		this.setColumn(column);
	}
	
	public void addObserver(FieldObserver observer){
		this.observers.add(observer);
	}
	
	void notifyObservers(FieldEvent event) {
		this.observers.stream()
			.forEach(o -> o.action(this, event));
	}
	
	boolean addField(Field next) {
		boolean differentLine = this.getLine() != next.getLine();
		boolean differentColumn = this.getColumn() != next.getColumn();
		boolean diagonal = differentLine && differentColumn;
		
		int deltaLine = Math.abs(this.getLine() - next.getLine());
		int deltaColumn = Math.abs(this.getColumn() - next.getColumn());
		int deltaGeral = deltaLine + deltaColumn;
		
		if(deltaGeral == 1 && !diagonal) {
			nextFields.add(next);
			return true;
		} else if(deltaGeral == 2 && diagonal) {
			nextFields.add(next);
			return true;
		} else {
			return false;
		}
	}
	
	public void addMark() {
		if(!isOpen) {
			this.setMarked(!(this.isMarked()));
			
			if(isMarked) {
				this.notifyObservers(FieldEvent.MARK);
			} else {
				this.notifyObservers(FieldEvent.MARKOFF);
			} 
		}
	}
	
	void addMine() {
		if(!isOpen() && !isMarked()) {
			this.setMined(true);
		}
	}
	
	public boolean open() {
		if (!isOpen && !isMarked) {
			
			if(isMined) {
				this.setMined(true);
				this.notifyObservers(FieldEvent.EXPLOSION);
				return true;
			}
			
			this.setOpen(true);
			
			if(safeNext()) {
				nextFields.forEach(n -> n.open());
			}
			
			return true;
		} else {
			return false;
		}
	}

	public boolean safeNext() {
		return nextFields.stream().allMatch(n -> !n.isMined());
	}
	
	
	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getColumn() {
		return column;
	}

	public void setColumn(int column) {
		this.column = column;
	}
	
	
	public boolean isOpen() {
		return isOpen;
	}

	public void setOpen(boolean isOpen) {
		this.isOpen = isOpen;
		if (isOpen) {
			this.notifyObservers(FieldEvent.OPEN);
		}
		
	}

	public boolean isEmpty() {
		return isEmpty;
	}

	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}

	public boolean isMarked() {
		return isMarked;
	}

	public void setMarked(boolean isMarked) {
		this.isMarked = isMarked;
	}

	public boolean isMined() {
		return isMined;
	}

	public void setMined(boolean isMined) {
		this.isMined = isMined;
	}

	public List<Field> getNextFields() {
		return nextFields;
	}

	public void setNextfields(List<Field> nextfields) {
		this.nextFields = nextfields;
	}
	
	
	public int amountMines() {
		return (int) this.getNextFields().stream().filter(n -> n.isMined()).count();
	}
	
	public boolean objective() {
		boolean openedSuccessful = !this.isMined() && this.isOpen();
		boolean markedSuccessful = this.isMarked() && this.isMined();
		return openedSuccessful || markedSuccessful;
	}
	
	public void reset() {
		this.setMined(false);
		this.setOpen(false);
		this.setMarked(false);
		notifyObservers(FieldEvent.RESET);
	}
	
}
