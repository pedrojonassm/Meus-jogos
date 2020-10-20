package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	public String[] options = {"Novo Jogo","Carregar jogo", "Sair"};
	public int opcao = 0, totalopcoes = options.length-1;
	public static boolean up, down, selecionado, pause = false;
	
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
				//
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
		g.setFont(new Font("Arial", Font.BOLD, 36));
		g.drawString("Meu Jogo #1", Game.WIDTH*Game.SCALE/2-80, Game.HEIGHT*Game.SCALE/2-100);
		g.setFont(new Font("Arial", Font.BOLD, 24));
		for (int i = 0; i < options.length; i++) {
			if(i == opcao) {
				g.setColor(Color.green);
			}
			if (i == 0 && pause) {
				g.drawString("Continuar", Game.WIDTH*Game.SCALE/2-80, Game.HEIGHT*Game.SCALE/2-100+(50*(i+1)));
			}else {
				g.drawString(options[i], Game.WIDTH*Game.SCALE/2-80, Game.HEIGHT*Game.SCALE/2-100+(50*(i+1)));
			}
			g.setColor(Color.white);
		}
	}
}
