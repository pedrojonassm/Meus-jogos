package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	
	// Link: https://www.youtube.com/watch?v=_1ThWf9Fkfo
	
	static ServerSocket serverSocket;
	static Socket socket;
	static DataOutputStream out;
	static DataInputStream in;
	public static int port = 7777;
	public static String ip = "localhost";
	static Users[] user = new Users[10];
	
	public static void main(String[] args) throws Exception {
		serverSocket = new ServerSocket(port);
		
		while(true) {
			socket = serverSocket.accept();
			for (int i = 0; i < 10; i++) {
				if (user[i] == null) {
					System.out.println("Conexão feita com: "+socket.getInetAddress());
					out = new DataOutputStream(socket.getOutputStream());
					in = new DataInputStream(socket.getInputStream());
					user[i] = new Users(out, in, user, i);
					Thread thread = new Thread(user[i]);
					thread.start();
					break;
				}
			}
		}
	}
}
