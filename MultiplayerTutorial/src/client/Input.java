package client;

import java.io.DataInputStream;
import java.io.IOException;

public class Input implements Runnable{
	
	DataInputStream in;
	Client client;
	public Input(DataInputStream in, Client c) {
		this.in = in;
		client = c;
	}

	@Override
	public void run() {
		while(true) {
			try {
				int playerid = in.readInt(), x = in.readInt(), y = in.readInt();
				client.updateCoordinates(playerid, x, y);;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
}
