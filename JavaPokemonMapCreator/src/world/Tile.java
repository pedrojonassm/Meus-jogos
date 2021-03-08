package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Gerador;

public class Tile {
	private ArrayList<BufferedImage> sprites;
	private int x, y;
	private boolean solid;
	
	public Tile(int x,int y){
		solid = false;
		this.x = x;
		this.y = y;
		sprites = new ArrayList<BufferedImage>();
	}
	
	public void adicionarsprite(BufferedImage sprite) {
		sprites.add(sprite);
	}
	
	public boolean getSolid(){
		return solid;
	}
	
	public void setSolid(boolean solid) {
		this.solid = solid;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public void render(Graphics g){
		for (BufferedImage sprite : sprites) {
			g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
		}
		if (World.ver_paredes_chaos_itens && solid) {
			g.setColor(new Color(255, 0, 0, 50));
			g.fillRect(x - Camera.x, y - Camera.y, Gerador.TS, Gerador.TS);
		}
	}

	public void trocar_solid() {
		solid = !solid;
	}

}
