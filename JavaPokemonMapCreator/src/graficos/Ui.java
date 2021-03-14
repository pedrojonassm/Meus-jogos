package graficos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import files.salvarCarregar;
import main.Gerador;
import world.Tile;
import world.World;

public class Ui {
	
	public static boolean mostrar, colocar_parede;
	private Rectangle colocar_paredes, caixinha_dos_sprites, caixinha_dos_livros;
	private String colocar_as_paredes = "setar paredes", tile_nivel = "Nível nos tiles: ";
	private int max_sprites_por_pagina, livro, pagina_livros, max_pagina_livros, max_livros_por_pagina, livro_tile_pego, index_tile_pego;
	private ArrayList<Integer> pagina, max_pagina, comecar_por, atual, sprites;
	private static ArrayList<ArrayList<Tile>> tiles_salvos;
	private static ArrayList<String> nome_livros;
	public static ArrayList<Integer> sprite_selecionado, array, lista; // esses dois pegam a imagem na lista de imagens estáticas World.sprites.get(array)[lista]
	public static int tiles_nivel, max_tiles_nivel; // corresponde a qual sprite será guardado os sprites nos tiles ex: 0 = chao, 1 = paredes, 2 = decoracoes, etc.
	
	public Ui() {
		livro = 0; // os livros podem ser adicionados depois, a fim de criar novas páginas para maior facilidade de achar sprites
		pagina = new ArrayList<Integer>();
		max_pagina = new ArrayList<Integer>();
		comecar_por = new ArrayList<Integer>();
		atual = new ArrayList<Integer>();
		sprites = new ArrayList<Integer>();
		pagina.add(0);
		max_pagina.add(0);
		comecar_por.add(0);
		atual.add(0);
		sprites.add(0);
		tiles_salvos = new ArrayList<ArrayList<Tile>>();
		nome_livros = new ArrayList<String>();
		nome_livros.add("todos os sprites");
		mostrar = true;
		colocar_parede = false;
		colocar_paredes = new Rectangle(Gerador.WIDTH-100, 20, 10, 10);
		caixinha_dos_sprites = new Rectangle(0, 8, Gerador.quadrado.width*5, Gerador.quadrado.width*11);
		caixinha_dos_livros = new Rectangle(caixinha_dos_sprites.x + caixinha_dos_sprites.width, caixinha_dos_sprites.y, Gerador.quadrado.width/3, caixinha_dos_sprites.height);
		sprite_selecionado = new ArrayList<Integer>();
		array = new ArrayList<Integer>();
		lista = new ArrayList<Integer>();
		max_sprites_por_pagina= (caixinha_dos_sprites.width/Gerador.quadrado.width)*(caixinha_dos_sprites.height/Gerador.quadrado.width);
		pagina_livros = 0;
		max_livros_por_pagina = caixinha_dos_livros.height/caixinha_dos_livros.width;
		tiles_nivel = 0;
		max_tiles_nivel = 4;
	}
	
	public static String pegar_nome_livro(int index) {
		return nome_livros.get(index);
	}
	
	public static ArrayList<Tile> pegar_livro(int index) {
		return tiles_salvos.get(index);
	}
	
	public Rectangle getCaixinha_dos_sprites() {
		return caixinha_dos_sprites;
	}
	
	public void max_pagina_por_total_de_sprites(int total_sprites) {
		int divisao = ((caixinha_dos_sprites.width/Gerador.quadrado.width)*(caixinha_dos_sprites.height/Gerador.quadrado.width));
		max_pagina.set(0, (int) (total_sprites/divisao));
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		int w1;
		if (mostrar) {
			g.setColor(Color.white);
			
			if (colocar_parede) g.fillRect(colocar_paredes.x, colocar_paredes.y, colocar_paredes.width, colocar_paredes.height);
			else g.drawRect(colocar_paredes.x, colocar_paredes.y, colocar_paredes.width, colocar_paredes.height);
			w1 = g.getFontMetrics().stringWidth(colocar_as_paredes);
			g.drawString(colocar_as_paredes, colocar_paredes.x-colocar_paredes.width-w1, colocar_paredes.y+colocar_paredes.height);
			g.setColor(Color.black);
			g.fillRect(caixinha_dos_sprites.x, caixinha_dos_sprites.y, caixinha_dos_sprites.width, caixinha_dos_sprites.height);
			g.setColor(Color.white);
			g.drawRect(caixinha_dos_sprites.x, caixinha_dos_sprites.y, caixinha_dos_sprites.width, caixinha_dos_sprites.height);
			g.drawRect(caixinha_dos_livros.x, caixinha_dos_livros.y, caixinha_dos_livros.width, caixinha_dos_livros.height);
			desenhar_livros(g);
			desenhar_sprites_a_selecionar(g);
			if (caixinha_dos_livros.contains(Gerador.quadrado.x, Gerador.quadrado.y)) mostrar_nome_livro(g);
		}
		g.setColor(Color.white);
		w1 = g.getFontMetrics().stringWidth(tile_nivel+tiles_nivel);
		g.drawString(tile_nivel+tiles_nivel, colocar_paredes.x-w1 + colocar_paredes.width, colocar_paredes.y);
	}
	
	private void mostrar_nome_livro(Graphics g) {
		g.setColor(Color.white);
		int py = (Gerador.quadrado.y-caixinha_dos_livros.y)/caixinha_dos_livros.width + pagina_livros*max_livros_por_pagina;
		if (py >= max_livros_por_pagina*(pagina_livros+1)) return;
		String nome = null;
		if (py < nome_livros.size()) {
			nome = nome_livros.get(py);
		}else if (py == nome_livros.size()) {
			nome = "Adicionar novo livro";
		}
		if (nome != null) {
			g.drawString(nome, Gerador.quadrado.x + Gerador.quadrado.width, Gerador.quadrado.y+10);
		}
	}

	private void desenhar_livros(Graphics g) {
		int y = caixinha_dos_livros.y;
		g.setColor(Color.blue);
		int i;
		for (i = max_livros_por_pagina*pagina_livros; i < max_livros_por_pagina*(pagina_livros+1) && i < nome_livros.size(); i++) {
			if (i == livro) {
				g.setColor(Color.red);
				g.drawRect(caixinha_dos_livros.x, y, caixinha_dos_livros.width, caixinha_dos_livros.width);
				g.setColor(Color.blue);
			}else {
				g.drawRect(caixinha_dos_livros.x, y, caixinha_dos_livros.width, caixinha_dos_livros.width);
			}
			y+=caixinha_dos_livros.width;
		}
		if (i < max_livros_por_pagina*(pagina_livros+1)) {
			g.drawRect(caixinha_dos_livros.x, y, caixinha_dos_livros.width, caixinha_dos_livros.width);
			g.setColor(Color.green);
			g.drawLine(caixinha_dos_livros.x+caixinha_dos_livros.width/2, y, caixinha_dos_livros.x+caixinha_dos_livros.width/2, y+caixinha_dos_livros.width);
			g.drawLine(caixinha_dos_livros.x, y+caixinha_dos_livros.width/2, caixinha_dos_livros.x+caixinha_dos_livros.width, y+caixinha_dos_livros.width/2);
		}
	}

	public void atualizar_caixinha() {
		comecar_por.set(livro, pagina.get(livro)*max_sprites_por_pagina);
		int atual = 0, sprites = 0;
		for (sprites = 0; sprites < World.sprites_do_mundo.size() && atual < comecar_por.get(livro); sprites++) {
			if (World.sprites_do_mundo.get(sprites).length <= comecar_por.get(livro)-atual) {
				atual += World.sprites_do_mundo.get(sprites).length;
			}else {
				atual += comecar_por.get(livro)-atual;
				break;
			}
		}
		for (int i = 0; i < sprites; i++) {
			atual -= World.sprites_do_mundo.get(i).length;
		}
		this.atual.set(livro, atual);
		this.sprites.set(livro, sprites);
	}

	private void desenhar_sprites_a_selecionar(Graphics g) {
		int desenhando = 0, k = atual.get(livro), spr = sprites.get(livro);
		if (livro == 0) 
			while(spr < World.sprites_do_mundo.size()) {
				desenhar_no_quadrado(World.sprites_do_mundo.get(spr)[k], desenhando, g);
				k++;
				desenhando++;
				if (k >= World.sprites_do_mundo.get(spr).length) {
					spr++;
					k = 0;
				}
				if (desenhando >= max_sprites_por_pagina) {
					break;
				}
			}
		else {
			ArrayList<Tile> tiles = tiles_salvos.get(livro-1);
			int x, y;
			for (int i = 0; i < max_sprites_por_pagina && k < tiles.size(); i++) {
				x = (desenhando%(caixinha_dos_sprites.width/Gerador.quadrado.width))*Gerador.quadrado.width+caixinha_dos_sprites.x;
				y = (desenhando/(caixinha_dos_sprites.width/Gerador.quadrado.width))*Gerador.quadrado.width+caixinha_dos_sprites.y;
				tiles.get(i+(max_sprites_por_pagina*pagina.get(livro))).setX(x);
				tiles.get(i+(max_sprites_por_pagina*pagina.get(livro))).setY(y);
				tiles.get(i+(max_sprites_por_pagina*pagina.get(livro))).render(g);
				if (i+(max_sprites_por_pagina*pagina.get(livro)) == index_tile_pego && livro == livro_tile_pego) {
					g.setColor(new Color(0, 255, 0, 50));
					g.fillRect(x, y, Gerador.quadrado.width, Gerador.quadrado.height);
				}
				k++;
				desenhando++;
			}
			if (desenhando < max_sprites_por_pagina) {
				// desenhar o "+" para adicionar um novo sprite
				x = (desenhando%(caixinha_dos_sprites.width/Gerador.quadrado.width))*Gerador.quadrado.width+caixinha_dos_sprites.x;
				y = (desenhando/(caixinha_dos_sprites.width/Gerador.quadrado.width))*Gerador.quadrado.width+caixinha_dos_sprites.y;
				g.setColor(Color.green);
				g.drawRect(x, y, Gerador.quadrado.width, Gerador.quadrado.height);
				g.drawLine(x+Gerador.quadrado.width/2, y+Gerador.quadrado.width/5, x+Gerador.quadrado.width/2, y+Gerador.quadrado.height-Gerador.quadrado.width/5);
				g.drawLine(x+Gerador.quadrado.width/5, y+Gerador.quadrado.height/2, x+Gerador.quadrado.width-Gerador.quadrado.width/5, y+Gerador.quadrado.height/2);
			}
		}
	}

	private void desenhar_no_quadrado(BufferedImage bufferedImage, int desenhando, Graphics g) {
		int x = desenhando%(caixinha_dos_sprites.width/Gerador.quadrado.width), y = desenhando/(caixinha_dos_sprites.width/Gerador.quadrado.width);
		g.drawImage(bufferedImage, x*Gerador.quadrado.width+caixinha_dos_sprites.x, y*Gerador.quadrado.width+caixinha_dos_sprites.y, Gerador.quadrado.width, Gerador.quadrado.height, null);
		if (sprite_selecionado.contains(desenhando+max_sprites_por_pagina*pagina.get(livro))) {
			g.setColor(new Color(0, 255, 0, 50));
			g.fillRect(x*Gerador.quadrado.width+caixinha_dos_sprites.x, y*Gerador.quadrado.width+caixinha_dos_sprites.y, Gerador.quadrado.width, Gerador.quadrado.height);
		}
	}

	public boolean clicou(int x, int y) {
		if (colocar_paredes.contains(x, y)) {
			colocar_parede = !colocar_parede;
			return true;
		}else if (caixinha_dos_sprites.contains(x, y)) {
			pegar_ou_retirar_sprite_selecionado(x,y);
			return true;
		}else if (caixinha_dos_livros.contains(x, y)) {
			trocar_livro(x, y);
			return true;
		}
		return false;
	}
	
	private void trocar_livro(int x, int y) {
		int py = (y-caixinha_dos_livros.y)/caixinha_dos_livros.width + pagina_livros*max_livros_por_pagina;
		if (py == pagina.size()) {
			String nome = null;
			do {
				nome = JOptionPane.showInputDialog("Insira um nome que já não seja um nome do livro");
				if (nome == null || nome.isBlank()) return;
			} while (nome_livros.contains(nome));
			adicionar_livro(nome);
		}else if (py < pagina.size()) {
			livro = py;
		}
	}

	private void adicionar_livro(String nome) {
		pagina.add(0);
		max_pagina.add(0);
		comecar_por.add(0);
		atual.add(0);
		sprites.add(0);
		tiles_salvos.add(new ArrayList<Tile>());
		nome_livros.add(nome);
		max_pagina_livros = nome_livros.size()/max_livros_por_pagina;
	}

	private void pegar_ou_retirar_sprite_selecionado(int x, int y) {
		int px = x/Gerador.quadrado.width, py = (y-caixinha_dos_sprites.y)/Gerador.quadrado.height;
		int aux = px+py*(caixinha_dos_sprites.width/Gerador.quadrado.width);
		if (livro == 0) {
			if (!Gerador.control && sprite_selecionado.contains(aux+max_sprites_por_pagina*pagina.get(livro))) {
				sprite_selecionado.remove((Integer) (aux+max_sprites_por_pagina*pagina.get(livro)));
				int k = atual.get(livro), spr = sprites.get(livro), desenhando = 0;
				while(spr < World.sprites_do_mundo.size()) {
					if (desenhando == aux) {
						array.remove((Integer) spr);;
						lista.remove((Integer) k);;
						break;
					}
					k++;
					if (k >= World.sprites_do_mundo.get(spr).length) {
						spr++;
						k = 0;
					}
					desenhando++;
				}
			}else {
				sprite_selecionado.add(aux+max_sprites_por_pagina*pagina.get(livro));
				int k = atual.get(livro), spr = sprites.get(livro), desenhando = 0;
				while(spr < World.sprites_do_mundo.size()) {
					if (desenhando == aux) {
						array.add(spr);
						lista.add(k);
						break;
					}
					k++;
					if (k >= World.sprites_do_mundo.get(spr).length) {
						spr++;
						k = 0;
					}
					desenhando++;
				}
			}
		}else {
			aux = aux+(max_sprites_por_pagina*pagina.get(livro));
			if (aux == tiles_salvos.get(livro-1).size() && sprite_selecionado.size() > 0) {
				// clicou no "+"
				Tile tile = new Tile(0, 0);
				tile.adicionar_sprite_selecionado();
				tiles_salvos.get(livro-1).add(tile);
				if (tiles_salvos.get(livro-1).size() >= max_sprites_por_pagina) {
					max_pagina.set(livro, max_pagina.get(livro)+1);
				}
				tile.salvar();
			}else if (aux < tiles_salvos.get(livro-1).size()) {
				livro_tile_pego = livro;
				index_tile_pego = aux;
				tiles_salvos.get(livro-1).get(aux).pegarsprites();
			}
		}
		Gerador.sprite_selecionado_index = 0;
	}

	public boolean trocar_pagina(int x, int y, int rodinha) {
		if (mostrar) {
			int k = 0;
			if (rodinha < 0) k=-1;
			else k=1;
			if (caixinha_dos_sprites.contains(x, y)) {
				pagina.set(livro, pagina.get(livro)+k);
				if (pagina.get(livro) < 0) {
					pagina.set(livro, max_pagina.get(livro));
				}else if (pagina.get(livro) > max_pagina.get(livro)) {
					pagina.set(livro, 0);
				}
				atualizar_caixinha();
				return true;
			}else if (caixinha_dos_livros.contains(x, y)) {
				pagina_livros += k;
				if (pagina_livros < 0) {
					pagina_livros = max_pagina_livros;
				}else if  (pagina_livros > max_pagina_livros) {
					pagina_livros = 0;
				}
				return true;
			}
		}
		return false;
	}
	
	public static void trocar_Nivel(int wheelRotation) {
		if (wheelRotation > 0) {
			tiles_nivel++;
			if (tiles_nivel > max_tiles_nivel) {
				tiles_nivel = 0;
			}
		}else if (wheelRotation < 0) {
			tiles_nivel--;
			if (tiles_nivel < 0) {
				tiles_nivel = max_tiles_nivel;
			}
		}
	}

	public static void pegar_tile_ja_colocado(ArrayList<int[]> sprites) {
		sprite_selecionado.clear();
		array.clear();
		lista.clear();
		for (int[] a : sprites) {
			array.add(a[0]);
			lista.add(a[1]);
			adicionar_sprite_colocado_aos_selecionados(a[0], a[1]);
		}
	}
	
	private static void adicionar_sprite_colocado_aos_selecionados(int array, int lista) {
		int k = 0;
		for (int i = 0; i < array; i++) {
			k += World.sprites_do_mundo.get(i).length;
		}
		k+=lista;
		sprite_selecionado.add(k);
	}

	public boolean cliquedireito(int x, int y) {
		if (caixinha_dos_sprites.contains(x, y)) {
			Ui.sprite_selecionado.clear();
			Ui.array.clear();
			Ui.lista.clear();
			livro_tile_pego = -1;
			index_tile_pego = -1;
			return true;
		}
		return false;
	}

	public static void salvar() {
		for (int i = 0; i < tiles_salvos.size(); i++) {
			salvarCarregar.salvar_livro(i);
		}
	}

	public void adicionar_livro_salvo(String nome, ArrayList<Tile> tiles) {
		adicionar_livro(nome);
		tiles_salvos.set(tiles_salvos.size()-1, tiles);
	}
}
