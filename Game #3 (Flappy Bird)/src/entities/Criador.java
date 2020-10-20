package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class Criador extends Entity {
	private Cano lastcano = null;
	private boolean criado;
	private int k = 0;

	public Criador(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
	}

	public void tick() {
		if (lastcano == null || !criado) {
			k++;
			criado = true;
			lastcano = new Cano(this.getX(), this.getY(), 0, 16, 16, null);
			if (k < 2) {
				Game.canos.add(lastcano);
				Game.entities.add(lastcano);
			}
			int r;
			do {
				r = rand.nextInt(Game.HEIGHT / 16); // 0 a 19
			} while (!(getY() / 16 + 6 > r && getY() / 16 - 6 < r && r >= 3 && r <= 17));
			setY(16 * r);
		} else if (criado) {
			int q;
			if (Game.pontos > 60) {
				q = 60;
			} else {
				q = 120 - Game.pontos;
			}
			if (getX() - lastcano.getX() > q) {
				if (k > 0) {
					k--;
				}
				criado = false;
			}
		}
	}

	public void render(Graphics g) {
		//g.setColor(Color.blue);
		//g.fillRect(getX(), getY(), 16, 16);
	}
}
