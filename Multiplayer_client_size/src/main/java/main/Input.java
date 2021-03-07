package main;

import java.io.DataInputStream;
import java.io.IOException;

import Inventario.Bag;
import world.World;

public class Input implements Runnable{
	
	DataInputStream in;
	Client client;
	public static Bag bag;
	
	public Input(DataInputStream in) {
		this.in = in;
		client = Client.instance;
	}

	public void run() {
		while(true) {
			try {
				switch (in.readInt()) {
				case 0:
					client.updateCoordinates(in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt());
					break;
				case 1:
					client.ui.adicionar_fala(in.readUTF(), in.readInt(), in.readInt(), false);
					break;
				case 2:
					// Recebendo um item que precisa atualizar seu id no banco de dados
					client.player.atualizar_item_id(in.readInt(), in.readInt()); // id antigo, id novo
					break;
				case 3:
					// recebendo um item que estava no chão
					World.adicionar_item(in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt());
					break;
				case 4:
					// removendo um item que estava no chão
					World.remover_item(in.readInt());
					break;
				case 5:
					Client.instance.player.pegar_itens_da_bag_no_server(in, bag);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
}
