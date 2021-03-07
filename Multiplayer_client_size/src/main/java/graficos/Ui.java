package graficos;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import entities.Entity;
import main.Client;
import main.Output;
import world.Camera;

public class Ui {
	public static final int tempo_tela = 10;
	private ArrayList<Talk> falas;
	
	public String look;
	private long sumir;
	// Caixinha de quantidade de itens ao mover
	public CaixinhaQuantidade caixinhaQuantidade;
	
	public Ui() {
		falas = new ArrayList<Talk>();
		
		caixinhaQuantidade = new CaixinhaQuantidade();
	}
	
	public void tick() {
		caixinhaQuantidade.tick();
		
		for (int i = 0; i < falas.size(); i++) {
			falas.get(i).tick();
		}
	}
	
	public void render(Graphics g) {
		
		for (int i = 0; i < falas.size(); i++) {
			falas.get(i).render(g);
		}
		if (System.currentTimeMillis() < sumir) {
			int w1 = g.getFontMetrics().stringWidth(look)/2;
			g.drawString(look, Client.instance.player.getX()+Client.TS/2-w1 - Camera.x, Client.instance.player.getY()+Client.TS+10 - Camera.y);
		}
		
		//*
		// teste das coordenadas do Client x[] e y[]
		g.setColor(Color.red);
		String str;
		for (int i = 0; i < Client.instance.max_players; i++) {
			str = "i: "+i+" x: "+Client.players[i].getX()+" y: "+Client.players[i].getY();
			g.drawString(str, 10, 20+i*15);
		}
		//*/
		
		caixinhaQuantidade.render(g);
	}
	
	public void retirar_fala(Talk fala) {
		falas.remove(fala);
	}
	
	public void adicionar_fala(String fala, int x, int y, boolean criou) {
		if (mostrar_pro_player(x, Client.instance.player.getX(), y, Client.instance.player.getY())) {
			Talk t = new Talk(fala, x, y);
			falas.add(t);
		}
		if (criou) {
			// enviar para o server
			Output.enviar_nova_fala_servidor(fala, x, y);
		}
	}
	
	public static boolean mostrar_pro_player(int x1, int x2, int y1, int y2) {
		// testando na minha maquina, a distancia ideal seria 750, mas usarei 800
		return Entity.distancia(x1, x2, y1, y2) < 800;
	}
	
	public void olhar(String fala) {
		look = fala;
		sumir = System.currentTimeMillis() + 1000*tempo_tela;
	}
}
