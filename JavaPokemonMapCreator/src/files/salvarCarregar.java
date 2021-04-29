package files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import graficos.Ui;
import main.Gerador;
import world.Build;
import world.Tile;
import world.World;

public class salvarCarregar {
	public static File arquivo_books, arquivo_worlds, arquivo_construcoes;
	// carregar e salvar os "livros"
	public static final String local_books = "books", local_worlds = "worlds", local_builds = "construcoes", end_file_builds = ".bld", end_file_book = ".book", name_file_world = "world.world";
	
	public salvarCarregar() {
		arquivo_books = new File(local_books);
		if (!arquivo_books.exists()) {
			arquivo_books.mkdir();
		}
		arquivo_worlds = new File(local_worlds);
		if (!arquivo_worlds.exists()) {
			arquivo_worlds.mkdir();
		}
		arquivo_construcoes = new File (local_builds);
		if (!arquivo_construcoes.exists()) {
			arquivo_construcoes.mkdir();
		}
	}
	
	public ArrayList<String> listFilesForFolder(final File folder) {
		ArrayList<String> retorno = new ArrayList<String>();
	    for (final File fileEntry : folder.listFiles()) {
	        if (fileEntry.isDirectory()) {
	            for (String nome : listFilesForFolder(fileEntry)) {
	            	retorno.add(folder.getName()+"/"+nome);
	            }
	        } else {
	            retorno.add(folder.getName()+"/"+fileEntry.getName());
	        }
	    }
	    return retorno;
	}
	
	public static void salvar_construcao(Tile pontoA, Tile pontoB) {
		try {
			String nome = null;
			File file = null;
			do {
				nome = JOptionPane.showInputDialog("Insira um nome NOVO para a nova construção");
				if (nome == null) return;
				if (!nome.isBlank()) {
					file = new File(local_builds, nome+end_file_builds);
					if (file.exists()) {
						file = null;
					}else {
							file.createNewFile();
					}
				}
			} while (nome == null || file == null);
			ArrayList<Tile> contrucao = World.pegar_construção(pontoA, pontoB);
			int horizontal = (pontoA.getX() >> World.log_ts) - (pontoB.getX() >> World.log_ts), vertical = (pontoA.getY() >> World.log_ts) - (pontoB.getY() >> World.log_ts), high = pontoA.getZ() - pontoB.getZ();
			//*
			System.out.println("horizontal: "+(pontoA.getX() >> World.log_ts) +" "+ (pontoB.getX() >> World.log_ts)); 
			System.out.println("vertical: "+(pontoA.getY() >> World.log_ts) +" "+ (pontoB.getY() >> World.log_ts));
			System.out.println("high"+pontoA.getZ() +" "+ pontoB.getZ());//*/
			if (horizontal < 0) horizontal *= -1; if (vertical < 0) vertical *= -1; if (high < 0) high *= -1; // salvar o tamanho da construção
			
			// 9 - 7 = 2, entretanto são as posições 7, 8 e 9, logo o correto seria 3. Logo, se o resoltado for maior que 0, o resultado sempre deve ser somado +1
			horizontal++; vertical++; high++;
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(horizontal+":"+vertical+":"+high+"\n");
			for (Tile t : contrucao) {
				writer.write(t.salvar());
			}
			writer.flush();
			writer.close();
			Gerador.ui.adicionar_construcao(new Build(horizontal, vertical, high, file));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void carregar_construcoes() {
		ArrayList<String> arquivos = listFilesForFolder(arquivo_construcoes);
		try {
			BufferedReader reader;
			ArrayList<Build> construcoes = new ArrayList<Build>();
			for (String caminho : arquivos) {
				File f = new File(caminho);
				reader = new BufferedReader(new FileReader(f));
				String[] size = reader.readLine().split(":");
				construcoes.add(new Build(Integer.parseInt(size[0]), Integer.parseInt(size[1]), Integer.parseInt(size[2]), f));
			}
			Gerador.ui.adicionar_construcoes_salvas(construcoes);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void carregar_livros() {
		ArrayList<String> arquivos = listFilesForFolder(arquivo_books);
		try {
			BufferedReader reader;
			for (String caminho : arquivos) {
				reader = new BufferedReader(new FileReader(new File(caminho)));
				String singleLine = null;
				ArrayList<Tile> tiles = new ArrayList<Tile>();
				while((singleLine = reader.readLine()) != null && !singleLine.isBlank()) {
					Tile tile = new Tile(0, 0, 0);
					tile.carregar_sprites(singleLine);
					tiles.add(tile);
				}
				caminho = caminho.split("/")[caminho.split("/").length-1];
				caminho = caminho.substring(0, caminho.length()-end_file_book.length());
				Gerador.ui.adicionar_livro_salvo(caminho, tiles);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void salvar_livro(int index) {
		ArrayList<Tile> tiles = Ui.pegar_livro(index);
		String nome = Ui.pegar_nome_livro(index+1);
		File file = new File(arquivo_books, nome+end_file_book);
		try {
			if (!file.exists()) file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			
			for (Tile t : tiles) {
				writer.write(t.salvar());
			}
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void salvar_mundo(File pasta_do_mundo, String salvar) {
		try {
			File world = new File(pasta_do_mundo, name_file_world);
			if (!world.exists()) world.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(world));
			writer.write(salvar);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Tile> carregar_construcao(Build construcao) {
		if (construcao == null || construcao.getFile() == null) return null;
		ArrayList<Tile> retorno = new ArrayList<Tile>();
		try {
			@SuppressWarnings("resource")
			BufferedReader reader = new BufferedReader(new FileReader(construcao.getFile()));
			reader.readLine(); // pula a linha das dimensões
			String singleLine;
			while((singleLine = reader.readLine()) != null && !singleLine.isBlank()) {
				Tile tile = new Tile(0, 0, 0);
				tile.carregar_sprites(singleLine);
				retorno.add(tile);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}
}
