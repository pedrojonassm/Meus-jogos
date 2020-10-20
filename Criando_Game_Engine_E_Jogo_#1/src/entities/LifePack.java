package entities;

import java.awt.image.BufferedImage;

import main.Game;

public class LifePack extends Entity {
	
	public int cura = Game.rand.nextInt(30);

	public LifePack(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		// TODO Auto-generated constructor stub
	}
}
