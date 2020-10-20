package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;
import main.Sons;

public class Weapon extends Entity{
	private static BufferedImage[] sprites;
	public static int index = 0, frames = 0, maxFrames = 5, maxIndex = 1;
	public static boolean atirando = false, som = false;
	public Weapon(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		sprites = new BufferedImage[4];
		sprites[0] = Game.spritesheet.getSprite(2*Game.TS, 1*Game.TS, Game.TS, Game.TS);
		sprites[1] = Game.spritesheet.getSprite(3*Game.TS, 1*Game.TS, Game.TS, Game.TS);
		sprites[2] = Game.spritesheet.getSprite(8*Game.TS, 1*Game.TS, Game.TS, Game.TS);
		sprites[3] = Game.spritesheet.getSprite(9*Game.TS, 1*Game.TS, Game.TS, Game.TS);
		// TODO Auto-generated constructor stub
	}
	
	public static void atirar() {
		if (atirando) {
			if(!som) {
				Sons.carregandoEsfera.loop();
				som = true;
			}
			frames++;
			if (frames >= maxFrames) {
				frames = 1;
				index++;
				if (index > maxIndex) {
					index = 0;
				}
			}
		}else {
			if(som) {
				Sons.carregandoEsfera.stop();
				som = false;
			}
		}
	}
	
	public static void desenhar(Graphics g, int x, int y, int d) {
		if(d == 0) {//direita
			g.drawImage(sprites[index], x, y, null);
		}
		else {// esquerda
			g.drawImage(sprites[index+2], x, y, null);
		}
	}
}
