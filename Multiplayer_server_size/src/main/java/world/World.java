package world;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import itens.Item;
import main.Server;
import main.Users;

public class World {
	public static ArrayList<Item> itens_no_chao;
	
	public World() {
		itens_no_chao = new ArrayList<Item>();
	}
	
	public void adicionar_item(int quantidade, int item, int item_idBD, int x, int y, int z, int id_bag) {
		if (item_idBD != -1)
			Server.daoItem.criar_ou_atualizar_item(item_idBD, item, 0, quantidade); // diz no banco de dados que o item foi jogado no chão
		itens_no_chao.add(new Item(quantidade, item, item_idBD, x, y, z, id_bag));
		if  (id_bag != -1) Server.daoBag.setar_personagem(id_bag, -1);
		
		// enviar para os jogadores que foi adicionado o item na posicao x, y, z
		try {
			for (Users u : Server.user) {
				if (u == null) continue;
				
				u.out.writeInt(3);
				u.out.writeInt(quantidade); 
				u.out.writeInt(item); 
				u.out.writeInt(item_idBD); 
				u.out.writeInt(x); 
				u.out.writeInt(y); 
				u.out.writeInt(z); 
				u.out.writeInt(id_bag);
			}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void remover_item(int item_idBD, int personagem, int bag_id) throws IOException {
		if (bag_id != -1) {
			Server.daoBag.setar_personagem(bag_id, personagem);
		}
		for (Users u : Server.user) {
			if (u != null) {
				u.out.writeInt(4);
				u.out.writeInt(item_idBD);
			}
		}
		for (Item i : itens_no_chao) {
			if (i.getItem_idBD() == item_idBD) {
				itens_no_chao.remove(i);
				return;
			}
		}
	}

	public void enviar_itens_no_chao(DataOutputStream out) throws IOException {
		out.writeInt(itens_no_chao.size());
		for (int i=0; i < itens_no_chao.size(); i++) {
			itens_no_chao.get(i).enviar(out);
		}
		
	}
}
