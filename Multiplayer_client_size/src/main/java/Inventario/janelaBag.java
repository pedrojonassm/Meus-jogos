package Inventario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

import main.Client;

public class janelaBag {
	// Essa classe é o quadrado que fica acima da bag, para fechá-la e/ou minimizá-la
	Bag bag = null;
	private Rectangle posicao = null, fechar = null, minimizar = null;
	int dx, dy;
	
	public janelaBag(Bag b) {
		dx = dy = 0;
		bag = b;
		int height = 16;
		posicao = new Rectangle(b.pegar_retangulo().x, b.pegar_retangulo().y-height, b.pegar_retangulo().width, height);
		fechar = new Rectangle(height, height);
		minimizar = new Rectangle(height, height);
		posicoes();
	}
	
	public Rectangle getPosicao() {
		return posicao;
	}
	
	public Bag getBag() {
		return bag;
	}
	
	public void mover() {
		posicao.x = Client.instance.quadrado.x - dx;
		posicao.y = Client.instance.quadrado.y + dy;
		bag.setar_posicao(posicao.x, posicao.y+posicao.height);
		posicoes();
	}
	
	private void posicoes() {
		fechar.x = posicao.x - fechar.width + posicao.width;
		fechar.y = posicao.y;
		
		minimizar.x = fechar.x-minimizar.width;
		minimizar.y = fechar.y;
		
	}

	public void render(Graphics g) {
		g.setColor(new Color(0x4C4C4C));
		g.fillRect(posicao.x, posicao.y, posicao.width, posicao.height);
		
		g.setColor(Color.BLUE);
		g.drawRect(fechar.x, fechar.y, fechar.width, fechar.height);
		g.drawRect(minimizar.x, minimizar.y, minimizar.width, minimizar.height);
		
		// Desenhar um "-"
		g.setColor(Color.black);
		g.drawLine(minimizar.x, minimizar.y+minimizar.height/2, minimizar.x + minimizar.width, minimizar.y+minimizar.height/2);
		
		// Desenhar um "X"
		g.drawLine(fechar.x, fechar.y, fechar.x+fechar.width, fechar.y+fechar.height);
		g.drawLine(fechar.x, fechar.y+fechar.height, fechar.x+fechar.width, fechar.y);
	}

	public void clicou(int mx, int my, int button) {
		if (button == 1) {
			
			// distancia do clique para o inicio do quadrado
			dx = mx - posicao.x;
			dy = posicao.y - my;
			
			if(fechar.contains(mx, my)) {
				
				Client.instance.player.abrir_bag(bag);
				return;
			}else if(minimizar.contains(mx, my)) {
				bag.minimizar = !bag.minimizar;
				return;
			}
			Client.instance.movendo_janela = this;
			
		}
		
	}

}
