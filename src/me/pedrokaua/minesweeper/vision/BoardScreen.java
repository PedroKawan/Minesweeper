package me.pedrokaua.minesweeper.vision;

import java.awt.GridLayout;

import javax.swing.JOptionPane;
import javax.swing.JPanel;

import me.pedrokaua.minesweeper.model.Board;

public class BoardScreen extends JPanel{
	private static final long serialVersionUID = 1L;

	public BoardScreen(Board board){
		this.setLayout(new GridLayout(
				board.getLines(), board.getColumns()));
		
		
		board.forEach(f -> add(new FieldButton(f)));
		
		board.addObserver(e -> {
			if(e.isWinner()) {
				JOptionPane.showMessageDialog(null, "YOU ARE A WINNER!!!");
			} else {
				JOptionPane.showMessageDialog(null, "YOU LOSE :(");
			}
			board.reset();

		});
	}
}
