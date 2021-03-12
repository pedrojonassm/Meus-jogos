package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import graficos.Ui;
import main.Gerador;

public class Tile {
	private ArrayList<ArrayList<int[]>> sprites;
	private int x, y;
	private boolean solid;
	
	public Tile(int x,int y){
		solid = false;
		this.x = x;
		this.y = y;
		sprites = new ArrayList<ArrayList<int[]>>();
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
		for (ArrayList<int[]> imagens : sprites) {
			if (imagens != null) {
				int[] sprite = imagens.get(World.tiles_index%imagens.size());
				g.drawImage(World.sprites_do_mundo.get(sprite[0])[sprite[1]], x - Camera.x, y - Camera.y, null);
			}
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
		ArrayList<int[]> novo = new ArrayList<int[]>();
		if (Ui.sprite_selecionado.size() == 0 && sprites.size() < Ui.tiles_nivel) {
			sprites.set(Ui.tiles_nivel, null);
			return;
		}
		for (int i = 0; i < Ui.sprite_selecionado.size(); i++) {
			int[] a = {Ui.array.get(i), Ui.lista.get(i)};
			novo.add(a);
		}
		if (sprites.size() > Ui.tiles_nivel || (sprites.size() > 0 && sprites.get(Ui.tiles_nivel) == null))	sprites.set(Ui.tiles_nivel, novo);
		else sprites.add(novo);
	}

}
