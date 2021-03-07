package Inventario;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

import entities.Player;
import itens.Item;
import main.Client;
import main.Output;

public class Bag extends Item {// pode ser uma bag ou o inventário do jogador
	private int total_slots, Bag_idBD;
	private ArrayList<Item> itens;
	private int x, y, width, height, linha_inicial = 0; // linha inicial é a linha que deve começar a renderizar a bag aberta
	private janelaBag janela;
	public boolean minimizar = false;
	
	public Bag(int itemIdBD, int bagIdBD, int tipo, int slots) {
		super(itemIdBD, tipo, 1, 1);
		this.Bag_idBD = bagIdBD; // id da bag no banco de dados do servidor
		item_name = "[falta adicionar o nome da bag]";
		total_slots = slots;
		itens = new ArrayList<Item>();
		width = Client.TI*5;
		height = (itens.size()/(width/Client.TI)+1)*Client.TI;
		
		x = Client.WIDTH/2-width/2;
		y = Client.HEIGHT/2;
		janela = new janelaBag(this);
	}
	
	public void setBag_idBD(int bag_idBD) {
		Bag_idBD = bag_idBD;
	}
	public int getBag_idBD() {
		return Bag_idBD;
	}
	
	public janelaBag getJanela() {
		return janela;
	}
	
	public void abrir_bag() {
		height = (itens.size()/(width/Client.TI)+1)*Client.TI;
	}
	
	public boolean addItem(Item item, boolean enviar_para_o_server) {
		/*
		if (item.getQuantidade() <= 0) {
		 
			if (item.getBag() != null) {
				item.getBag().remover_item(item);
			}
			return true;
		}//*/
		if (item.equals(this)) {
			return false;
		}
		/*
		 * item.getquantidade = 4
		 * quant = 1
		 * origem.bag = novo.bag
		 * novo.origem = origem.item
		 */
		if (item.getBag() != null && item.getBag().equals(this)) { // recoloca o item na mesma bag
			itens.remove(item);
			itens.add(item);
			return true;
		}
		
		for (int i = 0; i < itens.size() && !(item instanceof Bag); i++) {
			if (itens.get(i).item == item.item && item.getBag() != null && item.getBag() != itens.get(i).getBag() && (item.getItem_origem() == -1 || item.getItem_origem() != itens.get(i).getItemIdBD())) {
				int sobra = itens.get(i).adicionar_quantidade(item.getQuantidade());
				if (sobra == 0) {
					if (item.getBag() != null) {// se o item estava em uma bag
						item.getBag().remover_item(item);
					}
					item.setBag(this); // diz que o novo item está nessa bag
					item.setQuantidade(0);
					if (enviar_para_o_server) {
						Output.criar_ou_atualizar_item(item); // um item com 0 em quantidade é apagado do banco de dados
						Output.criar_ou_atualizar_item(itens.get(i)); // atualiza o item que ja estava na bag
					}
					return true; // guardou o item que ja tinha na bag
				}else {
					item.setQuantidade(sobra);
					if (enviar_para_o_server) {
						Output.criar_ou_atualizar_item(item);
						Output.criar_ou_atualizar_item(itens.get(i)); // atualiza o item que ja estava na bag
					}
				}
			}
		}
		
		// se chegar aqui, não havia o item na bag ou tinha mas já estava na quantidade máxima;
		if (itens.size() == total_slots) {
			return false; // não é possível adicionar o item na bag
		}
		if (item.getBag() != null) {// se o item estava em uma bag
			item.getBag().remover_item(item);
		}
		item.setBag(this); // diz que o novo item está nessa bag
		if (enviar_para_o_server) Output.criar_ou_atualizar_item(item);
		itens.add(item);
		
		if (item instanceof Bag) Client.instance.player.adicionar_bag_fechada((Bag) item);
		return true;
	}
	
	public void remover_item(Item item) {
		itens.remove(item);
		item.setBag(null);
	}
	
	public janelaBag pegar_janela() {
		return janela;
	}
	
	public void setar_posicao(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public Rectangle pegar_retangulo() {
		return new Rectangle(x, y, width, height);
	}
	
	@Override
	public void mouse_click(int button) {
		if (button == 3 && Client.instance.item_segurando == null) {
			Client.instance.player.abrir_bag(this);
			return;
		}
		super.mouse_click(button);
	}
	
	public void render(Graphics g) {
		if (!minimizar) {
			int linhas = height/Client.TI; // quantas linhas estão aparecendo
			for (int i = 0; i < linhas*width/Client.TI; i++) {
				if (i+linha_inicial*width/Client.TI < itens.size()) {
					int xi = x+((i+linha_inicial*width/Client.TI)%(width/Client.TI))*Client.TI, yi = y+((i+linha_inicial*width/Client.TI)/(width/Client.TI))*Client.TI - linha_inicial*Client.TI;
					itens.get(i+linha_inicial*width/Client.TI).render(xi, yi, g);
				}else if (linha_inicial*(width/Client.TI) + i < total_slots){
					g.setColor(Color.green);
					g.drawRect(x+((i+linha_inicial*width/Client.TI)%(width/Client.TI))*Client.TI, y+((i+linha_inicial*width/Client.TI)/(width/Client.TI))*Client.TI - linha_inicial*Client.TI, Client.TI, Client.TI);
				}
			}
			g.setColor(Color.white);
			g.drawRect(x, y, width, height);
		}
		janela.render(g);
	}

	public void mover_linha(int mw) {
		if (!minimizar) {
			linha_inicial += mw;
			int linhas_aparecendo = height/Client.TI;
			if (linha_inicial < 0) {
				linha_inicial = total_slots/(width/Client.TI) - linhas_aparecendo+1;
			}else if (linha_inicial > total_slots/(width/Client.TI) - linhas_aparecendo+1) {
				linha_inicial = 0;
			}
		}
	}

	public void clicou(int mx, int my, int button) { // button; 1 = left, 2 = middle, 3 = right click from mouse
		
		if (!minimizar) {
			int pos = (mx-x)/Client.TI+((my-y)/Client.TI + linha_inicial)*width/Client.TI;
			//System.out.println(pos);
			if (pos < itens.size()) {
				itens.get(pos).mouse_click(button);
			}
		}
		
	}

	public void setPosition(janelaBag janela2) {
		janela.getPosicao().x = janela2.getPosicao().x;
		janela.getPosicao().y = janela2.getPosicao().y;
		
	}

	public static Bag criar_bag(int id_bag_bd, int id_item_bd, int tipo_item) {
		Player p = Client.instance.player;
		//System.out.println("id bag: "+id_bag_bd+" id item: "+id_item_bd+ " tipo: "+tipo_item);
		Bag b = null;
		switch (tipo_item) {
			case 0:
				b = new Inventario(id_item_bd, id_bag_bd);
				p.setInventario(b);
				return b;
			case 6:
				return new simpleBag(id_item_bd, id_bag_bd);
		}
		return null;
	}
	

}
