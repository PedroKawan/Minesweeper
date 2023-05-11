package me.pedrokaua.minesweeper.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class Board implements FieldObserver{
	
	private int lines;
	private int columns;
	private int mines;
	long minesGenerated;
	
	 private List<Field> fields = new ArrayList<>();
	 private final List<Consumer<ResultEvent>> observers
	 	= new ArrayList<>();
	 
	public Board(int lines, int columns, int mines) {
		this.setLines(lines);
		this.setColumns(columns);
		this.setMines(mines);

		generateFields();
		addNextFields();
		addMines();
	}

	public void addObserver(Consumer<ResultEvent> observer){
		observers.add(observer);
	}
	
	private void notifyObservers(boolean result) {
		observers.stream()
			.forEach(o -> o.accept(new ResultEvent(result)));
	}
	
	public void forEach(Consumer<Field> consumer) {
		fields.forEach(consumer);
	}
	
	public void open(int line, int column){
			fields.parallelStream()
			.filter(f -> f.getLine() == line && f.getColumn() == column)
			.findFirst()
			.ifPresent(f -> f.open());
			fields.forEach(f -> f.setOpen(true));
	}
	
	public void mark(int line, int column){
		fields.parallelStream()
			.filter(f -> f.getLine() == line && f.getColumn() == column)
			.findFirst()
			.ifPresent(f -> f.addMark());
	}

	private void generateFields() {
		for (int l = 0; l < this.getLines(); l++) {
			for (int c = 0; c < this.getColumns(); c++) {
				Field field = new Field(l, c);
				field.addObserver(this);
				fields.add(field);
			}
		}
	}
	
	private void addNextFields() {
		for(Field f1: fields){
			for (Field f2: fields) {
				f1.addField(f2);
			}
		}
	}
	
	private void addMines(){
		minesGenerated = 0;
		Predicate<Field> isMined = f -> f.isMined();
		Random r = new Random();
		
		do {
			int random = r.nextInt(fields.size() - 1);
			fields.get(random).addMine();
			minesGenerated = fields.stream().filter(isMined).count();
		} while(minesGenerated < this.getMines());
	}
	
	boolean generateSuccessful(){
		int amountgenerateds = fields.size();
		int amountLinesColumns = this.getLines() * this.getColumns();
		if(amountgenerateds == amountLinesColumns) return true;
		return false;
	}
	
	public void reset(){
		fields.stream().forEach(f -> f.reset());
		addMines();
	}

	public boolean objective(){
		return fields.stream().allMatch(f -> f.objective());
	}


	//Getters and Setters methods
	public int getLines() {
		return lines;
	}

	public void setLines(int lines) {
		this.lines = lines;
	}

	public int getColumns() {
		return columns;
	}

	public void setColumns(int columns) {
		this.columns = columns;
	}

	public int getMines() {
		return mines;
	}

	public void setMines(int mines) {
		this.mines = mines;
	}

	public List<Field> getFields() {
		return fields;
	}

	public void setFields(List<Field> fields) {
		this.fields = fields;
	}
	//


	@Override
	public void action(Field field, FieldEvent event) {
		if(event == FieldEvent.EXPLOSION) {
			viewMines();
			notifyObservers(false);
		} else if (this.objective()) {
			notifyObservers(true);
		}
	}
	
	private void viewMines() {
		fields.stream()
			.filter(f -> f.isMined())
			.filter(f -> !f.isMarked())
			.forEach(f -> f.setOpen(true));
	}
	
}
