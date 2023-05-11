package me.pedrokaua.minesweeper.vision;

import java.awt.Color;

import javax.swing.JFrame;

import me.pedrokaua.minesweeper.model.Board;

public class ScreenMain extends JFrame{
	private static final long serialVersionUID = 1L;
	Board board = new Board(15, 15, 30);
	BoardScreen boardScreen = new BoardScreen(board);
	
	public ScreenMain(){
		this.add(boardScreen);
		creatingScreen();
	}

	private void creatingScreen() {
		this.setSize(300, 300);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.getContentPane().setBackground(new Color(30, 30, 30));
		this.setResizable(false);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	
	public static void main(String[] args) {
		new ScreenMain();
	}

	
}
