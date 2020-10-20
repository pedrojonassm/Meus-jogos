package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import world.Mundo;

public class Player extends Entity {

	public boolean move, moved, morre = false, subir = false, mudar = false, hold = false;
	private int maxY, r, xx, yy;

	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		this.xx = x;
		this.yy = y;
		this.speed = 0;
		this.setMask(0, -2, Game.TS, Game.TS - 4);
	}

	public void tick() {
		// r = 90 (baixo) r = 270 (cima)
		// para subir use speed (r = -90 quando speed = 3) r=a*speed; -90=3*a; a=-30
		// para descer r = 90 quando speed = 6; r = a*speed; 90=6*a; a=15
		if (mudar) {
			speed = 0;
			mudar = false;
		}
		if (speed < 3 && !subir) {
			speed += 2;
		} else {
			if (speed < 6) {
				speed += 0.02;
			}
		}
		this.depth = 1;
		if (!subir) {
			r = (int) (15 * speed);
		} else {
			r = (int) (-30 * speed);
		}
		if (move && !hold) {
			hold = true;
			subir = true;
			move = false;
			maxY = getY() - 32;
		}
		if (getY() > maxY && subir && getY() > 0) {
			y -= speed;
			if (getY() < maxY) {
				mudar = true;
				setY(maxY);
			}
		} else {
			subir = false;
			if (getY() < Game.HEIGHT - 16) {
				y += speed;
				if (getY() < maxY) {
					setY(maxY);
				}
			}
		}
		morre = colisao();
		if (morre) {
			setX(xx);
			setY(yy);
		}
	}

	public boolean colisao() {
		for (int i = 0; i < Game.canos.size(); i++) {
			Cano e = Game.canos.get(i);
			if (e.getX() < getX() + 16 && e.getX() > getX() - 16) {
				for (int k = 0; k < e.yd.size(); k++) {
					if (k == 0) {
						if (Mundo.pixelPerfectColision(sprite, getX(), getY(), e.sprites[0], e.getX(), e.yd.get(k))) {
							return true;
						}
					} else if (k == e.yd.size() - 1) {
						if (Mundo.pixelPerfectColision(sprite, getX(), getY(), e.sprites[2], e.getX(), e.yd.get(k))) {
							return true;
						}
					} else {
						if (Mundo.pixelPerfectColision(sprite, getX(), getY(), e.sprites[1], e.getX(), e.yd.get(k))) {
							return true;
						}
					}
				}
				for (int k = 0; k < e.yu.size(); k++) {
					if (k == 0) {
						if (Mundo.pixelPerfectColision(sprite, getX(), getY(), e.sprites[0], e.getX(), e.yu.get(k))) {
							return true;
						}
					} else if (k == e.yd.size() - 1) {
						if (Mundo.pixelPerfectColision(sprite, getX(), getY(), e.sprites[2], e.getX(), e.yu.get(k))) {
							return true;
						}
					} else {
						if (Mundo.pixelPerfectColision(sprite, getX(), getY(), e.sprites[1], e.getX(), e.yu.get(k))) {
							return true;
						}
					}
				}

			}
		}
		return false;
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		// g.setColor(Color.white);
		// g.fillRect(getX()-this.mx, getY()-my, this.mwidth, this.mheight);
		g2.rotate(Math.toRadians(r), this.getX() + Game.TS / 2, this.getY() + Game.TS / 2);
		g2.drawImage(sprite, this.getX(), this.getY(), null);
	}
}
