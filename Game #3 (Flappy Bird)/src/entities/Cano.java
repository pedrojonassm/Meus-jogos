package entities;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;

public class Cano extends Entity {
	BufferedImage[] sprites = new BufferedImage[3], sprites2 = new BufferedImage[3];
	public ArrayList<Integer> yd = new ArrayList<Integer>(), yu = new ArrayList<Integer>();
	private boolean pontuou = false;

	public Cano(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		for (int i = 0; i < 3; i++) {
			sprites[i] = Game.spritesheet.getSprite((4 + i) * 16, 0, 16, 16);
			sprites2[i] = Game.spritesheet.getSprite((7 + i) * 16, 0, 16, 16);
		}
		int p;
		if (Game.pontos*3 > 60) {
			p = 0;
		} else {
			p = 60 - Game.pontos*3;
		}
		//
		int y1 = getY() + 32 + p;
		yd.add(y1);
		while (y1 < Game.HEIGHT - 32) {
			y1 += 16;
			yd.add(y1);
		}
		y1 += 16;
		yd.add(y1);

		//
		y1 = getY() - 32 - p;
		yu.add(y1);
		while (y1 > 0) {
			y1 -= 16;
			yu.add(y1);
		}
		y1 -= 16;
		yu.add(y1);
	}

	public void tick() {
		x--;
		if (x < (-16)) {
			Game.entities.remove(this);
			Game.canos.remove(this);
		} else if (x - 8 < Game.player.getX() && !pontuou) {
			this.pontuou = true;
			Game.pontos++;
		}
	}

	public void render(Graphics g) {
		for (int k = 0; k < yd.size(); k++) {
			if (k == 0) {
				g.drawImage(sprites[0], this.getX(), yd.get(k), null);
			} else if (k == yd.size() - 1) {
				g.drawImage(sprites[2], this.getX(), yd.get(k), null);
			} else {
				g.drawImage(sprites[1], this.getX(), yd.get(k), null);
			}
		}
		for (int k = 0; k < yu.size(); k++) {
			if (k == 0) {
				g.drawImage(sprites2[0], this.getX(), yu.get(k), null);
			} else if (k == yu.size() - 1) {
				g.drawImage(sprites2[2], this.getX(), yu.get(k), null);
			} else {
				g.drawImage(sprites2[1], this.getX(), yu.get(k), null);
			}
		}

		/*
		 * y1 = getY()-32; yu.add(y1); g.drawImage(sprites2[0], this.getX(), y1,null);
		 * y1-=16; while(y1>0) { yu.add(y1); g.drawImage(sprites2[1], this.getX(),
		 * y1,null); y1-=16; } yu.add(y1); g.drawImage(sprites2[2], this.getX(),
		 * y1,null);
		 */
	}
}
