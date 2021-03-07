package itens;

import java.io.DataOutputStream;
import java.io.IOException;

public class Item {
	protected int quantidade, item, item_idBD, x, y, z, id_bag;

	public Item(int quantidade, int item, int item_idBD, int x, int y, int z, int id_bag) {
		this.quantidade = quantidade;
		this.item = item;
		this.item_idBD = item_idBD;
		this.x = x;
		this.y = y;
		this.z = z;
		this.id_bag = id_bag;
	}

	public void enviar(DataOutputStream out) throws IOException {
		//id_item_bd, tipo_item, quantidade_item
		out.writeInt(item_idBD);
		out.writeInt(item);
		out.writeInt(quantidade);
		out.writeInt(id_bag);
		out.writeInt(x);
		out.writeInt(y);
		out.writeInt(z);	
	}
	
	public int getItem_idBD() {
		return item_idBD;
	}
	
}
