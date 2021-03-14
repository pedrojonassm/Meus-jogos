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
		for (int i = 0; i < Ui.max_tiles_nivel; i++) {
			sprites.add(new ArrayList<int[]>());
		}
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
	
	public void carregar_sprites(String linha) {
		sprites.clear();
		String[] sla = linha.split(";"), sla2 = sla[1].split("-");
		for (int i = 0; i < Integer.parseInt(sla[0]); i++) {
			ArrayList<int[]> sprite = new ArrayList<int[]>();
			String[] sla3 = sla2[i].split(":");
			for (int k = 0; k < Integer.parseInt(sla3[0]); k++) {
				String[] s = sla3[k+1].split("a");
				int[] a = {Integer.parseInt(s[0]), Integer.parseInt(s[1])};
				sprite.add(a);
			}
			sprites.add(sprite);
		}
	}
	
	public void render(Graphics g){
		for (ArrayList<int[]> imagens : sprites) {
			if (imagens != null && imagens.size() > 0) {
				int[] sprite = imagens.get(World.tiles_index%imagens.size());
				BufferedImage image = World.sprites_do_mundo.get(sprite[0])[sprite[1]];
				if (image.getWidth() > Gerador.quadrado.width || image.getHeight() > Gerador.quadrado.height) g.drawImage(image, x - Camera.x - Gerador.quadrado.width, y - Camera.y - Gerador.quadrado.height, null);
				else g.drawImage(image, x - Camera.x, y - Camera.y, null);
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
		if (Ui.array.size() == 0 && sprites.size() < Ui.tiles_nivel && sprites.size() > 0) {
			sprites.set(Ui.tiles_nivel, null);
			return;
		}
		for (int i = 0; i < Ui.sprite_selecionado.size(); i++) {
			int[] a = {Ui.array.get(i), Ui.lista.get(i)};
			novo.add(a);
		}
		if (sprites.size() > Ui.tiles_nivel || (sprites.size() > Ui.tiles_nivel && sprites.get(Ui.tiles_nivel) == null))	sprites.set(Ui.tiles_nivel, novo);
		else sprites.add(novo);
	}

	public void pegarsprites() {
		ArrayList<int[]> sprite = sprites.get(Ui.tiles_nivel);
		if (sprite == null || sprite.size() == 0) {
			return;
		}
		Ui.pegar_tile_ja_colocado(sprite);
	}
	
	public void setX(int x) {
		this.x = x;
	}
	public void setY(int y) {
		this.y = y;
	}
	
	public String salvar() {
		String retorno = "";
		
		retorno += sprites.size()+";";
		for (ArrayList<int[]> sprite : sprites) {
			retorno += sprite.size();
			for (int[] a : sprite) {
				retorno += ":"+a[0] + "a" + a[1];
			}
			retorno += "-";
		}
		return retorno += "\n";
	}

}
