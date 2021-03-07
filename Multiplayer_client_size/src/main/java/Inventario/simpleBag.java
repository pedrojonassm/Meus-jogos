package Inventario;

import main.SpriteSheets;

public class simpleBag extends Bag{

	public simpleBag(int itemIdBD, int bagidBD) {
		super(itemIdBD, bagidBD, 6, 15);
		item_name = "bag simples";
		addSprites(1, 42, 0, SpriteSheets.itensSprites);
	}

}
