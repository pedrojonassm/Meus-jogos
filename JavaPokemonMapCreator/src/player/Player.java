package player;

import java.awt.Color;
import java.awt.Graphics;

import main.Gerador;
import world.Camera;
import world.Tile;
import world.World;

public class Player {
	private int x, y, z;
	public int horizontal, vertical, speed;
	
	public Player(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		speed = 5;
		horizontal = vertical = 0;
	}
	
	public void tick() {
		if (x + horizontal*speed > 0 && x + horizontal*speed < World.WIDTH*Gerador.TS - Gerador.quadrado.width) {
			if (World.isFree(x + horizontal*speed, y, z)) {
				x += horizontal*speed;
			}else {
				int increase = 0;
				while (World.isFree(x + horizontal*(increase+1), y, z)) {
					increase++;
				}
				x += horizontal*increase;
			}
		}
		if (y + vertical*speed > 0 && y + vertical*speed < World.HEIGHT*Gerador.TS - Gerador.quadrado.height) {
			if (World.isFree(x, y + vertical*speed, z)) {
				y += vertical*speed;
			}else {
				int increase = 0;
				while (World.isFree(x, y + vertical*(increase+1),z)) {
					increase++;
				}
				y += vertical*increase;
			}
		}
		
		colidindo_com_escada();
		
		updateCamera();
	}
	
	private void colidindo_com_escada() {
		Tile t = World.pegar_chao(x+Gerador.TS/2, y+Gerador.TS/2, z);
		if (t.getStairs_type() == 1) {
			// subir
			z++;
			switch (t.getStairs_direction()) {
			case 0:
				x=t.getX()+Gerador.quadrado.width;
				break;
			case 1:
				y=t.getY()+Gerador.quadrado.height;
				break;
			case 2:
				x=t.getX()-Gerador.quadrado.width;
				break;
			case 3:
				y=t.getY()-Gerador.quadrado.height;
				break;
			}
			return;
		}
		t = World.pegar_chao(x+Gerador.TS/2, y+Gerador.TS/2, z-1);
		if (t.getStairs_type() == 1) {
			// descer
			z--;
			switch (t.getStairs_direction()) {
			case 0:
				x=t.getX()-Gerador.quadrado.width;
				break;
			case 1:
				y=t.getY()-Gerador.quadrado.height;
				break;
			case 2:
				x=t.getX()+Gerador.quadrado.width;
				break;
			case 3:
				y=t.getY()+Gerador.quadrado.height;
				break;
			}
			return;
		}
		
	}

	public int getX() {
		return x;
	}
	public void setX(int x) {
		this.x = x;
	}
	public int getY() {
		return y;
	}
	public void setY(int y) {
		this.y = y;
	}
	public int getZ() {
		return z;
	}
	public void setZ(int z) {
		this.z = z;
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(x - Gerador.WIDTH/2, 0, World.WIDTH*Gerador.TS - Gerador.WIDTH);
		Camera.y = Camera.clamp(y - Gerador.HEIGHT/2, 0, World.HEIGHT*Gerador.TS - Gerador.HEIGHT);
	}
	
	public void render(Graphics g) {
		g.setColor(Color.WHITE);
		g.fillRect(x - Camera.x, y - Camera.y, Gerador.quadrado.width, Gerador.quadrado.height);
		
	}

	public void camada(int acao) {
		int fz;
		if (acao < 0) {
			fz = z+1;
			if (fz >= World.HIGH) {
				fz = 0;
			}
			if (World.isFree(x, y, fz)) {
				z = fz;
			}
		}else if (acao > 0) {
			fz = z-1;
			if (fz < 0) {
				fz = World.HIGH-1;
			}
			if (World.isFree(x, y, fz)) {
				z = fz;
			}
		}
		
	}
}
