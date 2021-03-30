package world;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import graficos.Ui;
import main.Gerador;

public class Tile {
	private ArrayList<ArrayList<int[]>> sprites;
	private int x, y, z,
	stairs_type, stairs_direction; // stairs_type 0 = não tem, 1 = escada "normal", 2 = escada de clique direito, 3 = buraco sempre aberto, 4 = Buraco fechado (usar picareta ou cavar para abrí-lo); direction 0 = direita, 1 = baixo, 2 = esquerda, 3 = cima
	private boolean solid, aberto_ou_fechado; // usado para paredes; usado em conjunto para ver se esta aberto ou fechado
	private int[] sprite_fechado, sprite_aberto; // sprites de reações
	
	public Tile(int x,int y,int z){
		solid = false;
		aberto_ou_fechado = true;
		stairs_type = 0;
		stairs_direction = 0;
		this.x = x;
		this.y = y;
		this.z = z;
		sprite_fechado = sprite_aberto = null;
		sprites = new ArrayList<ArrayList<int[]>>();
		for (int i = 0; i < Ui.max_tiles_nivel; i++) {
			sprites.add(new ArrayList<int[]>());
		}
	}
	
	public boolean getSolid(){
		return solid;
	}
	
	public void setSolid(boolean solid) {
		if (stairs_type == 0) this.solid = solid;
	}
	
	public int getStairs_type() {
		return stairs_type;
	}
	public int getStairs_direction() {
		return stairs_direction;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public int getZ() {
		return z;
	}
	
	public void carregar_sprites(String linha) {
		sprites.clear();
		String[] sla = linha.split(";"), sla2 = sla[1].split("-"), sla4 = sla[2].split("-");
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
		stairs_type = Integer.parseInt(sla4[0]);
		stairs_direction = Integer.parseInt(sla4[1]);
		if (Integer.parseInt(sla4[2]) == 1) {
			solid = true;
		}
		if (stairs_type == 4) {
			String[] sprites_buraco = sla[3].split("-"), fechado = sprites_buraco[0].split("a"), aberto = sprites_buraco[1].split("a");
			sprite_fechado = new int[2];
			sprite_aberto = new int[2];
			for (int i = 0; i < 2; i++) {
				sprite_fechado[i] = Integer.parseInt(fechado[i]);
				sprite_aberto[i] = Integer.parseInt(aberto[i]);
			}
		}
	}
	
	public void render(Graphics g){
		for (ArrayList<int[]> imagens : sprites) {
			if (imagens != null && imagens.size() > 0) {
				int[] sprite = imagens.get(World.tiles_index%imagens.size());
				int dx, dy;
				BufferedImage image = World.sprites_do_mundo.get(sprite[0])[sprite[1]];
				if (image.getWidth() > Gerador.quadrado.width || image.getHeight() > Gerador.quadrado.height) {
					dx = x - Camera.x - Gerador.quadrado.width;
					dy = y - Camera.y - Gerador.quadrado.height;
				}
				else {
					dx = x - Camera.x;
					dy = y - Camera.y;
				}
				dx -= (z-Gerador.player.getZ())*Gerador.quadrado.width;
				dy -= (z-Gerador.player.getZ())*Gerador.quadrado.height;
				g.drawImage(image, dx, dy, null);
			}
		}
		if (sprite_aberto != null) {
			BufferedImage image;
			int dx = x - Camera.x - (z-Gerador.player.getZ())*Gerador.quadrado.width, dy = y - Camera.y - (z-Gerador.player.getZ())*Gerador.quadrado.height;
			if (aberto_ou_fechado) {
				image = World.sprites_do_mundo.get(sprite_fechado[0])[sprite_fechado[1]];
			}else {
				image = World.sprites_do_mundo.get(sprite_aberto[0])[sprite_aberto[1]];
			}
			g.drawImage(image, dx, dy, null);
		}
		
		if (Ui.colocar_parede && solid) {
			g.setColor(new Color(255, 0, 0, 50));
			g.fillRect(x - Camera.x-(z-Gerador.player.getZ())*Gerador.quadrado.width, y - Camera.y-(z-Gerador.player.getZ())*Gerador.quadrado.height, Gerador.TS, Gerador.TS);
		}else if (Ui.colocar_escada && stairs_type != 0) {
			int[] cor = {255, 255, 255};
			if (stairs_type != 4) cor[stairs_type-1] = 0;
			g.setColor(new Color(cor[0], cor[1], cor[2], 50));
			g.fillRect(x - Camera.x-(z-Gerador.player.getZ())*Gerador.quadrado.width, y - Camera.y-(z-Gerador.player.getZ())*Gerador.quadrado.height, Gerador.TS, Gerador.TS);
		}
	}

	public void trocar_solid() {
		if (stairs_type == 0) solid = !solid;
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
	
	public boolean adicionar_sprite_reajível() {
		if (sprite_fechado != null && Ui.sprite_selecionado.size() == 0) {
			sprite_aberto = sprite_fechado = null;
			return true;
		}
		if (Ui.sprite_selecionado.size() != 2)	{
			JOptionPane.showMessageDialog(null, "Necessário ter 2 esprites selecionados, o primeiro representa o fechado, enquanto o segundo aberto");
			return false;
		}
		sprite_fechado = new int[2];
		sprite_fechado[0] = Ui.array.get(0);
		sprite_fechado[1] = Ui.lista.get(0);
		sprite_aberto = new int[2];
		sprite_aberto[0] = Ui.array.get(1);
		sprite_aberto[1] = Ui.lista.get(1);
		aberto_ou_fechado = true;
		return true;
	}

	public void pegarsprites() {
		ArrayList<int[]> sprite;
		if (!Ui.sprite_reajivel) {
			sprite = sprites.get(Ui.tiles_nivel);
		}else {
			sprite = new ArrayList<int[]>();
			sprite.add(sprite_fechado);
			sprite.add(sprite_aberto);
		}
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
	public void setZ(int z) {
		this.z = z;
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
		// stairs_type 0 = não tem, 1 = escada "normal", 2 = escada de clique direito; mode 1 = subir, -1 = descer
		retorno += ";"+stairs_type+"-"+stairs_direction+"-";
		if (solid) {
			retorno+="1";
		}else {
			retorno+="0";
		}
		if (sprite_aberto != null) {
			retorno += ";"+sprite_fechado[0]+"a"+sprite_fechado[1]+"-"+sprite_aberto[0]+"a"+sprite_aberto[1];
		}
		return retorno += "\n";
	}

	public boolean existe() {
		for (ArrayList<int[]> spr : sprites) {
			if (spr.size() > 0) {
				return true;
			}
		}
		return false;
	}

	public void virar_escada() {
		if (!solid) {
			if (Ui.modo_escadas == 3) {
				if (!adicionar_sprite_reajível()) return;
			}else if (Ui.modo_escadas == 2) {
				adicionar_sprite_selecionado(); // escada de clique direito
			}
			stairs_type = Ui.modo_escadas+1;
			stairs_direction = Ui.escadas_direction;
		}
	}

	public void desvirar_escada() {
		stairs_type = 0;
	}

	public boolean tem_sprites() {
		for (ArrayList<int[]> spr : sprites) {
			if (spr.size() > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean pode_descer_com_colisao() {
		if (stairs_type == 1 || stairs_type == 2 || stairs_type == 3 || (stairs_type == 4 && !aberto_ou_fechado)) {
			return true;
		}
		return false;
	}

	public boolean pode_subir_com_colisao() {
		if (stairs_type == 1) {
			return true;
		}
		return false;
	}

	public void reajir() {
		aberto_ou_fechado = !aberto_ou_fechado;
	}

}
