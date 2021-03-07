package itens.dinheiro;

import main.SpriteSheets;

public class DollarNote extends Dinheiro{

	public DollarNote(int idbd, int total) {
		super(idbd, 2, total, 100);
		addSprites(7, 8, 0, SpriteSheets.itensSprites);
		setar_sprite();
		item_name = "dollar note";
	}

}
