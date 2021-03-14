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
	private static File arquivo;
	// carregar e salvar os "livros"
	private static final String local = "books", end_file = ".sz";
	
	public salvarCarregar() {
		arquivo = new File(local);
		if (!arquivo.exists()) {
			arquivo.mkdir();
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
		ArrayList<String> arquivos = listFilesForFolder(arquivo);
		try {
			BufferedReader reader;
			for (String caminho : arquivos) {
				reader = new BufferedReader(new FileReader(new File(caminho)));
				String singleLine = null;
				ArrayList<Tile> tiles = new ArrayList<Tile>();
				while((singleLine = reader.readLine()) != null && !singleLine.isBlank()) {
					System.out.println(singleLine);
					Tile tile = new Tile(0, 0);
					tile.carregar_sprites(singleLine);
					tiles.add(tile);
				}
				caminho = caminho.split("/")[caminho.split("/").length-1];
				caminho = caminho.substring(0, caminho.length()-end_file.length());
				System.out.println(caminho);
				Gerador.ui.adicionar_livro_salvo(caminho, tiles);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void salvar_livro(int index) {
		ArrayList<Tile> tiles = Ui.pegar_livro(index);
		String nome = Ui.pegar_nome_livro(index+1);
		File file = new File(arquivo, nome+end_file);
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

	public void salvar_tudo() {
		Ui.salvar();
	}
}
