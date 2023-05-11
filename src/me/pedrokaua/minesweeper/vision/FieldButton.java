package me.pedrokaua.minesweeper.vision;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import me.pedrokaua.minesweeper.model.Field;
import me.pedrokaua.minesweeper.model.FieldEvent;
import me.pedrokaua.minesweeper.model.FieldObserver;

public class FieldButton extends JButton
				implements FieldObserver, MouseListener {

	private static final long serialVersionUID = 1L;

	Color PATTERN = new Color(200, 200, 200);
	Color MARK = new Color(200, 200, 255);
	Color EXPLOSION = new Color(255, 10, 10);
	Color TEXT = new Color(0, 100, 0);
	
	Border PATTERN_BORDER 
		= BorderFactory.createBevelBorder(0);
	private Field field;
	
	public FieldButton(Field field) {
		this.field = field;
		this.setFont(new Font("ArialBlack", Font.BOLD, 12));
		this.setBorder(PATTERN_BORDER);
		this.setBackground(PATTERN);
		this.field.addObserver(this);
		this.setOpaque(true);
		this.addMouseListener(this);
	}

	@Override
	public void action(Field field, FieldEvent event) {
		switch(event) {
		case OPEN:
			eventOpen();
			break;
		case MARK:
			eventMark();
			break;
		case MARKOFF:
			eventMarkoff();
			break;
		case EXPLOSION:
			eventExplosion();
			break;
		default:
			eventPattern();
			break;
		}
		
		SwingUtilities.invokeLater(() -> {
			repaint();
			validate();
		});
	}

	void eventPattern() {
		this.setBackground(PATTERN);
		this.setBorder(PATTERN_BORDER);
		this.setText("");
	}

	private void eventOpen() {
		if (field.isMined()) {
			this.setBackground(Color.red);
			return;
		}
		
		this.setBackground(new Color(100, 100, 100));
		this.setBorder(null);
		
		switch(field.amountMines()) {
		case 1:
			this.setForeground(Color.GREEN);
			break;
		case 2:
			this.setForeground(Color.BLUE);
			break;
		case 3:
			this.setForeground(Color.RED);
			break;
		}
		
		String value = !field.safeNext()?
				Integer.toString(field.amountMines()): "";
		this.setText(value);
	}

	private void eventMark() {
		this.setText("🚩");
		this.setForeground(Color.WHITE);
		this.setBackground(Color.BLUE);
	}
	
	private void eventMarkoff() {
		this.eventPattern();
	}
	
	private void eventExplosion() {
		this.setText("💣");
		this.setForeground(Color.WHITE);
		this.setBorder(null);
	}

	
	//MouseListener
		@Override
		public void mouseClicked(MouseEvent e) {
			if(e.getButton() == 1) {
				field.open();
			} else {
				field.addMark();
			}
		}
		
		public void mouseEntered(MouseEvent e) {}
		public void mouseExited(MouseEvent e) {}
		public void mousePressed(MouseEvent e) {}
		public void mouseReleased(MouseEvent e) {}
		//
}
