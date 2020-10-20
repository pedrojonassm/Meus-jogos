package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import World.Mundo;
import entities.Player;

public class Menu {
	
	public String[] options = {"Novo Jogo", "Salvar jogo","Carregar jogo", "Sair"};
	public int opcao = 0, totalopcoes = options.length-1, codigo = 13;
	public static boolean up, down, selecionado, pause = false, saveGame = false, saveExists = false;
	
	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists = true;
		}else {
			saveExists = false;
		}
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
				if(!Game.cutscene) {
					Game.GameState = "normal";
				}
				else {
					Game.GameState = "entrada";
				}
				file = new File("save.txt");
				file.delete();
			}else if(opcao == 1) {
				String[] opt1 = {"vida", "municao", "level"};
				int[] opt2 = {(int) Game.player.life, Game.player.ammo, Game.level};
				saveGame(opt1,opt2,codigo);
			}else if(opcao == 2){
				file = new File("save.txt");
				if (file.exists()) {
					String saver = loadGame(codigo);
					applySave(saver);
				}
			}else {
				System.exit(1);
			}
		}
	}
	
	public static void applySave(String str) {
		String[] spl = str.split("/"); //pega a string str e separa em várias trings, usando o "/" para separalas ex: str = oi/olo spl[0] = oi spl[1] = olo
		for (int i = 0; i < spl.length;i++) {
			String[] spl2 = spl[i].split(":"); //pega o valor do que foi salvo (level/:8)
			switch(spl2[0]) {
			case "level":
				Mundo.restartGame("nivel"+spl2[1]+".png");
				Game.GameState = "normal";
				pause = false;
				break;
			case "vida":
				Game.player.life = Integer.parseInt(spl2[1]);
				break;
			case "municao":
				Game.player.life = Integer.parseInt(spl2[1]);
				break;
			}
		}
	}
	
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");//ele lê até os :
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i] -= encode; //decodificando
							trans[1] += val[i];
						}
						line+=trans[0];
						line+=":";
						line+=trans[1];
						line+="/";
					}
				}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
		return line;
	}
	
	public static void saveGame(String[] val1, int[] val2, int encode) {// vida, valor de vida, codificação
		BufferedWriter write = null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
		}catch(IOException e) {}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current+=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value[n]+=encode; //criptografia do arquivo
				current+=value[n];
			}
			try {
				write.write(current);
				if(i < val1.length - 1) {
					write.newLine();
				}
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close(); //seguranças pra fechar o arquivo depois de salvar
		}catch(IOException e) {}
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
