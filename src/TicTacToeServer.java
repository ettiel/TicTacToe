import java.awt.BorderLayout;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class TicTacToeServer extends JFrame implements TicTacToeConstants{

	public TicTacToeServer(){
		JTextArea area = new JTextArea();
		
		JScrollPane scrollPane = new JScrollPane(area);
		
		add(scrollPane, BorderLayout.CENTER);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(300,300);
		setTitle("TicTacToeServer");
		setVisible(true);
		
		try{
			ServerSocket serverSocket = new ServerSocket(8000);
			area.append(new Date() + ": server started at socket 8000");
			
			int session = 1;
			
			while(true){
				area.append(new Date() + "\n: wait for players to join game " + session + "\n");
				
				//player 1
				Socket player1 = serverSocket.accept();
				
				area.append(new Date() + ": player 1 joined game " + session);
				area.append("\nPlayer 1's IP Address " + player1.getInetAddress().getHostAddress() + '\n');
				
				new DataOutputStream(
						player1.getOutputStream()).writeInt(PLAYER1);
				
				
				//player 2
				Socket player2 = serverSocket.accept();
				
				area.append(new Date() + ": player 2 joined game " + session + '\n');
				area.append("player 2's IP address " + player2.getInetAddress().getHostAddress() + '\n');
				
				new DataOutputStream( player2.getOutputStream()).writeInt(PLAYER2);
				
				area.append(new Date() + ": start thread for session " + session++);
				
				HandleASession task = new HandleASession(player1, player2);
				
				new Thread(task).start();
			}
		}
		
		catch(IOException ex){
			System.err.println(ex);
		}
	}

	public static void main(String[] args){
		TicTacToeServer frame = new TicTacToeServer();
	}
	
}
