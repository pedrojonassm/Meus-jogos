package itens.dinheiro;

import main.SpriteSheets;

public class MillionDollarNote extends Dinheiro{
	public MillionDollarNote(int idbd, int total) {
		super(idbd, 5, total, 100);
		addSprites(7, 29, 0, SpriteSheets.itensSprites);
		setar_sprite();
		item_name = "Million Dollar Note";
	}
}
