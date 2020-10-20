package entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Game;
import world.AStar;
import world.Camera;
import world.FloorTile;
import world.Mundo;
import world.Tile;
import world.Vector2i;

public class Secador extends Entity {
	
	private int Ix, Iy;
	
	public Secador(double x, double y, int width, int height, double speed, BufferedImage sprite) {
		super(x, y, width, height, speed, sprite);
		Ix = getX();
		Iy = getY();
		this.speed = 4;
	}
	
	public void tick() {
		if (explorar) {
			//updateCamera();
			int px = this.getX()/16, py = this.getY()/16;
			if (Mundo.tiles[px+py*Mundo.WIDTH].estado == 3) {
				Mundo.tiles[px+py*Mundo.WIDTH].estado++;
				Network.SeExplorados.add(Mundo.tiles[px+py*Mundo.WIDTH]);
				Network.SeConhecidos.remove(Mundo.tiles[px+py*Mundo.WIDTH]);
			}
			if((path == null || path.size() == 0) && Network.SeConhecidos.size() > 0) {
				Vector2i start = new Vector2i( (int)(this.getX()/16),(int)this.getY()/16);
				alvo = maisProximo();
				if (alvo != null) {
					Vector2i end = new Vector2i(alvo.x/16, alvo.y/16);
					path = AStar.findPath(Game.world, start, end);
				}
			}
			if (alvo != null && getX() == alvo.x && getY() == alvo.y) {
				path = null;
			}else {
				followPath(path);
			}
			if (Network.SeConhecidos.size() == 0) {
				explorar = false;
				Vector2i start = new Vector2i( (int)(this.getX()/16),(int)this.getY()/16);
				Vector2i end = new Vector2i(Ix/16, Iy/16);
				path = AStar.findPath(Game.world, start, end);
				irParaCasa= true;
			}
		}
		if (irParaCasa) {
			if (path != null && path.size() > 0) {
				followPath(path);
			}else {
				irParaCasa = false;
			}
		}
	}
	
	private Tile maisProximo() {
		double d = -1, d1;
		int k = 0;
		for (int i = 0; i < Network.SeConhecidos.size(); i++) {
			Tile z = Network.SeConhecidos.get(i);
			boolean pode = true;
			for (int l = 0; l < Game.network.agentes.size(); l++) {
				if (Game.network.agentes.get(l).alvo == z) {
					pode = false;
				}
			}
			if (pode) {
				d1 = distancia(this.getX(), this.getY(), z.x, z.y);
				if (d == -1 || d1 < d) {
					d = d1;
					k = i;
				}
			}
		}
		if (Network.SeConhecidos.size() > 0) {
			return Network.SeConhecidos.get(k);
		}
		return null;
	}
	
	
	public void render(Graphics g) {
		//Graphics2D g2 = (Graphics2D) g;
		g.setColor(Color.yellow);
		g.fillRect(getX() - Camera.x, getY() - Camera.y, Game.TS, Game.TS);
	}

}
