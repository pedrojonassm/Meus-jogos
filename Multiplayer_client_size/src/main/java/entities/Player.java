package entities;

import java.awt.Graphics;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import Inventario.Bag;
import itens.Item;
import world.Camera;
import world.WallTile;
import world.World;
import main.Client;
import main.Input;
import main.Output;
import main.SpriteSheets;

public class Player extends Entity{
	public boolean left, right, up, down;
	private boolean moved;
	private int playerid, speed, total_capturas, level, admin_power_level;
	Client client;
	private String name;
	
	private ArrayList<Item> itens_que_precisam_atualizar_id;
	
	private Bag inventario;
	private ArrayList<Bag> bags_abertas, bags_fechadas;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPlayerid(int playerid) {
		this.playerid = playerid;
		client.updateCoordinates(playerid, getX(), getY(), z, life, state, index, skin);
	}
	
	public void setInventario(Bag inventario) {
		this.inventario = inventario;
	}
	
	public void abrirInventario() {
		abrir_bag(inventario);
		skin++;
		if (skin >= total_skins) {
			skin = 0;
		}
	}
	
	public int getPlayerid() {
		return playerid;
	}
	
	public Player() {
		super(6*Client.TS, 6*Client.TS, 0);
		itens_que_precisam_atualizar_id = new ArrayList<Item>();
		name = "";
		max_animation_time = 9;
		bags_abertas = new ArrayList<Bag>();
		bags_fechadas = new ArrayList<Bag>();
		left = right = up = down = false;
		speed = 4;
		client = Client.instance;
		moved = false;
		totalStates = 8;
		total_skins = 12;
		
		adicionar_sprites();
	}
	
	@Override
	protected void adicionar_sprites() {
		super.adicionar_sprites(); // resetar as sprites
		// states: 
		for (int i = 0; i < total_skins; i++) { // adiciona todas as skin
			addSprites(1, 0, i, SpriteSheets.playerSprites);// 0 --> parado para baixo
			addSprites(2, 1, i, SpriteSheets.playerSprites);// 1 --> andando para baixo
			addSprites(1, 3, i, SpriteSheets.playerSprites);// 2 --> parado para cima
			addSprites(2, 4, i, SpriteSheets.playerSprites);// 3 --> andando para cima
			addSprites(1, 6, i, SpriteSheets.playerSprites);// 4 --> parado para a direita
			addSprites(2, 7, i, SpriteSheets.playerSprites);// 5 --> andando para a direita
			addSprites(1, 9, i, SpriteSheets.playerSprites);// 6 --> parado para a esquerda
			addSprites(2, 10, i, SpriteSheets.playerSprites);// 7 --> andando para a esquerda
		}
	}

	public void updateCamera() {
		Camera.x = Camera.clamp(x - Client.WIDTH/2, 0, World.WIDTH*Client.TS - Client.WIDTH);
		Camera.y = Camera.clamp(y - Client.HEIGHT/2, 0, World.HEIGHT*Client.TS - Client.HEIGHT);
	}
	
	public void descer_subir_bag(int mx, int my, int mw) {
		for (Bag b : bags_abertas) {
			//System.out.println("mx: "+mx+" my: "+my+" mw: "+mw);
			//System.out.println("bx: "+b.pegar_retangulo().x+" by: "+b.pegar_retangulo().y);
			if (b.pegar_retangulo().contains(mx, my)) {
				b.mover_linha(mw);
				return;
			}
		}
	}
	
	public void tick() {
		if (up) {
			state = 3;
			if(World.isFreeDynamic(x, y, Client.TS, Client.TS)) {
				y-=speed;
				moved = true;
			}
		}else if (down) {
			state = 1;
			if(World.isFreeDynamic(x, y, Client.TS, Client.TS)) {
				y+=speed;
				moved = true;
			}
		}if (left) {
			if(World.isFreeDynamic(x, y, Client.TS, Client.TS)) {
				x-=speed;
				moved = true;
				state = 7;
			}
		}else if (right) {
			state = 5;
			if(World.isFreeDynamic(x, y, Client.TS, Client.TS)) {
				x+=speed;
				moved = true;
			}
		}
		if (moved) {
			updateCamera();
			moved = false;
			client.updateCoordinates(playerid, x, y, z, life, state, index, skin);
			Output.enviar_player(this);
		}else {
			if (!(state%2 == 0)) { 
				state--;
				index = 0;
				client.updateCoordinates(playerid, x, y, z, life, state, index, skin);
				Output.enviar_player(this);
			}
		}
		super.tick();
	}
	
	public void render(Graphics g) {
		if (Client.instance.player == this || toRender(getX(), getY())) {
			// renderizar o personagem
			super.render(g);
			
			// renderizar a bag
			for (Bag b : bags_abertas) {
				b.render(g);
			}
		}
	}

	public void abrir_bag(Bag bag) {
		// abre uma bag fechada
		for (int i = 0; i < bags_fechadas.size(); i++) {
			if (bag.equals(bags_fechadas.get(i))) {
				bags_abertas.add(bags_fechadas.get(i));
				bags_fechadas.remove(i);
				bag.abrir_bag();
				return;
			}
		}
		
		// fecha uma bag aberta
		for (int i = 0; i < bags_abertas.size(); i++) {
			if (bag.equals(bags_abertas.get(i))) {
				bags_fechadas.add(bags_abertas.get(i));
				bags_abertas.remove(i);
				return;
			}
		}
	}
	
	public boolean mover_item(int mx, int my, Item item) {
		for (Bag b : bags_abertas) {
			if (b.pegar_retangulo().contains(mx,  my)) {
				return b.addItem(item, true);
			}
		}
		// se chegou aqui, é para jogar no mapa
		mx += Camera.x; my += Camera.y;
		if (!(World.pegar_chao(mx, my) instanceof WallTile)) { // se o item não for uma parede
			if (item.getBag() != null) item.getBag().remover_item(item);
			item.setBag(null);
			Output.jogar_item_no_chao(item, ((int) (mx>>5))<<5, ((int) (my>>5))<<5, getZ());
			if (item instanceof Bag) remover_bag((Bag) item);
			item = null;
			return true;
		}
		return false;
	}
	
	private void remover_bag(Bag bag) {
		for (Bag b : bags_fechadas) if (b.getItemIdBD() == bag.getItemIdBD()) {
			bags_fechadas.remove(b);
			return;
		}
		for (Bag b : bags_abertas) if (b.getItemIdBD() == bag.getItemIdBD()) {
			bags_abertas.remove(b);
			return;
		}
	}

	public void bag_estava_lotada(Item novo) {
		// tentou adicionar numa bag, mas ela estava lotada. Agora irá tentar adicionar em outras
		for (Bag b : bags_abertas) {
			if (b.addItem(novo, true)) {
				return;
			}
		}
		
		for (Bag b : bags_fechadas) {
			if (b.addItem(novo, true)) {
				return;
			}
		}
		
		// agora só falta fazer o item ser jogado no chão
		Output.jogar_item_no_chao(novo, getX() >> 5, getY() >> 5, getZ());
	}
	
	public boolean mover_quantidade_x_de_item(int mx, int my, Item item, int quant) {
		// mover quantidade x na mesma bag não está funcionando
		if (quant == 0) {
			return true;
		}else if (quant == item.getQuantidade()) {
			return mover_item(mx, my, item);
		}
		Item novo = Item.criar_item(-1, item.item, quant, 0);
		novo.setItem_origem(item.getItemIdBD());
		if (mover_item(mx, my, novo)){
			item.setQuantidade(item.getQuantidade()-quant);
			Output.criar_ou_atualizar_item(item);
			if (item.getQuantidade() <= 0 && item.getBag() != null) {
				item.getBag().remover_item(item);
			}
			novo.setItem_origem(-1);
			return true;
		}
		return false;
	}
	

	public boolean clicou_inventario(int mx, int my, int button) {
		for (Bag b : bags_abertas) {
			/*
			System.out.println("mx: "+mx+" my: "+my+" btn: "+button);
			System.out.println("bx: "+b.pegar_retangulo().x+" by: "+b.pegar_retangulo().y+ " width: "+b.pegar_retangulo().width+" height: "+b.pegar_retangulo().height);
			//*/
			if (b.pegar_janela().getPosicao().contains(mx, my)) {
				b.pegar_janela().clicou(mx, my, button);
				return true;
			}
			if (b.pegar_retangulo().contains(mx, my)) {
				b.clicou(mx, my, button);
				return true;
			}
		}
		return false;
	}
	
	public void adicionar_bag_fechada(Bag bag) {
		for (Bag b : bags_fechadas) if (bag.getBag_idBD() == b.getBag_idBD()) return;

		for (Bag b : bags_abertas) if (bag.getBag_idBD() == b.getBag_idBD()) return;
		bags_fechadas.add(bag);
		Output.pedir_itens_da_bag(bag);
		//Client.ou
	}
	
	public void receber_coisas(DataInputStream in, DataOutputStream out) {
		// Receber bags, level, xp, etc do server
		try {
			// tomar cuidado na ordem no dao personagens
			total_capturas = in.readInt();
			level = in.readInt();
			exp = in.readInt();
			admin_power_level = in.readInt();
			
			// receber as bags
			int total_bags = in.readInt();
			//System.out.println("total bags: "+total_bags);
			for (int i = 0; i < total_bags; i++) {
				// id da bag no bd; id do item no bd; tipo de item
				bags_fechadas.add(Bag.criar_bag(in.readInt(), in.readInt(), in.readInt()));
			}
			
			// pegar os itens agora
			for (Bag b : bags_fechadas) {
				out.writeInt(b.getBag_idBD()); // enviar o id da bag para receber os itens
				in.readInt(); // receber o 5 que não servirá de nada
				pegar_itens_da_bag_no_server(in, b);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void pegar_itens_da_bag_no_server(DataInputStream in, Bag b) throws IOException {
		int total_itens = in.readInt();
		for (int i = 0; i < total_itens; i++) {
			//id do item no bd; tipo do item; quantidade do item
			int id_item_bd = in.readInt(), tipo_item = in.readInt(), quantidade_item = in.readInt(), posicao_bags_fechadas = item_bag(id_item_bd);
			if (posicao_bags_fechadas == -1) {
				b.addItem(Item.criar_item(id_item_bd, tipo_item, quantidade_item, 0), false);
			}else {
				b.addItem(bags_fechadas.get(posicao_bags_fechadas), false);
			}
		}
	}

	private int item_bag(int id_item_bd) {
		// verifica se o item é uma bag que ja foi adicionada nas bags fechadas, caso seja, retorna a posição dela, caso não, retorna -1
		for (int i = 0; i < bags_fechadas.size(); i++) {
			if (bags_fechadas.get(i).getItemIdBD() == id_item_bd) {
				return i;
			}
		}
		return -1;
	}
	
	public void precisa_atualizar_id(Item item) {
		itens_que_precisam_atualizar_id.add(item);
	}

	public void atualizar_item_id(int id_antigo, int id_novo) {
		for (Item item : itens_que_precisam_atualizar_id) {
			if (item.getItemIdBD() == id_antigo) {
				item.setItemIdBD(id_novo);
				itens_que_precisam_atualizar_id.remove(item);
				return;
			}
		}
		
	}
}
