import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import javax.swing.JApplet;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.LineBorder;

public class TicTacToeClient extends JApplet implements Runnable,
		TicTacToeConstants {

	private boolean myTurn;
	private char myToken;
	private char otherToken;
	private Cell[][] cells;
	private JLabel title;
	private JLabel status;
	private boolean continueToPlay;
	private int rowSelected;
	private int columnSelected;
	private DataInputStream fromServer;
	private DataOutputStream toServer;
	private boolean waiting;
	private boolean isStandAlone;// if run as application
	private String host;// host name or IP add

	public TicTacToeClient() {
		myTurn = false;
		myToken = ' ';
		otherToken = ' ';
		cells = new Cell[3][3];
		title = new JLabel();
		status = new JLabel();
		continueToPlay = true;
		waiting = true;
		isStandAlone = false;
		host = "localhost";
	}

	@Override
	public void init() {

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(3, 3, 0, 0));
		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				panel.add(cells[i][j] = new Cell(i, j, this));
			}
		}

		panel.setBorder(new LineBorder(Color.BLACK, 1));
		title.setHorizontalAlignment(JLabel.CENTER);
		title.setFont(new Font("SansSerif", Font.BOLD, 16));
		title.setBorder(new LineBorder(Color.BLACK, 1));
		status.setBorder(new LineBorder(Color.BLACK, 1));

		add(title, BorderLayout.NORTH);
		add(panel, BorderLayout.CENTER);
		add(status, BorderLayout.SOUTH);

		connectToServer();
	}

	public void connectToServer() {
		try {
			Socket socket;
			if (isStandAlone) {
				socket = new Socket(host, 8000);
			} else {
				socket = new Socket(getCodeBase().getHost(), 8000);
			}

			// input & output streams to get and send data from and to server
			fromServer = new DataInputStream(socket.getInputStream());
			toServer = new DataOutputStream(socket.getOutputStream());

		} catch (Exception e) {
			e.printStackTrace();
		}

		Thread thread = new Thread(this);
		thread.start();

	}

	@Override
	public void run() {
		try {
			// get mssg from server
			int player = fromServer.readInt();

			// figure out which palyer and startup game
			if (player == PLAYER1) {
				myToken = 'X';
				otherToken = 'O';
				title.setText("Player One using X");
				status.setText("Waiting for player 2 to join");

				fromServer.readInt();// get mssg from ohter player
				status.setText("Player 2 has joined.");

				myTurn = true;// Plyer one's turn
			}
			
			else if(player == PLAYER2){
				myToken = 'O';
				otherToken = 'X';
				title.setText("Player Two using O");
				status.setText(("Waiting for player one to make move.");
			}
			
			//continue game
			while(continueToPlay){
				if(player == PLAYER1){
					waitForPlayerAction();//wait for plyer one
					sendMove();//send move to server
					recieveInfoFromServer();
				}
				else if(player == PLAYER2){
					recieveInfoFromServer();
					waitForPlayerAction();//wait for player 2 to move
					sendMove(); //send player 2 move to server
				}
			}

		} catch (Exception e) {
		}
	}

	private void recieveInfoFromServer() throws IOException{
		
		int gameStatus = fromServer.readInt();//get game status
		
		if(gameStatus == PLAYER1_WON){
			//winner so stop game
			continueToPlay = false;
			if(myToken == 'X'){
				status.setText("YOU WON (Player X)!");
			}
			else if(myToken == '0'){
				status.setText("Player one (X) has won!");
				recieveMove();
			}
		}
		
		else if(gameStatus == PLAYER2_WON){
			//player 2 won so stop game
			continueToPlay = false;
			if(myToken == 'O'){
				status.setText("YOU WON (Player O)!");
			}
			else if(myToken == 'X'){
				status.setText("Player 2 (O) has won!");
				recieveMove();
			}
		}
		
		else{
			recieveMove();
			status.setText("Your turn");
			myTurn = true; 
		}
		
	}

	private void recieveMove() {
		//get other players move
		int row = fromServer.readInt();
		int column = fromServer.readInt();
		cells[row][column].setToken(otherToken);
		
	}

	//send player move to server
	private void sendMove() {
		
		toServer.writeInt(rowSelected);
		toServer.writeInt(columnSelected);
		
	}

	private void waitForPlayerAction() {
		while(waiting){
			Thread.sleep(100);
		}
		waiting = true;
	}

	public boolean isMyTurn() {
		return myTurn;
	}

	public char getMyToken() {
		return myToken;
	}

	public void setMyTurn(boolean myTurn) {
		this.myTurn = myTurn;
	}

	public void setRowSelected(int rowSelected) {
		this.rowSelected = rowSelected;
	}

	public void setColumnSelected(int columnSelected) {
		this.columnSelected = columnSelected;
	}

	public JLabel getStatus() {
		return status;
	}

	public void setStatus(JLabel status) {
		this.status = status;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}

}
