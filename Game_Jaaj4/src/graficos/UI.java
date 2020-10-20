package graficos;


import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import main.Game;
import world.Camera;

public class UI {
	private int bv = 100; //bv = barra de vida
	public void render(Graphics g) {
		if (Game.GameState == "normal") {
			g.setColor(Color.red);
			g.fillRect(10, 10, bv, 30);
			g.setColor(Color.green);
			g.fillRect(10, 10, (int) ((Game.player.life*bv)/Game.player.maxLife), 30);
			if (Game.player.controle != null) {
				g.setColor(Color.gray);
				g.fillRect(10, 40, bv, 30);
				g.setColor(Color.blue);
				g.fillRect(10, 40, (int) ((Game.player.controle.life*bv)/Game.player.controle.maxLife), 30);
			}
			g.setColor(Color.white);
			String pontos = ""+Game.pontos;
			g.setFont(new Font("Arial", 18, 18));
			int w1 = g.getFontMetrics().stringWidth(pontos);
			g.drawString(pontos, Game.WIDTH*Game.SCALE-w1, 18);
		}
		/*String coor = "Coordenadas: "+"X: "+Game.player.getX()+"\tY: "+Game.player.getY();
		g.drawString(coor, 0, 18);*/
		//String sla = "Velocidade: "+Game.planetas.get(0).life;
		//g.drawString(sla, 0, 18);
	}
	
}
