package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Rank {
	public int ponto;
	public String nome;
	
	public static void novoRank(String nome, int pontos) {
		BufferedWriter write = null;
		ArrayList<Rank> ranks = pegarRanks();
		File file = new File("sla.txt");
		file = new File("sla.txt");
		file.delete();
		try {
			write = new BufferedWriter(new FileWriter("sla.txt"));
		}catch(IOException e) {}
		Rank current = new Rank();
		current.nome = nome;
		current.ponto = pontos;
		ranks.add(current);
		Collections.sort(ranks, nodeSorter);
		for(int i = 0; i < ranks.size(); i++) {
			current = ranks.get(i);
			String atual = current.nome;
			atual+=":";
			char[] value = Integer.toString(current.ponto).toCharArray();
			for(int n = 0; n < value.length; n++) {
				//value[n]+=encode; //criptografia do arquivo
				atual+=value[n];
			}
			try {
				write.write(atual);
				if(i < ranks.size() - 1) {
					write.newLine();
				}
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close(); //seguranças pra fechar o arquivo depois de salvar
		}catch(IOException e) {}
	}
	
	public static ArrayList<Rank> pegarRanks() {
		ArrayList<Rank> line = new ArrayList<Rank>();
		File file = new File("sla.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("sla.txt"));
				try {
					while((singleLine = reader.readLine()) != null) {
						String[] trans = singleLine.split(":");//ele lê até os :
						char[] val = trans[1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							//val[i] -= encode;
							trans[1] += val[i];
						}
						Rank current = new Rank();
						current.nome = trans[0];
						current.ponto= Integer.parseInt(trans[1]);
						line.add(current);
					}
				}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
		while(line.size() > 12) {
			line.remove(line.size()-1);
		}
		return line;
	}
	
	public static Comparator<Rank> nodeSorter = new Comparator<Rank>() {
		
		@Override
		public int compare(Rank n0,Rank n1) {
			if(n1.ponto < n0.ponto)
				return -1; //se n0 for maior, renderize primeiro
			if(n1.ponto > n0.ponto)
				return +1;
			return 0;
		}
		
	};
}
