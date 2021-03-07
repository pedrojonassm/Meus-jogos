package itens.dinheiro;

import main.SpriteSheets;

public class TenThousandDollarNote extends Dinheiro{

	public TenThousandDollarNote(int idbd, int total) {
		super(idbd, 4, total, 100);
		addSprites(7, 22, 0, SpriteSheets.itensSprites);
		setar_sprite();
		item_name = "Ten Thousand Dollar Note";
	}
}
