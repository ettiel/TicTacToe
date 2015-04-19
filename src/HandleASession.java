import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;


public class HandleASession implements Runnable, TicTacToeConstants{
	
	private Socket player1;
	private Socket player2;
	
	private char[][] cell = new char[3][3];
	
	private DataInputStream from1;
	private DataOutputStream to1;
	private DataInputStream from2;
	private DataOutputStream to2;
	
	private boolean continueToPlay = true;
	
	public HandleASession (Socket player1, Socket player2){
		this.player1 = player1;
		this.player2 = player2;
		
		//cells
		for(int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				cell[i][j] = ' ';
		
	}
	

	@Override
	public void run() {
		try{
			DataInputStream from1 = new DataInputStream( player1.getInputStream());
			DataOutputStream to1 = new DataOutputStream( player1.getOutputStream());
			DataInputStream from2 = new DataInputStream( player2.getInputStream());
			DataOutputStream to2 = new DataOutputStream( player2.getOutputStream());
			
			//player 1 start
			to1.writeInt(1);
			
			//game loop
			while(true){
				int row = from1.readInt();
				int column = from1.readInt();
				cell[row][column] = 'X';
				
				if (isWon('X')){
					to1.writeInt(PLAYER1_WON);
					to2.writeInt(PLAYER1_WON);
					sendMove(to2, row, column);
					break;
				}
				else{ //player 2
					to2.writeInt(CONTINUE);
					sendMove(to2, row, column);
				}
				
				row = from2.readInt();
				column = from2.readInt();
				cell[row][column] = 'O';
				
				if(isWon('O')){
					to1.writeInt(PLAYER2_WON);
					to2.writeInt(PLAYER2_WON);
					sendMove(to1, row, column);
					break;
				}
				else{
					to1.writeInt(CONTINUE);
					sendMove(to1, row, column);
				}
			}
			
			
		}
		
		catch(IOException ex){
			System.err.println(ex);
		}
		// TODO Auto-generated method stub
		
	}

	
	private void sendMove(DataOutputStream out, int row, int column)throws IOException{
		out.writeInt(row);
		out.writeInt(column);
	}
	
	//if all cells are full
	private boolean isFull(){
		for (int i=0; i<3; i++)
			for (int j=0; j<3; j++)
				if(cell[i][j] == ' ')
					return false;
		return true;
	}
	
	private boolean isWon(char peice){
		//rows
		for (int i=0; i<3; i++)
			if((cell[i][0] ==peice) && (cell[i][1] ==peice) && (cell[i][2] == peice)){
				return true;
			}
		
		//columns
		for (int j=0; j<3; j++)
			if((cell[0][j]==peice) && (cell[1][j]==peice) && (cell[2][j]==peice)){
				return true;
			}
		
		//diagonals
		if((cell[0][0]==peice) && (cell[1][1]==peice) && (cell[2][2]==peice)){
			return true;
		}
		
		if((cell[0][2]==peice) && (cell[1][1]==peice) && (cell[2][0]==peice)){
			return true;
		}
		return false;
	}
}
