package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Gerador;

public class Tile {
	public static BufferedImage TILE_Vazio = Gerador.chaos.getAsset(0);

	private ArrayList<BufferedImage> sprites;
	private int x, y;
	public boolean solid = false;
	
	public Tile(int x,int y){
		this.x = x;
		this.y = y;
		sprites = new ArrayList<BufferedImage>();
	}
	
	public void adicionarsprite(BufferedImage sprite) {
		sprites.add(sprite);
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
	}

}
