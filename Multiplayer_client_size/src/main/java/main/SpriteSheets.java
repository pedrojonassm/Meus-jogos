package main;

import graficos.SpriteSheet;

public class SpriteSheets {
	// Guardas as sprites de todos os sprites
	public static SpriteSheet spritesheet, itensSprites, playerSprites;
	public SpriteSheets() {
		spritesheet = new SpriteSheet("/spritesheet.png", Client.TS, 1);
		itensSprites = new SpriteSheet("/itens_sprites.png", Client.TI, 1);
		playerSprites = new SpriteSheet("/entidades/players.png", Client.TS, 12);
	}
}
