package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graficos.Ui;
import main.Gerador;

public class Tile {
	private ArrayList<BufferedImage[]> sprites;
	private int x, y;
	private boolean solid;
	
	public Tile(int x,int y){
		solid = false;
		this.x = x;
		this.y = y;
		sprites = new ArrayList<BufferedImage[]>();
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
		for (BufferedImage[] imagens : sprites) {
			BufferedImage sprite = imagens[World.tiles_index%imagens.length];
			g.drawImage(sprite, x - Camera.x, y - Camera.y, null);
		}
		if (Ui.colocar_parede && solid) {
			g.setColor(new Color(255, 0, 0, 50));
			g.fillRect(x - Camera.x, y - Camera.y, Gerador.TS, Gerador.TS);
		}
	}

	public void trocar_solid() {
		solid = !solid;
	}

	public void adicionar_sprite_selecionado() {
		BufferedImage[] novo = new BufferedImage[Ui.sprite_selecionado.size()];
		if (novo.length == 0) {
			sprites.remove(Ui.nivel);
			return;
		}
		for (int i = 0; i < Ui.sprite_selecionado.size(); i++) {
			novo[i] = World.sprites_do_mundo.get(Ui.array.get(i))[Ui.lista.get(i)];
		}
		if (sprites.size() > Ui.nivel)	sprites.set(Ui.nivel, novo);
		else sprites.add(novo);
	}

}
