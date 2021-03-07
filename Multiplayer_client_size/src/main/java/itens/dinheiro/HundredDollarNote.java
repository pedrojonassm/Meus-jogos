package itens.dinheiro;

import main.SpriteSheets;

public class HundredDollarNote extends Dinheiro{

	public HundredDollarNote(int idbd, int total) {
		super(idbd, 3, total, 100);
		addSprites(7, 15, 0, SpriteSheets.itensSprites);
		setar_sprite();
		item_name = "Hundred Dollar Note";
	}

}
