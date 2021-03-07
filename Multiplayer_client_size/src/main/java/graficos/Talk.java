package graficos;

import java.awt.Graphics;

import main.Client;
import world.Camera;

public class Talk {// fala dos players que aparecem na tela
	private String frase;
	private long tempo;
	private int x, y;
	
	public Talk(String fala, int px, int py) {
		frase = fala;
		tempo = System.currentTimeMillis() + 1000*Ui.tempo_tela;
		x = px + Client.TS/2;
		y = py + Client.TS;
	}
	
	public void tick() {
		if  (System.currentTimeMillis() >= tempo) {
			Client.instance.ui.retirar_fala(this);;
		}
	}
	
	public void render(Graphics g) {
		int w1 = g.getFontMetrics().stringWidth(frase)/2;
		g.drawString(frase, x-w1-Camera.x, y+10-Camera.y);
	}

}
