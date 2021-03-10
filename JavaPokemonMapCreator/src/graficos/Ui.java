package graficos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import main.Gerador;
import world.World;

public class Ui {
	
	public static boolean mostrar, colocar_parede;
	private Rectangle colocar_paredes, caixinha_dos_sprites;
	private String colocar_as_paredes = "setar paredes";
	private int max_sprites_por_pagina, pagina, max_pagina;
	
	public Ui() {
		mostrar = true;
		colocar_parede = false;
		colocar_paredes = new Rectangle(Gerador.WIDTH-100, 20, 10, 10);
		caixinha_dos_sprites = new Rectangle(0, 8, Gerador.quadrado.width*5, Gerador.quadrado.width*11);
		max_sprites_por_pagina= (caixinha_dos_sprites.width/Gerador.quadrado.width)*(caixinha_dos_sprites.height/Gerador.quadrado.width);
		pagina = 0;
		max_pagina = 0; // quem setara o total de páginas máximas será o Wolrd
	}
	
	public void max_pagina_por_total_de_sprites(int total_sprites) {
		int divisao = ((caixinha_dos_sprites.width/Gerador.quadrado.width)*(caixinha_dos_sprites.height/Gerador.quadrado.width));
		max_pagina = (int) total_sprites/divisao;
		if (total_sprites%divisao > 0) {
			max_pagina++;
		}
	}
	
	public void tick() {
		
	}
	
	public void render(Graphics g) {
		if (mostrar) {
			g.setColor(Color.white);
			
			if (colocar_parede) g.fillRect(colocar_paredes.x, colocar_paredes.y, colocar_paredes.width, colocar_paredes.height);
			else g.drawRect(colocar_paredes.x, colocar_paredes.y, colocar_paredes.width, colocar_paredes.height);
			int w1 = g.getFontMetrics().stringWidth(colocar_as_paredes);
			g.drawString(colocar_as_paredes, colocar_paredes.x-colocar_paredes.width-w1, colocar_paredes.y+colocar_paredes.height);
			
			g.drawRect(caixinha_dos_sprites.x, caixinha_dos_sprites.y, caixinha_dos_sprites.width, caixinha_dos_sprites.height);
			desenhar_sprites_a_selecionar(g);
		}
	}

	private void desenhar_sprites_a_selecionar(Graphics g) {
		int comecar_por = pagina*max_sprites_por_pagina, atual = 0, sprites;
		for (sprites = 0; sprites < World.sprites_do_mundo.size() && atual < comecar_por; sprites++) {
			if (World.sprites_do_mundo.get(sprites).length <= comecar_por-atual) {
				atual += World.sprites_do_mundo.get(sprites).length;
			}else {
				atual += comecar_por-atual;
				break;
			}
		}
		for (int i = 0; i < sprites; i++) {
			atual -= World.sprites_do_mundo.get(i).length;
		}
		int desenhando = 0;
		while(sprites < World.sprites_do_mundo.size()) {
			desenhar_no_quadrado(World.sprites_do_mundo.get(sprites)[atual], desenhando, g);
			atual++;
			desenhando++;
			if (atual >= World.sprites_do_mundo.get(sprites).length) {
				sprites++;
				atual = 0;
			}
			if (desenhando >= max_sprites_por_pagina) {
				break;
			}
		}
	}

	private void desenhar_no_quadrado(BufferedImage bufferedImage, int desenhando, Graphics g) {
		int x = desenhando%(caixinha_dos_sprites.width/Gerador.quadrado.width), y = desenhando/(caixinha_dos_sprites.width/Gerador.quadrado.width);
		g.drawImage(bufferedImage, x*Gerador.quadrado.width+caixinha_dos_sprites.x, y*Gerador.quadrado.width+caixinha_dos_sprites.y, Gerador.quadrado.width, Gerador.quadrado.width, null);
		
	}

	public boolean clicou(int x, int y) {
		if (colocar_paredes.contains(x, y)) {
			colocar_parede = !colocar_parede;
			return true;
		}else if (caixinha_dos_sprites.contains(x, y)) {
			return true;
		}
		return false;
	}
	
	public boolean trocar_pagina(int x, int y, int rodinha) {
		if (mostrar) {
			if (caixinha_dos_sprites.contains(x, y)) {
				if (rodinha < 0) pagina--;
				else pagina++;
				if (pagina < 0) {
					pagina = max_pagina;
				}else if (pagina > max_pagina) {
					pagina = 0;
				}
				return true;
			}
		}
		return false;
	}

}
