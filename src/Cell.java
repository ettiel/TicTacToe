import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.border.LineBorder;


public class Cell extends JPanel{

	private int row;
	private int column;
	private TicTacToeClient client;
	private char token = ' ';
	
	public Cell(int row, int column, TicTacToeClient client){
		this.row = row;
		this.column = column;
		this.client = client;
		setBackground(Color.BLACK);
		setBorder(new LineBorder(Color.WHITE, 2));//cell border
		addMouseListener(new ClickListener(this, client));
		
	}
	
	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		
		if(token == 'X'){
			g.setColor(Color.PINK);
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(5));
			g2.drawLine(10, 10, getWidth()-10, getHeight() -10);
			g2.drawLine(getWidth()-10, 10, 10, getHeight() -10);
		}
		
		else if (token == 'O'){
			g.setColor(Color.BLUE);
			Graphics2D g2 = (Graphics2D) g;
			g2.setStroke(new BasicStroke(5));
			g2.drawOval(10, 10, getWidth() - 20, getHeight() - 20);
		}
	}

	public char getToken() {
		return token;
	}

	public void setToken(char token) {
		this.token = token;
		repaint();
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}
	
}
