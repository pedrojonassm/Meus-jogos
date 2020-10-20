package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import main.Game;
import main.Sons;
import world.Camera;

public class Gerador extends Entity{
	
	public BufferedImage[] sprites;
	public boolean destruido = false, proteger = false, semNaves = false;
	public int tick, maxTick = 10*60, foguetes, dispararFoguetes = 180;
	public ArrayList<Entity> near;
	
	public Gerador(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		// aqui foi usado a velocidade para determinar qual planeta era qual
		cor = (int) speed;
		cacador = new ArrayList<Nave>();
		sprites = new BufferedImage[2];
		sprites[0] = Game.spritesheet.getSprite((int) (speed*48), 2*Game.TS, 48, 48);
		sprites[1] = Game.spritesheet.getSprite((int) (speed*48), 2*Game.TS+48, 48, 48);
		speed = 0;
		maxLife = life = 10000;
	}
	public void tick() {
		if (!destruido) {
			if (this.life <= 0) {
				Sons.planeta.play();
				Game.pontos++;
				this.destruido = true;
			}
			if ( near == null || near.size() == 0) {
				near = proximo(150);
			}
			foguetes++;
			while(near.size() > 0 && (distancia(near.get(0).getX(), near.get(0).getY(), getX(), getY()) > 150 || near.get(0).cor == this.cor)) {
				near.remove(0);
			}
			while (near.size() > 0 && !existe()) {
				near.remove(0);
			}
			if (foguetes > dispararFoguetes && near.size() > 0 && distancia(near.get(0).getX(), near.get(0).getY(), getX(), getY()) <= 150) {
				foguetes -= dispararFoguetes;
				Missil m = new Missil(getX()+24, getY()+24, 16, 16, 10, Game.spritesheet.getSprite(0, Game.TS, Game.TS, Game.TS));
				m.alvo = near.get(0);
				m.cor = cor;
				Game.entities.add(m);
			}
			for (int i = 0; i < Game.meteoros.size(); i++) {
				Meteor m = Game.meteoros.get(i);
				if (isColidding(this, m)) {
					this.life -= m.life*3;
					if (m.controlado) {
						Game.player.controle = null;
					}
					
					for (int l = 0; l < near.size(); l++) {
						if (near.get(l) == m) {
							near.remove(l);
							break;
						}
					}
					Game.meteoros.remove(m);
					Game.entities.remove(m);
					m.avisarMorte();
				}
			}
			if (tick > maxTick) {
				tick = 0;
				// azul, verde, vermelho, amarelo, rosa;
				boolean cabe = true;
				for (int i = 0; i < 3 && cabe; i++) {
					int k = ThreadLocalRandom.current().nextInt(cor*5, (cor+1)*5);
					Nave n = new Nave(getX()+24, getY()+24, 16, 16,Entity.rand.nextInt(2)+3, Game.naves.getNave(k, Entity.rand.nextInt(10)));
					n.mae = this;
					n.cor = cor;
					if (cor == 0) {
						if (Game.azul.size() < 10) {
							Game.azul.add(n);
						}
						else
							cabe = false;
					}else if (cor == 1) {
						if (Game.verde.size() < 10){
							Game.verde.add(n);
						}
						else
							cabe = false;
					}else if (cor == 2) {
						if (Game.vermelho.size() < 10){
							Game.vermelho.add(n);
						}
						else
							cabe = false;
					}else if (cor == 3) {
						if (Game.amarelo.size() < 10) {
							Game.amarelo.add(n);
						}
						else
							cabe = false;
					}else if (cor == 4) {
						if (Game.rosa.size() < 10) {
							Game.rosa.add(n);
						}
						else
							cabe = false;
					}
					if (cabe) {
						Game.entities.add(n);
					}
				}
			}
			tick++;
		}
		if (destruido && !semNaves) {
			// azul, verde, vermelho, amarelo, rosa;
			if (cor == 0) {
				for (int i = 0; i < Game.azul.size(); i++) {
					Game.entities.remove(Game.azul.get(0));
					Game.azul.remove(0);
				}
				this.semNaves = true;
			}else if (cor == 1) {
				for (int i = 0; i < Game.verde.size(); i++) {
					Game.entities.remove(Game.verde.get(0));
					Game.verde.remove(0);
				}
				this.semNaves = true;
			}else if (cor == 2) {
				for (int i = 0; i < Game.vermelho.size(); i++) {
					Game.entities.remove(Game.vermelho.get(0));
					Game.vermelho.remove(0);
				}
				this.semNaves = true;
			}else if (cor == 3) {
				for (int i = 0; i < Game.amarelo.size(); i++) {
					Game.entities.remove(Game.amarelo.get(0));
					Game.amarelo.remove(0);
				}
				this.semNaves = true;
			}else if (cor == 4) {
				for (int i = 0; i < Game.rosa.size(); i++) {
					Game.entities.remove(Game.rosa.get(0));
					Game.rosa.remove(0);
				}
				this.semNaves = true;
			}
		}
	}
	private boolean existe() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity m = Game.entities.get(i);
			if (m == near.get(0)) {
				return true;
			}
		}
	return false;
	}
	public void render(Graphics g) {
		g.setColor(Color.blue);
		if (proteger) {
			g.fillRect(getX()+mx -Camera.x,getY()+my-Camera.y,getWidth()-mwidth,getHeight()-mheight);//mascara de colisao
		}
		if (destruido) {
			g.drawImage(sprites[1], this.getX()-Camera.x, this.getY()-Camera.y, null);
		}else {
			// Desenhar barra de vida
			g.setColor(Color.red);
			g.fillRect(getX()-Camera.x, getY()-3-Camera.y, 48, 3);
			g.setColor(Color.blue);
			g.fillRect(getX()-Camera.x, getY()-3-Camera.y, (int) ((life*48/maxLife)), 3);
			g.drawImage(sprites[0], this.getX()-Camera.x, this.getY()-Camera.y, null);
		}
	}
}
