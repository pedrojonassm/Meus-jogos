package files;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import graficos.Ui;
import main.Gerador;
import world.Tile;

public class salvarCarregar {
	public static File arquivo_books, arquivo_worlds;
	// carregar e salvar os "livros"
	public static final String local_books = "books", local_worlds = "worlds", end_file_book = ".book", end_file_world = ".world";
	
	public salvarCarregar() {
		arquivo_books = new File(local_books);
		if (!arquivo_books.exists()) {
			arquivo_books.mkdir();
		}
		arquivo_worlds = new File(local_worlds);
		if (!arquivo_worlds.exists()) {
			arquivo_worlds.mkdir();
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

	public static void salvar_mundo(File file, String salvar) {
		try {
			if (!file.exists()) file.createNewFile();
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			writer.write(salvar);
			writer.flush();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
