package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import main.Sons;
import world.Camera;
import world.Mundo;

public class Nave extends Entity{
	
	public Entity alvo = null;
	public Gerador mae = null;
	public boolean moved = false, freio = false;
	public int tickDisparo, maxTick=30, dir;
	public double v, vr = 3, vxr, vxl, vyu, vyd, vx, vy, ralvo;
	
	public Nave(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		while (a == 0) {
			a = Math.random()+0.5;
		}
		cacador = new ArrayList<Nave>();
		life = maxLife = 100;
		/*int c = Entity.rand.nextInt(25), n = Entity.rand.nextInt(19);
		this.sprite = Game.naves.getNave(c, n);
		if (c <= 4) {
			Game.azul.add(this);
		}else if (c <= 9) {
			Game.verde.add(this);
		}else if (c <= 14) {
			Game.vermelho.add(this);
		}else if (c <= 19) {
			Game.amarelo.add(this);
		}else if (c <= 24) {
			Game.rosa.add(this);
		}*/
	}
	
	public void tick() {
		if (this.escolhido) {
			if (!Game.player.hold) {
				escolhido = false;
			}
		}
		tickDisparo++;
		if (controlado) {
			x = Game.player.getX();
			y = Game.player.getY();
			rotate = Game.player.rotate;
		}else{
			if (alvo != null) {
				moverse();
			}
		}
		if (!controlado) {
			// verificar se a distancia da nave pro alvo é entre 150 e 200, caso seja fazer ele perseguir e depois atirar
			if (alvo != null) {
				this.rotate = Math.toDegrees(Math.atan2(alvo.getY()-getY(), alvo.getX()-getX()));
				if (distancia(alvo.getX(), alvo.getY(), getX(), getY()) <= 50){
					freio = true;
					moved = true;
					if (alvo != null) {
						if (tickDisparo>maxTick) {
							tickDisparo = 0;
							atirar();
						}
					}
				}else if (distancia(alvo.getX(), alvo.getY(), getX(), getY()) <= 100 || (alvo instanceof Gerador && distancia(alvo.getX(), alvo.getY(), getX(), getY()) > 100)) {
					freio = false;
					moved = true;
				}else{
					if (!(alvo instanceof Gerador)) {
						alvo = null;
					}
				}
			}
			if (alvo == null || alvo instanceof Gerador) {
				//amarelo, verde, vermelho, azul, rosa;
				ArrayList<Entity> near = proximo(100);
				if (near.size() > 0) {
					int menor = near.get(0).distance;
					for (int i = 0; i < near.size(); i++) {
						if (near.get(0).distance <= menor) {
							alvo = near.get(0);
							menor = near.get(0).distance;
						}
						near.remove(0);
					}
				}
				if (alvo == null) {
					while (alvo == null) {
						alvo = Game.planetas.get(rand.nextInt(Game.planetas.size()));
						if ( ((Gerador)alvo).cor == cor) {
							alvo = null;
						}
					}
				}
				if (alvo != null) {
					alvo.cacador.add(this);
				}
			}
		}
		if (vyu == 0 && vyd == 0 && vxr == 0 && vxl == 0) {
			if (!existe()) {
				alvo = null;
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
		if (moved || freio) {
			if (rotate > 0 && rotate < 180) {
				// vai para baixo --> v < 0
				dir = 1;
				if (moved) {
					if (vyd < speed) {
						if(vyu > 0) {
							vyu-=a;
						}else{
							vyu = 0;
							vyd+=a;
						}
					}
				}else if (freio) {
					if(vyu > 0) {
						vyu-=a;
					}else if (vyd > 0){
						vyu = 0;
						vyd-=a;
						if (vyd < 0) {
							vyd = 0;
						}
					}
				}
			}if (rotate < 0 && rotate > -180){
				// vai para cima --> v > 0
				dir = 0;
				if (moved) {
					if (vyu < speed) {
						if (vyd > 0) {
							vyd -= a;
						}else{
							vyd = 0;
							vyu+=a;
						}
					}
				}else if (freio) {
					if (vyd > 0) {
						vyd -= a;
					}else if (vyu > 0){
						vyd = 0;
						vyu-=a;
						if (vyu < 0) {
							vyu = 0;
						}
					}
				}
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
				if (moved) {
					if (vxr < speed) {
						if (vxl > 0) {
							vxl -= a;
						}else{
							vxl = 0;
							vxr += a;
						}
					}
				}else if (freio) {
					if (vxl > 0) {
						vxl -= a;
					}else if (vxr > 0){
						vxl = 0;
						vxr -= a;
						if (vxr < 0) {
							vxr = 0;
						}
					}
				}
			}else if ((rotate > 90 && rotate < 180) || (rotate < -90 && rotate < -180)) {
				// vai para a esquerda
				dir = 0;
				if (moved) {
					if (vxl < speed) {
						if (vxr > 0) {
							vxr -=a;
						}else {
							vxr = 0;
							vxl += a;
						}
					}
				}else if (freio) {
					if (vxr > 0) {
						vxr -=a;
					}else if (vxl > 0){
						vxr = 0;
						vxl -= a;
						if (vxl < 0) {
							vxl = 0;
						}
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
		}
		x+= vx;
		y += vy;
	}
	public int modulo(double vx) {
		if (vx < 0) {
			vx *= -1;
		}
		return (int) vx;
	}
	public void atirar() {
		disparoLaser d = new disparoLaser(getX(), getY(), 16, 16, 10, null);
		d.rotate = rotate;
		d.pai = this;
		Game.lasers.add(d);
		if (this.controlado) {
			Sons.laser.play();
		}
	}
	
	public void render(Graphics g) {
		if (escolhido) {
			g.setColor(Color.red);
			g.fillRect(getX()-Camera.x, getY()-Camera.y, 1, 3);
			g.fillRect(getX()-Camera.x, getY()-Camera.y, 3, 1);
			g.fillRect(getX()-Camera.x+Game.TS-1, getY()-Camera.y+Game.TS-3, 1, 3);
			g.fillRect(getX()-Camera.x+Game.TS-3, getY()-Camera.y+Game.TS-1, 3, 1);
		}
		g.setColor(Color.white);
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(Math.toRadians(rotate), this.getX()+Game.TS/2-Camera.x, this.getY()+Game.TS/2-Camera.y);
		//g2.fillRect(getX()+mx-Camera.x, getY()+my-Camera.y, this.getWidth()-mwidth, this.getHeight()-mheight);//mascara de colisao
		g2.drawImage(sprite, this.getX()-Camera.x, this.getY()-Camera.y, null);
		g2.rotate(-Math.toRadians(rotate), this.getX()+Game.TS/2-Camera.x, this.getY()+Game.TS/2-Camera.y);
	}
}
