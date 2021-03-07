package main;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import dao.DaoBag;
import dao.DaoItem;
import world.World;

public class Game {
	private DataInputStream in;
	private DataOutputStream out;
	private int characterID;
	private int playerid;
	private boolean running;
	
	public Game(DataInputStream in, DataOutputStream out) {
		this.in = in;
		this.out = out;
	}
	
	public void setPlayerid(int playerid) {
		this.playerid = playerid;
	}
	
	public void start(int charID) {
		try {
			characterID = charID;
			running = true;
			pegar_coisas_do_char(charID);
			Server.world.enviar_itens_no_chao(out);
			enviar_outros_players();
			while(running) {
					switch(in.readInt()) {
						case 0:
							atualizar_player(in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt());
							break;
						case 1:
							// enviou uma conversa, agora deve enviar para os outros
							enviar_fala_para_todos(in.readUTF(), in.readInt(), in.readInt());
							break;
						case 2:
							// enviou um item que precisa ser atualizado
							acessar_daoitem_para_criar_ou_atualizar_item(in.readInt(), in.readInt(), in.readInt(), in.readInt());
							break;
						case 3:
							// jogado item no chão
							Server.world.adicionar_item(in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt(), in.readInt());
							break;
						case 4:
							// remover item do chão
							Server.world.remover_item(in.readInt(), in.readInt(), in.readInt());
							break;
						case 5:
							// abrindo uma bag que estava no chão
							enviar_itens_em_uma_bag(in.readInt());
							break;
						case -1:
							atualizar_player(playerid, 0, 0, 0, 0, 0, 0, 0);
							running = false;
							Server.user[playerid] = null;
							break;
					}
				}
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void enviar_outros_players() throws IOException {
		for (Users u : Server.user) {
			if (u != null && u.playerid != playerid) {
				u.player.enviar_para_novo_player(out);
			}
			
		}
		
	}

	private void acessar_daoitem_para_criar_ou_atualizar_item(int id, int tipo, int id_bag, int quant) throws IOException {
		
		// id, tipo, id da bag e quantidade
		//System.out.println("recebido na ordem: ");
		//System.out.println("item_id: "+id+" tipo: "+tipo+" bag: "+id_bag+" quant: "+quant);
		enviar_id_novo_item(id, Server.daoItem.criar_ou_atualizar_item(id, tipo, id_bag, quant));
		//System.out.println("id antigo enviado: "+id+" id novo enviado: "+r);
	}
	
	private void enviar_id_novo_item(int id_antigo, int id_novo) throws IOException {
		out.writeInt(2);
		out.writeInt(id_antigo);
		out.writeInt(id_novo);
		//System.out.println("id antigo enviado: "+id+" id novo enviado: "+r);
	}

	private void pegar_coisas_do_char(int id_char) {
		// ja foi enviado total de capturas, level, exp e admin_power_level
		
		try {
			// enviar bags
			ArrayList<int[]> bags = Server.daoBag.pegar_bags(id_char); // id da bag no bd; id do item no bd; tipo de item
			
			out.writeInt(bags.size()); //System.out.println("bags.size(): "+bags.size());
			for (int[] bag : bags) {
				for (int i : bag) {
					out.writeInt(i);
				}
			}
			for (int i = 0; i < bags.size(); i++) {
				enviar_itens_em_uma_bag(in.readInt());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void enviar_itens_em_uma_bag(int id_bag) throws IOException {
		ArrayList<int[]> itens = Server.daoItem.pegar_itens_da_bag(id_bag);
		out.writeInt(5);
		out.writeInt(itens.size());
		for (int[] item : itens) {
			for (int k : item) {
				out.writeInt(k);
			}
		}
	}

	private void atualizar_player(int playeridin, int xin, int yin, int zin, int lifeIn, int stateIn, int indexIn, int skinIn) throws IOException {
		
		Server.user[playeridin].player.atualizar(xin, yin, zin, lifeIn, stateIn, indexIn, skinIn);
		for (int i = 0; i < Server.user.length; i++) {
			if (Server.user[i] != null && i != playerid) {
				//System.out.println("x: "+xin+" y: "+yin);
				//System.out.println("playeridin: "+playeridin+" i: "+i);
				Server.user[i].out.writeInt(0);
				Server.user[i].out.writeInt(playeridin);
				Server.user[i].out.writeInt(xin);
				Server.user[i].out.writeInt(yin);
				Server.user[i].out.writeInt(zin);
				Server.user[i].out.writeInt(lifeIn);
				Server.user[i].out.writeInt(stateIn);
				Server.user[i].out.writeInt(indexIn);
				Server.user[i].out.writeInt(skinIn);
			}
		}
	}

	private void enviar_fala_para_todos(String fala, int x, int y) throws IOException {
		for (int i = 0; i < Server.user.length; i++) {
			if (Server.user[i] != null && i != playerid) { // mostrar que o player deslogou
				Server.user[i].out.writeInt(1);
				Server.user[i].out.writeUTF(fala);
				Server.user[i].out.writeInt(x);
				Server.user[i].out.writeInt(y);
			}
		}
	}

}
