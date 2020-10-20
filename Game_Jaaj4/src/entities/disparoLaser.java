package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import world.Camera;
import world.Mundo;

public class disparoLaser extends Entity{
	
	public Entity pai;

	public disparoLaser(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		this.sprite = Game.spritesheet.getSprite(Game.TS, Game.TS, Game.TS, Game.TS);
		mx = 2;
		my = 6;
		mwidth = 12;
		mheight = 3;
		maxLife = 60*15;
	}
	
	public void tick() {
		if (life > maxLife || x < 0 || x > Mundo.WIDTH*16 || y < 0 || y > Mundo.HEIGHT*16) {
			Game.lasers.remove(this);
			return;
		}
		life++;
		x+=Math.cos(Math.toRadians(rotate))*speed;
		y+=Math.sin(Math.toRadians(rotate))*speed;
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity e = Game.entities.get(i);
			if (isColidding(this, e)) {
				if (this.pai != Game.player.controle) {
					if (e instanceof Nave && ((Nave) e).cor == ((Nave) pai).cor && pai != Game.player.controle) {
						continue;
					}
					if (e == pai || e == Game.player || e instanceof disparoLaser || (e instanceof Gerador && ((Nave) pai).cor == ((Gerador) e).cor)) {
						continue;
					}
				}
				if ((e == Game.player || e == Game.player.controle) && pai == Game.player.controle) {
					continue;
				}
				else {
					Game.lasers.remove(this);
					if (e == Game.player && Game.player.controle != null) {
						e = Game.player.controle;
					}
					e.life -= Entity.rand.nextInt(5);
					if (e.life <= 0) {
						e.avisarMorte();
						if (e == Game.player.controle) {
							Game.player.controle = null;
						}
						if (((Nave) pai).alvo != null) {
							((Nave) pai).alvo = null;
						}
						if (e instanceof Gerador) {
							((Gerador) e).destruido = true;
						}else {
							Game.entities.remove(e);
						}
						if (e instanceof Meteor) {
							Game.meteoros.remove(e);
						}else if (e instanceof Nave) {
							if (((Nave) e).cor == 0) {
								// azul, verde, vermelho, amarelo, rosa;
								Game.azul.remove(e);
							}else if (((Nave) e).cor == 1) {
								Game.verde.remove(e);
							}else if (((Nave) e).cor == 2) {
								Game.vermelho.remove(e);
							}else if (((Nave) e).cor == 3) {
								Game.amarelo.remove(e);
							}else if (((Nave) e).cor == 4) {
								Game.rosa.remove(e);
							}
						}
					}
					return;
				}
			}
		}
		if (isColidding(this,Game.player) && pai != Game.player.controle) {
			Game.player.life -= Entity.rand.nextInt(5);
			Game.entities.remove(this);
		}
	}
	
	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.red);
		g2.rotate(Math.toRadians(rotate), this.getX()+Game.TS/2-Camera.x, this.getY()+Game.TS/2-Camera.y);
		g2.drawImage(sprite, this.getX()-Camera.x, this.getY()-Camera.y, null);
		g2.rotate(-Math.toRadians(rotate), this.getX()+Game.TS/2-Camera.x, this.getY()+Game.TS/2-Camera.y);
	}
	
}
