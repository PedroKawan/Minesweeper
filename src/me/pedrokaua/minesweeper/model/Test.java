package me.pedrokaua.minesweeper.model;

public class Test {
	public static void main(String[] args) {
		Board board = new Board(3, 3, 9);
		
		board.addObserver(f -> {
			if(f.isWinner()) {
				System.out.println("Winner");
			} else {
				System.out.println("BUUM");
			}
		});
		
		board.open(0, 0);
		board.mark(0, 0);
		board.mark(0, 1);
		board.mark(0, 2);
		board.mark(1, 0);
		board.mark(1, 1);
		board.mark(1, 2);
		board.mark(2, 0);
		board.mark(2, 1);
		board.open(2, 2);
	}
}
