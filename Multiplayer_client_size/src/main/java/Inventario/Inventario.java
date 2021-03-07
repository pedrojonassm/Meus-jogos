package Inventario;

import main.SpriteSheets;

public class Inventario extends Bag{

	public Inventario(int itemIdBD, int bagidBD) {
		super(itemIdBD, bagidBD, -1, 50);
		item_name = "inventário";
		addSprites(1, 42, 0, SpriteSheets.itensSprites);
	}

}
