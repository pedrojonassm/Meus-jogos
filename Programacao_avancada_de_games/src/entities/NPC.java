package entities;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import main.Game;

public class NPC extends Entity {
	public String[] frases = new String[5];
	public boolean mensagem = false, show = false;
	public int curIndex = 0, frase = 0, time = 0, maxtime = 5;
	
	public NPC(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		frases[0] = "Olá! Bem vindo ao meu jogo";
		frases[1] = "Destrua os inimigos";
		frases[2] = "A arma está em algum lugar pelo mapa";
		frases[3] = "Segure a tecla de tiro para soltar uma descarga maior";
		frases[4] = "Creio não ter mais o que falar";
		this.depth = 2;
	}
	public void tick() {
		int xPlayer = Game.player.getX(), yPlayer = Game.player.getY();
		
		if(Math.abs(xPlayer - getX()) < 10 && Math.abs(yPlayer - getY()) < 10 && !show){
			mensagem = true;
			show = true;
		}else if (Math.abs(xPlayer - getX()) > 10 && Math.abs(yPlayer - getY()) > 10) {
			mensagem = false;
			show = false;
		}
		if(mensagem) {
			time++;
			if(time >= maxtime) {
				if(curIndex < frases[frase].length()) {
					curIndex++;
				}
				time = 0;
			}
		}else {
			curIndex = 0;
		}
	}
	public void render(Graphics g) {
		super.render(g);
		if(mensagem) {
			g.setColor(Color.black);
			g.fillRect(10, 10,  Game.WIDTH-20, Game.WIDTH-20);
			g.setColor(Color.blue);
			g.fillRect(12, 12,  Game.WIDTH-24, Game.WIDTH-94);
			g.setFont(new Font("Arial", Font.BOLD, 8));
			g.setColor(Color.white);
			g.drawString(frases[frase].substring(0, curIndex), (int) x - 50, (int) y);
			g.drawString(">Pressione enter para continuar", (int) x-50, (int) y+20);
			Game.GameState = "mensagem";
		}
	}
	
}
