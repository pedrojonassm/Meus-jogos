package world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Gerador;

public class Tile {
	public static BufferedImage TILE_Vazio = Gerador.chaos.getSprite(0,0,Gerador.TS,Gerador.TS);

	private BufferedImage sprite;
	private int x, y;
	public boolean solid = false;
	
	public Tile(int x,int y,BufferedImage sprite){
		this.x = x;
		this.y = y;
		this.sprite = sprite;
	}
	
	public void render(Graphics g){
		g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
	}

}
