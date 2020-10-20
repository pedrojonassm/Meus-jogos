package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

import main.Game;
import main.Sons;
import world.Camera;

public class Missil extends Nave{
	
	
	public boolean trocarAlvo = true, som = false;
	public Missil(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		life = 180;
		a = 2;
		mx = 1;
		my = 5;
		mheight = 8;
		mwidth = 3;
	}
	
	public void tick() {
		life--;
		if (life < 0) {
			Game.entities.remove(this);
		}
		
		if (alvo != null && trocarAlvo) {
			alvo.cacador.add(this);
			trocarAlvo = false;
		}
		if (path == null && alvo != null) {
			this.rotate = Math.toDegrees(Math.atan2(alvo.getY()-getY(), alvo.getX()-getX()));
			moverse();
		}else if (alvo == null){
			trocarAlvo = true;
		}else {
			this.followPath(path);
		}
		colisao();
	}
	public void colisao(){
		for (int i = 0; i < Game.meteoros.size(); i++) {
			Meteor m = Game.meteoros.get(i);
			if (isColidding(this, m)) {
				if (alvo == m) {
					for (int k = 0; k < Game.planetas.size(); k++) {
						if (Game.planetas.get(k).cor == cor) {
							if (Game.planetas.get(k).near.size() > 0 && Game.planetas.get(k).near.get(0) == m) {
								Game.planetas.get(k).near.remove(0);
							}
						}
					}
				}
				m.life -= this.life;
				if (m.life < 0) {
					m.avisarMorte();
					if (m == Game.player.controle) {
						Game.player.controle = null;
					}
					Game.entities.remove(m);
					Game.meteoros.remove(m);
				}
				Game.entities.remove(this);
			}
		}
		for (int i = 0; i < Game.verde.size() && cor != Game.verde.get(0).cor; i++) {
			Nave m = Game.verde.get(i);
			if (isColidding(this, m)) {
				if (alvo == m) {
					for (int k = 0; k < Game.planetas.size(); k++) {
						if (Game.planetas.get(k).cor == cor) {
							if (Game.planetas.get(k).near.size() > 0 && Game.planetas.get(k).near.get(0) == m) {
								Game.planetas.get(k).near.remove(0);
							}
						}
					}
				}
				m.life -= this.life;
				if (m.life < 0) {
					m.avisarMorte();
					if (m == Game.player.controle) {
						Game.player.controle = null;
					}
					Game.entities.remove(m);
					Game.verde.remove(m);
				}
				Game.entities.remove(this);
			}
		}
		for (int i = 0; i < Game.vermelho.size() && cor != Game.vermelho.get(0).cor; i++) {
			Nave m = Game.vermelho.get(i);
			if (isColidding(this, m)) {
				if (alvo == m) {
					for (int k = 0; k < Game.planetas.size(); k++) {
						if (Game.planetas.get(k).cor == cor) {
							if (Game.planetas.get(k).near.size() > 0 && Game.planetas.get(k).near.get(0) == m) {
								Game.planetas.get(k).near.remove(0);
							}
						}
					}
				}
				m.life -= this.life;
				if (m.life < 0) {
					m.avisarMorte();
					if (m == Game.player.controle) {
						Game.player.controle = null;
					}
					Game.entities.remove(m);
					Game.vermelho.remove(m);
				}
				Game.entities.remove(this);
			}
		}
		for (int i = 0; i < Game.amarelo.size() && cor != Game.amarelo.get(0).cor; i++) {
			Nave m = Game.amarelo.get(i);
			if (isColidding(this, m)) {
				if (alvo == m) {
					for (int k = 0; k < Game.planetas.size(); k++) {
						if (Game.planetas.get(k).cor == cor) {
							if (Game.planetas.get(k).near.size() > 0 && Game.planetas.get(k).near.get(0) == m) {
								Game.planetas.get(k).near.remove(0);
							}
						}
					}
				}
				m.life -= this.life;
				if (m.life < 0) {
					m.avisarMorte();
					if (m == Game.player.controle) {
						Game.player.controle = null;
					}
					Game.entities.remove(m);
					Game.amarelo.remove(m);
				}
				Game.entities.remove(this);
			}
		}
		for (int i = 0; i < Game.rosa.size() && cor != Game.rosa.get(0).cor; i++) {
			Nave m = Game.rosa.get(i);
			if (isColidding(this, m)) {
				if (alvo == m) {
					for (int k = 0; k < Game.planetas.size(); k++) {
						if (Game.planetas.get(k).cor == cor) {
							if (Game.planetas.get(k).near.size() > 0 && Game.planetas.get(k).near.get(0) == m) {
								Game.planetas.get(k).near.remove(0);
							}
						}
					}
				}
				m.life -= this.life;
				if (m.life < 0) {
					m.avisarMorte();
					if (m == Game.player.controle) {
						Game.player.controle = null;
					}
					Game.entities.remove(m);
					Game.rosa.remove(m);
				}
				if (som) {
					Sons.foguetePerseguindo.stop();	
				}
				Game.entities.remove(this);
			}
		}
		if ((alvo == Game.player || alvo == Game.player.controle) && !this.som) {
			Sons.foguetePerseguindo.loop();
			som = true;
		}
		if (isColidding(this,Game.player)) {
			Game.player.life -= this.life/5;
			Game.entities.remove(this);
			if (som) {
				Sons.foguetePerseguindo.stop();	
			}
		}
		if (!existe()) {
			Game.entities.remove(this);
			if (som) {
				Sons.foguetePerseguindo.stop();	
			}
		}
	}
	
	private boolean existe() {
		for (int i = 0; i < Game.entities.size(); i++) {
			Entity m = Game.entities.get(i);
			if (m == alvo) {
				return true;
			}
		}
	return false;
	}
	
	public void moverse() {
		if (rotate > 0 && rotate < 180) {
			// vai para baixo --> v < 0
			dir = 1;
			if (vyd < speed) {
				if(vyu > 0) {
					vyu-=a;
				}else{
					vyu = 0;
					vyd+=a;
				}
			}
		}if (rotate < 0 && rotate > -180){
			// vai para cima --> v > 0
			dir = 0;
			if (vyu < speed) {
				if (vyd > 0) {
					vyd -= a;
				}else{
					vyd = 0;
					vyu+=a;
				}
			}
			//System.out.println("vyd: "+vyd+"   vyu: "+vyu);
		}
		v = vyu - vyd;
		if (v < 0) {
			v *= -1;
		}
		if ((dir == 0 && vyu - vyd < 0) || (dir == 1 && vyu - vyd > 0)) {
			// A nave estava indo para um lado e agora vai para o outro
			v *= -1;
		}
		vy = v*Math.sin(Math.toRadians(rotate));
		if (rotate > -90 && rotate < 90) {
			// vai para a direita
			dir = 1;
			if (vxr < speed) {
				if (vxl > 0) {
					vxl -= a;
				}else{
					vxl = 0;
					vxr += a;
				}
			}
		}else if ((rotate > 90 && rotate <= 180) || (rotate < -90 && rotate <= -180)) {
			// vai para a esquerda
			dir = 0;
			if (vxl < speed) {
				if (vxr > 0) {
					vxr -=a;
				}else {
					vxr = 0;
					vxl += a;
				}
			}
		}
		
		v = vxl - vxr;
		
		if (v < 0) {
			v *= -1;
		}
		
		if ((dir == 0 && vxl - vxr < 0) || (dir == 1 && vxl - vxr > 0)) {
			// A nave estava indo para um lado e agora vai para o outro
			v *= -1;
		}
		
		vx= v*Math.cos(Math.toRadians(rotate));
		x+= vx;
		y += vy;
	}
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(Math.toRadians(rotate), this.getX()+Game.TS/2-Camera.x, this.getY()+Game.TS/2-Camera.y);
		//g2.fillRect(getX()+mx-Camera.x, getY()+my-Camera.y, this.getWidth()-mwidth, this.getHeight()-mheight);//mascara de colisao
		g2.drawImage(sprite, this.getX()-Camera.x, this.getY()-Camera.y, null);
		g2.rotate(-Math.toRadians(rotate), this.getX()+Game.TS/2-Camera.x, this.getY()+Game.TS/2-Camera.y);
	}

}
