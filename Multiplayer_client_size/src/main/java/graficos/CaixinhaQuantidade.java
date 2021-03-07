package graficos;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.text.NumberFormat;

import javax.swing.JButton;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.text.NumberFormatter;

import itens.Item;
import main.Client;
import main.Output;
import world.World;

public class CaixinhaQuantidade {
	private Item item;
	private Rectangle
	menor, // é o retângulo que vai se mover para selecionar a quantidade
	maior, // é o retângulo que o menor se move dentro
	caixa, // é a caixinha em si
	fechar; // onde fecha
	private int mx, my;
	JFormattedTextField quant;
	JButton confirmar, cancelar;
	private int dx; // distancia do mouse ao menor apois clicar
	private boolean seguindo_o_mouse;
	
	// Ao mover um item, se control e shift não estiverem pressionados, essa caixa aparece
	public CaixinhaQuantidade() {
		caixa = new Rectangle(Client.WIDTH/2-Client.TS/2, Client.HEIGHT/2-Client.TS/2, Client.TS*3, Client.TS*2);
		menor = new Rectangle(Client.TI/2, Client.TI/2);
		maior = new Rectangle(caixa.x + caixa.width/2 - menor.width/2 - 50, caixa.y + caixa.height - menor.height*3, 100+menor.width, menor.height); // 100 é 100%
		fechar = new Rectangle(caixa.x+caixa.width-Client.TI/2, caixa.y, Client.TI/2, Client.TI/2);
		criar_botoes();
		menor.y = maior.y;
	}
	
	private void criar_botoes() {
		NumberFormat format = NumberFormat.getInstance();
	    NumberFormatter formatter = new NumberFormatter(format);
	    formatter.setValueClass(Integer.class);
	    formatter.setMinimum(0);
	    formatter.setMaximum(Integer.MAX_VALUE);
	    formatter.setAllowsInvalid(false);
	    formatter.setCommitsOnValidEdit(true);
	    quant = new JFormattedTextField(formatter);
	    quant.setBounds(maior.x+maior.width/2 - Client.TI/2, (int) (maior.y-maior.height*1.5), Client.TI, maior.height);
		
	    Color bg = new Color(0x65727B);
	    
	    confirmar = new JButton("yes");
	    cancelar = new JButton("no");
	    
	    quant.setBackground(bg);
	    confirmar.setBackground(bg);
	    cancelar.setBackground(bg);
	    
	    confirmar.setBorder(null);
	    cancelar.setBorder(null);
	    
	    confirmar.setBounds(maior.x, maior.y+maior.height, maior.width/2, maior.height);
	    cancelar.setBounds(maior.x+maior.width/2, maior.y+maior.height, maior.width/2, maior.height);
	    
	    cancelar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				cancelar_mover();
			}
		});
	    
		confirmar.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) {
				mover();
			}
		});
		
		quant.addKeyListener(new KeyListener() {
			
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					mover();
				}
				
			}
		});
		
	    quant.setVisible(false);
	    confirmar.setVisible(false);
	    cancelar.setVisible(false);
	}

	public void setItem(Item item, int mx, int my) {
		this.mx = mx;
		this.my = my;
		aparecer_ou_desaparecer();
		quant.setValue(0);
		this.item = item;
		menor.x = maior.x+Integer.parseInt(quant.getValue().toString());
	}
	
	private void aparecer_ou_desaparecer() {
		quant.setVisible(!quant.isVisible());
		confirmar.setVisible(!confirmar.isVisible());
		cancelar.setVisible(!cancelar.isVisible());
	}

	public void adicionar_no_frame(JFrame frame) {
		frame.add(quant);
		frame.add(confirmar);
		frame.add(cancelar);
	}
	
	public void tick() {
		if (item != null) {
			if (seguindo_o_mouse) {
				menor.x = Client.instance.quadrado.x - dx;
				int valor = (int) (((menor.x - maior.x)*item.getQuantidade())/100);
				
				if (valor >= item.getQuantidade()) {
					valor = item.getQuantidade();
					menor.x = maior.x+maior.width-menor.width;
				}else if (valor <= 0) {
					valor = 0;
					menor.x = maior.x;
				}
				quant.setValue(valor);
			}else if (Integer.parseInt(quant.getValue().toString()) <= item.getQuantidade())
				
				menor.x = maior.x+((100*Integer.parseInt(quant.getValue().toString()))/item.getQuantidade());
			else {
				quant.setValue(item.getQuantidade());
				menor.x = maior.x+maior.width-menor.width;
			}
		}
	}
	
	public void render(Graphics g) {
		if (item != null) {
			g.setColor(new Color(0x65727B));
			g.fillRect(caixa.x, caixa.y, caixa.width, caixa.height);
			
			g.setColor(Color.blue);
			g.drawRect(fechar.x, fechar.y, fechar.width, fechar.height);
			
			g.setColor(Color.black);
			g.drawLine(fechar.x, fechar.y, fechar.x+fechar.width, fechar.y+fechar.height);
			g.drawLine(fechar.x, fechar.y+fechar.height, fechar.x+fechar.width, fechar.y); // X
			
			g.setColor(new Color(0x1C1F22));
			g.fillRect(maior.x, maior.y, maior.width, maior.height);
			
			g.setColor(new Color(0x3E464C));
			g.fillRect(menor.x, menor.y, menor.width, menor.height);
			
			item.render(caixa.x+caixa.width/2-Client.TI/2, caixa.y+Client.TI/2, g);
		}
	}

	public boolean clicou(int x, int y) { // clique do mouse
		if (item == null || !caixa.contains(x, y)) {
			return false;
		}
		if (fechar.contains(x, y)) {
			item = null;
			aparecer_ou_desaparecer();
			return true;
		}else if (menor.contains(x, y)) {
			dx = x - menor.x;
			seguindo_o_mouse = true;
		}
		
		return true; // clicou na caixinha
	}
	
	public void parar_de_seguir_o_mouse() {
		this.seguindo_o_mouse = false;
	}
	
	public void cancelar_mover() {
		aparecer_ou_desaparecer();
		if (item != null) {
			if (item.getBag().equals(World.bag)) {
				Output.jogar_item_no_chao(item, item.x, item.y, item.z);
			}
			item = null;
		}
		parar_de_seguir_o_mouse();
		
	}
	
	public void mover() {
		parar_de_seguir_o_mouse();
		aparecer_ou_desaparecer();
		if (item != null) {
			Client.instance.player.mover_quantidade_x_de_item(mx, my, item, Integer.parseInt(quant.getValue().toString()));
		}
		item = null;
	}
}
