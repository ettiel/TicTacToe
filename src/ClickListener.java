import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JLabel;


public class ClickListener extends MouseAdapter{
	
	private Cell cell;
	private TicTacToeClient client;
	private JLabel clientStatus;
	
	public ClickListener(Cell cell, TicTacToeClient client){
		this.cell = cell;
		this.client = client;
		clientStatus = client.getStatus();
	}
	
	@Override
	public void mouseClicked(MouseEvent event){
		//if the cell is not occupied then player takes turn
		if(cell.getToken() == ' ' && client.isMyTurn()){
			cell.setToken(client.getMyToken());//set plyers token in cell
			client.setMyTurn(false);
			client.setRowSelected(cell.getRow());
			client.setColumnSelected(cell.getColumn());
			clientStatus.setText("Waiting for next player to take turn.");
			client.setWaiting(false);//this player successfully completed a move
			
		}
	}
	
	

}
