package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.File;

import world.Camera;

public class Menu {
	
	public String[] options = {"Novo Jogo", "Ver Ranks", "Sair"};
	public int opcao = 0, totalopcoes = options.length-1, codigo = 13;
	public boolean up, down, selecionado, pause = false;
	
	public void tick() {
		if(up) {
			up = false;
			opcao--;
			if(opcao<0) {
				opcao = totalopcoes;
			}
		}else if(down) {
			down = false;
			opcao++;
			if(opcao>totalopcoes){
				opcao = 0;
			}
		}
		if (selecionado) {
			selecionado = false;
			pause = false;
			if (opcao == 0) {
				Game.GameState = "normal";
			}else if(opcao == 1) {
				Game.GameState = "rank";
			}else {
				System.exit(1);
			}
		}
	}

	public void render(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE);
		g.setColor(Color.white);
		int w;
		g.setFont(new Font("Arial", Font.BOLD, 12));
		w = g.getFontMetrics().stringWidth("Meu Jogo #2 (Pac-Man)");
		g.drawString("Meu Jogo #2 (Pac-Man)", Game.WIDTH/2-w/2, Game.HEIGHT/2-30);
		g.setFont(new Font("Arial", Font.BOLD, 12));
		for (int i = 0; i < options.length; i++) {
			if(i == opcao) {
				g.setColor(Color.green);
			}
			if (i == 0 && pause) {
				w = g.getFontMetrics().stringWidth("continuar");
				g.drawString("Continuar", Game.WIDTH/2-w/2, Game.HEIGHT/2-100+(25*(i+1) -30));
			}else {
				w = g.getFontMetrics().stringWidth(options[i]);
				g.drawString(options[i], Game.WIDTH/2-w/2, Game.HEIGHT/2+(20*(i+1)-30));
			}
			g.setColor(Color.white);
		}
	}
}
