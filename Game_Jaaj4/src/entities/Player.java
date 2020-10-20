package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import main.Game;
import main.Sons;
import world.Camera;
import world.Mundo;

public class Player extends Entity {

	public boolean up, down,right, left, morre, moved, freio, atacar = false, hold = false; //ld = last key = direita?
	public Entity controle = null;
	private List<Entity> near = null;
	public int escolha;
	private Entity colidindo;
	
	public int dir = 0;
	private BufferedImage[] sprites;
	public double v, vr = 3, vxr, vxl, vyu, vyd, vx, vy;
	private int index, maxIndex = 4, frames, maxFrames = 20, l = 1;
	public boolean atirar = false;
	
	public Player(int x, int y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		life = maxLife = 100;
		cacador = new ArrayList<Nave>();
		sprites = new BufferedImage[5];
		for (int i = 0; i < 5; i++) {
			sprites[i] = Game.spritesheet.getSprite((i+2)*Game.TS, Game.TS, Game.TS, Game.TS);
		}
	}

	public void tick() {
		frames++;
		if (frames > maxFrames) {
			frames = 0;
			index += l;
			if (index == maxIndex || index == 0) {
				l *= -1;
			}
		}
		if (rotate < 0) {
			rotate += 360;
		}else if (rotate > 360) {
			rotate -= 360;
		}
		if (controle != null) {
			this.speed = controle.speed;
			a = controle.a;
		}
		updateCamera();
		if (controle instanceof Nave && !hold) {
			if(right) {
				rotate+=vr;
			}else if(left) {
				rotate-=vr;
			}
		}
		if (atirar && controle instanceof Nave) { 
			atirar = false;
			((Nave)controle).atirar();
		}
		if ((moved || freio) && controle instanceof Nave) {
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
			}if (rotate > 180 && rotate < 360){
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
			
			if ((rotate >= 0 && rotate < 90) || (rotate > 270 && rotate <= 360)) {
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
			}else if (rotate > 90 && rotate < 270) {
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
		else if (!(controle instanceof Nave) && !hold) {
			if (up) {
				vy = -4;
				vyu = 1; vyd = 0;
			}else if (down) {
				vy = 4;
				vyd = 1; vyu = 0;
			}else {
				vy = vyd = vyu = 0;
			}
			if (left) {
				vx = -4;
				vxr = 0; vxl = 1;
			}else if (right) {
				vx = 4;
				vxr = 1; vxl = 0;
			}else {
				vx = vxr = vxl = 0;
			}
		}
		if (hold) {
			if (near.size()>0) {
				if (escolha <= near.size()) {
					if (escolha != near.size())
					near.get(escolha).escolhido = true;
					if (left) {
						left = false;
						if (escolha != near.size())
						near.get(escolha).escolhido = false;	
						escolha--;
					}else if (right) {
						right = false;
						if (escolha != near.size())
						near.get(escolha).escolhido = false;
						escolha++;
					}
					if (escolha > near.size()) {
						escolha = 0;
					}else if (escolha < 0) {
						escolha = near.size();
					}
				}
			}
		}
		if (hold && !atacar) {
			avisarMorte();
			if (controle != null && escolha != near.size()) {
				controle.controlado = false;
			}
			if (near.size()>0) {
				if (escolha < near.size()) {
					this.life = this.maxLife;
					near.get(escolha).escolhido = false;
					if (controle != near.get(escolha)) {
						Sons.possuirInimigo.play();
						controle = near.get(escolha);
						this.setX(controle.getX());
						this.setY(controle.getY());
						this.rotate = controle.rotate;
						controle.controlado = true;
					}else {// caso seja escolhido a nave em que ele está, o bicho sairá da nave
						controle.vx = vx;
						controle.vy = vy;
						controle = null;
					}
				}//caso não seja escolhido nada, nada acontece
				for (int i = 0; i < near.size(); i++) {
					near.remove(0);
				}
			}else {
				controle = null;
			}
			near = null;
			hold = false;
			escolha = 0;
		}
		if (atacar) {
			// aqui ele cria uma lista com todas as entidades próximas para entrar em uma delas
			hold = true;
			near = new ArrayList<Entity>();
			for (int i = 0; i < Game.meteoros.size(); i++) {
				Meteor m = Game.meteoros.get(i);
				if (this.distancia(this.getX(), this.getY(), m.getX(), m.getY()) <= 100) {
					near.add(m);
				}
			}
			for (int i = 0; i < Game.azul.size(); i++) {
				Nave m = Game.azul.get(i);
				if (this.distancia(this.getX(), this.getY(), m.getX(), m.getY()) <= 100) {
					near.add(m);
				}
			}
			for (int i = 0; i < Game.verde.size(); i++) {
				Nave m = Game.verde.get(i);
				if (this.distancia(this.getX(), this.getY(), m.getX(), m.getY()) <= 100) {
					near.add(m);
				}
			}
			for (int i = 0; i < Game.vermelho.size(); i++) {
				Nave m = Game.vermelho.get(i);
				if (this.distancia(this.getX(), this.getY(), m.getX(), m.getY()) <= 100) {
					near.add(m);
				}
			}
			for (int i = 0; i < Game.amarelo.size(); i++) {
				Nave m = Game.amarelo.get(i);
				if (this.distancia(this.getX(), this.getY(), m.getX(), m.getY()) <= 100) {
					near.add(m);
				}
			}
			for (int i = 0; i < Game.rosa.size(); i++) {
				Nave m = Game.rosa.get(i);
				if (this.distancia(this.getX(), this.getY(), m.getX(), m.getY()) <= 100) {
					near.add(m);
				}
			}
		}
		if (colidindo == null && controle != null) {
			colisao();
		}else if (colidindo != null){
			if (!(isColidding(colidindo, this))) {
				colidindo = null;
			}
		}
		if (Mundo.isFree((int)(x+vx), (int)y)){
			x += vx;
		}else {
			if (vx > 0) {
				vxr = 0;
			}else {
				vxl = 0;
			}
		}
		if (Mundo.isFree((int)(x), (int) (y+vy))) {
			y += vy;
		}else {
			if (vy > 0) {
				vyd = 0;
			}else {
				vyu = 0;
			}
		}
		if (controle != null) {
			mx = controle.mx;
			my = controle.my;
			mheight = controle.mheight;
			mwidth = controle.mwidth;
		}
		if (life <= 0) {
			//life = maxLife;
			Game.GameState = "novorank";
		}
		/*for (int i = 0; i < Game.inimigos.size(); i++) {
			Enemy e = Game.inimigos.get(i);
			if (isColidding(this, e)) {
				Game.entities.remove(e);
				Game.inimigos.remove(e);
				life--;
				return;
			}
		}*/
	}
	public void colisao() {
		for (int i = 0; i < Game.meteoros.size(); i++) {
			Meteor m = Game.meteoros.get(i);
			if (isColidding(m, this) && m != controle) {
				colidindo = m;
				int tx = getX(), ty=getY(), cx=m.getX(), cy=m.getY();
				if (tx < cx) {
					//nave esta a esquerda do objeto
					vxl = vxr/4;
					m.vx += vxr/3;
					vxr = 0;
				}else if (tx > cx){
					vxr = vxl/4;
					m.vx += -1*vxr/3;
					vxl = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vx= v*Math.cos(Math.toRadians(rotate));
				
				if (ty < cy) {
					//nave esta acima do objeto
					vyu = vyd/4;
					m.vy += vyd/3;
					vyd = 0;
				}else if (ty > cy){
					vyd = vyu/4;
					m.vy += vyd/3*-1;
					vyu = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vy= v*Math.sin(Math.toRadians(rotate));
				controle.life -= vx+vy;
				return;
			}
		}
		for (int i = 0; i < Game.azul.size(); i++) {
			Nave m = Game.azul.get(i);
			if (isColidding(m, this) && m != controle) {
				colidindo = m;
				int tx = getX(), ty=getY(), cx=m.getX(), cy=m.getY();
				if (tx < cx) {
					//nave esta a esquerda do objeto
					vxl = vxr/4;
					m.vx += vxr/3;
					vxr = 0;
				}else if (tx > cx){
					vxr = vxl/4;
					m.vx += -1*vxr/3;
					vxl = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vx= v*Math.cos(Math.toRadians(rotate));
				
				if (ty < cy) {
					//nave esta acima do objeto
					vyu = vyd/4;
					m.vy += vyd/3;
					vyd = 0;
				}else if (ty > cy){
					vyd = vyu/4;
					m.vy += vyd/3*-1;
					vyu = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vy= v*Math.sin(Math.toRadians(rotate));
				controle.life -= vx+vy;
				return;
			}
		}
		for (int i = 0; i < Game.verde.size(); i++) {
			Nave m = Game.verde.get(i);
			if (isColidding(m, this) && m != controle) {
				colidindo = m;
				int tx = getX(), ty=getY(), cx=m.getX(), cy=m.getY();
				if (tx < cx) {
					//nave esta a esquerda do objeto
					vxl = vxr/4;
					m.vx += vxr/3;
					vxr = 0;
				}else if (tx > cx){
					vxr = vxl/4;
					m.vx += -1*vxr/3;
					vxl = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vx= v*Math.cos(Math.toRadians(rotate));
				
				if (ty < cy) {
					//nave esta acima do objeto
					vyu = vyd/4;
					m.vy += vyd/3;
					vyd = 0;
				}else if (ty > cy){
					vyd = vyu/4;
					m.vy += vyd/3*-1;
					vyu = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vy= v*Math.sin(Math.toRadians(rotate));
				controle.life -= vx+vy;
				return;
			}
		}
		for (int i = 0; i < Game.vermelho.size(); i++) {
			Nave m = Game.vermelho.get(i);
			if (isColidding(m, this) && m != controle) {
				colidindo = m;
				int tx = getX(), ty=getY(), cx=m.getX(), cy=m.getY();
				if (tx < cx) {
					//nave esta a esquerda do objeto
					vxl = vxr/4;
					m.vx += vxr/3;
					vxr = 0;
				}else if (tx > cx){
					vxr = vxl/4;
					m.vx += -1*vxr/3;
					vxl = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vx= v*Math.cos(Math.toRadians(rotate));
				
				if (ty < cy) {
					//nave esta acima do objeto
					vyu = vyd/4;
					m.vy += vyd/3;
					vyd = 0;
				}else if (ty > cy){
					vyd = vyu/4;
					m.vy += vyd/3*-1;
					vyu = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vy= v*Math.sin(Math.toRadians(rotate));
				controle.life -= vx+vy;
				return;
			}
		}
		for (int i = 0; i < Game.amarelo.size(); i++) {
			Nave m = Game.amarelo.get(i);
			if (isColidding(m, this) && m != controle) {
				colidindo = m;
				int tx = getX(), ty=getY(), cx=m.getX(), cy=m.getY();
				if (tx < cx) {
					//nave esta a esquerda do objeto
					vxl = vxr/4;
					m.vx += vxr/3;
					vxr = 0;
				}else if (tx > cx){
					vxr = vxl/4;
					m.vx += -1*vxr/3;
					vxl = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vx= v*Math.cos(Math.toRadians(rotate));
				
				if (ty < cy) {
					//nave esta acima do objeto
					vyu = vyd/4;
					m.vy += vyd/3;
					vyd = 0;
				}else if (ty > cy){
					vyd = vyu/4;
					m.vy += vyd/3*-1;
					vyu = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vy= v*Math.sin(Math.toRadians(rotate));
				controle.life -= vx+vy;
				return;
			}
		}
		for (int i = 0; i < Game.rosa.size(); i++) {
			Nave m = Game.rosa.get(i);
			if (isColidding(m, this) && m != controle) {
				colidindo = m;
				int tx = getX(), ty=getY(), cx=m.getX(), cy=m.getY();
				if (tx < cx) {
					//nave esta a esquerda do objeto
					vxl = vxr/4;
					m.vx += vxr/3;
					vxr = 0;
				}else if (tx > cx){
					vxr = vxl/4;
					m.vx += -1*vxr/3;
					vxl = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vx= v*Math.cos(Math.toRadians(rotate));
				
				if (ty < cy) {
					//nave esta acima do objeto
					vyu = vyd/4;
					m.vy += vyd/3;
					vyd = 0;
				}else if (ty > cy){
					vyd = vyu/4;
					m.vy += vyd/3*-1;
					vyu = 0;
				}
				v = vxl - vxr;
				
				if (v < 0) {
					v *= -1;
				}
				
				vy= v*Math.sin(Math.toRadians(rotate));
				controle.life -= vx+vy;
				return;
			}
		}
	}
	public void render(Graphics g) {
		if (controle != null) {
			controle.render(g);
		}else {
			g.drawImage(sprites[index], getX()-Camera.x, getY()-Camera.y, null);
		}
	}

}
