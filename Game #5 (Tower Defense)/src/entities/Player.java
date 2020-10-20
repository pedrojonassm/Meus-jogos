package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import main.Rank;
import world.Camera;
import world.Mundo;

public class Player extends Entity {

	public boolean right, left, moved, morre, ld, grande = false, caindo, holdJump = false, salto = false; //ld = last key = direita?
	public static double life = 100, maxLife = 100;
	
	
	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}

	public void tick() {
		for (int i = 0; i < Game.inimigos.size(); i++) {
			Enemy e = Game.inimigos.get(i);
			if (isColidding(this, e)) {
				Game.entities.remove(e);
				Game.inimigos.remove(e);
				life--;
				return;
			}
		}
		if (life <= 0) {
			System.exit(1);
		}
	}
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(this.getX(),this.getY(),16,16);
		g.fillRect(getX(), getY()-3, 100, 3);
		g.setColor(Color.blue);
		g.fillRect(getX(), getY()-3, (int) ((life/maxLife)*16), 3);
		g.setColor(Color.white);
	}

}
