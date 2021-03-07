package itens.dinheiro;

import main.SpriteSheets;

public class Coin extends Dinheiro{

	public Coin(int idbd, int total) {
		super(idbd, 1, total, 100);
		addSprites(8, 0, 0, SpriteSheets.itensSprites);
		setar_sprite();
		item_name = "coin";
	}
	
}
