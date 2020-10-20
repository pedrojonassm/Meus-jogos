package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class Users implements Runnable{
	
	DataOutputStream out;
	DataInputStream in;
	int id;
	Users[] users = new Users[10];
	int playerid, playeridin, xin, yin;
	
	public Users(DataOutputStream out, DataInputStream in, Users[] user, int pid) {
		this.out = out;
		this.in = in;
		this.users = user;
		playerid = pid;
	}

	@Override
	public void run() {
		
		try {
			out.writeInt(playerid);
		} catch (IOException e1) {
			System.out.println("Não enviou o id");
		}
		while(true) {
			try {
				playeridin = in.readInt();
				xin = in.readInt();
				yin= in.readInt();
				for (int i = 0; i < 10; i++) {
					if (users[i] != null) {
						users[i].out.writeInt(playeridin);
						users[i].out.writeInt(xin);
						users[i].out.writeInt(yin);
					}
				}
			} catch (IOException e) {
				users[playerid] = null;
			}
		}
	}
	
}
