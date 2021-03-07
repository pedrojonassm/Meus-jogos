package main;

import java.io.DataOutputStream;
import java.io.IOException;

import Inventario.Bag;
import entities.Player;
import itens.Item;

public class Output {
	private static DataOutputStream out;
	
	public Output(DataOutputStream o) {
		out = o;
	}
	
	public static void enviar_player(Player player) {
		try {
			out.writeInt(0);
			out.writeInt(player.getPlayerid());// System.out.println("enviado: "+player.getPlayerid());
			out.writeInt(player.getX());// System.out.println("enviado: "+player.getX());
			out.writeInt(player.getY());// System.out.println("enviado: "+player.getY());
			out.writeInt(player.getZ());// System.out.println("enviado: "+player.getZ());
			out.writeInt(player.getLife());// System.out.println("enviado: "+player.getLife());
			out.writeInt(player.getState());// System.out.println("enviado: "+player.getState());
			out.writeInt(player.getIndex());// System.out.println("enviado: "+player.getIndex());
			out.writeInt(player.getSkin());// System.out.println("enviado: "+player.getSkin());
		} catch (IOException e) {
			System.out.println("Não foi possivel enviar as coordenadas");
		}
	}
	
	public static void criar_ou_atualizar_item(Item item) {
		//System.out.println("item_id: "+item.getItemIdBD()+" tipo: "+item.item+" bag: "+item.getBag().getBag_idBD()+" quant: "+item.getQuantidade());
		try {
			out.writeInt(2);
			out.writeInt(item.getItemIdBD());
			out.writeInt(item.item);
			out.writeInt(item.getBag().getBag_idBD());
			out.writeInt(item.getQuantidade());
			Client.instance.player.precisa_atualizar_id(item);
		} catch (Exception e) {
			System.out.println("Não foi possivel enviar o item");
			e.printStackTrace();
		}
	}
	
	public static void enviar_bag(Bag bag) {
		try {
			
		} catch (Exception e) {
			System.out.println("Não foi possivel enviar a bag");
		}
	}
	
	public static void enviar_nova_fala_servidor(String fala, int x, int y) {
		try {
			out.writeInt(1);
			out.writeUTF(fala);
			out.writeInt(x);
			out.writeInt(y);
		} catch (IOException e) {
			System.out.println("Não foi possivel enviar a fala");
		}
	}
	
	public static void jogar_item_no_chao(Item item, int x, int y, int z) {
		try {
			out.writeInt(3);
			out.writeInt(item.getQuantidade());
			out.writeInt(item.item);
			out.writeInt(item.getItemIdBD());
			out.writeInt(x);
			out.writeInt(y);
			out.writeInt(z);
			if (item instanceof Bag) out.writeInt(((Bag) item).getBag_idBD());
			else out.writeInt(-1);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void remover_item_do_chao(Item i) {
		try {
			out.writeInt(4);
			out.writeInt(i.getItemIdBD());
			out.writeInt(Client.instance.player.getEntityIdBD());
			if (i instanceof Bag) {
				out.writeInt(((Bag) i).getBag_idBD());
			}else {
				out.writeInt(-1);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void pedir_itens_da_bag(Bag bag) {
		Input.bag = bag;
		try {
			out.writeInt(5);
			out.writeInt(bag.getBag_idBD());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
}
