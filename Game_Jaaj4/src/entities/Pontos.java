package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;

public class Pontos extends Entity{
	Pontos p1 = null, p2 = null;
	public boolean fixo;
	public boolean colidindo = false, alterar = false, alterar2 = false;
	
	public Pontos(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}
	
	public void tick() {
		if (Entity.isColidding(this, Game.player)) {
			this.colidindo = true;
		}
		if (this.colidindo) {
			if (p2 != null) {
				p2.alterar = true;
			}
			if (p1 != null) {
				p1.alterar = true;
			}
		}
		if (alterar2) {
			alterar2 = false;
			colidindo = true;
		}
		if (alterar) {
			alterar = false;
			alterar2 = true;
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		if(fixo) {
			g2.setColor(Color.black);
		}else if (colidindo) {
			g2.setColor(Color.yellow);
		}
		else {
			g2.setColor(Color.red);
		}
		g.fillRect(getX()-Camera.x, getY()-Camera.y, 1*Game.SCALE, 1*Game.SCALE);
	}
}
