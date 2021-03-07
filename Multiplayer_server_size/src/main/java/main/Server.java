package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import javax.swing.JOptionPane;

import dao.DaoBag;
import dao.DaoItem;
import world.World;

public class Server {
	private static Server server;
	// socket
	// Link: https://www.youtube.com/watch?v=_1ThWf9Fkfo
	static ServerSocket serverSocket;
	public Socket socket;
	public DataOutputStream out;
	public DataInputStream in;
	public static int port;
	public static String ip = "localhost";
	public static ArrayList<Users> usuarios;
	public static Users[] user;
	public static int jogadores_conectados = 0;
	private static final String txt_port = "porta", txt_players = "total de jogadores", splitter = ": ";
	public static World world;
	public static DaoBag daoBag;
	public static DaoItem daoItem;
	
	public static void main(String[] args){
		server = new Server();
		File file = new File("settings.txt");
		try {
			if (!file.exists()) {
				file.createNewFile();
				BufferedWriter write = null;
				write = new BufferedWriter(new FileWriter(file));
				write.write(txt_port + splitter + "7777");
				write.newLine();
				write.write(txt_players + splitter + "10");
				write.newLine();
				write.close();
				JOptionPane.showMessageDialog(null, "Foi criado um arquivo \"settings.txt\" com as configurações padrão de porta e número de jogadores\n Caso necessário, edite-o");
			}else {
				BufferedReader read = new BufferedReader(new FileReader(file));
				String str_port = read.readLine().split(splitter)[1], str_max_players = read.readLine().split(splitter)[1];
				if (!tem_letras(str_port) && !tem_letras(str_max_players)) {
					port = Integer.parseInt(str_port);
					user = new Users[Integer.parseInt(str_max_players)];
				}
				read.close();
				daoBag = new DaoBag();
				daoItem = new DaoItem();
				world = new World();
				//JOptionPane.showMessageDialog(null, "server iniciado!\n"+txt_port+": "+str_port+"\n"+txt_players+": "+str_max_players);
				usuarios = new ArrayList<Users>();
				server.hostear();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	private static boolean tem_letras(String text) {
		char[] numeros = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
		boolean tem;
		for (char c : text.toCharArray()) {
			tem = false;
			for (char n : numeros) {
				if (n == c) {
					// verifica se o caractere é um número
					tem = true;
					break;
				}
			}
			if (!tem) {
				// se não for, a função retorna verdadeiro, já que tem letras
				return true;
			}
		}
		// caso contrário, ela retorna falso, pois tem apenas números
		return false;
	}
	
	public static int tem_espaco() {
		for (int i = 0; i < user.length; i++) {
			if (user[i] == null) {
				return i;
			}
		}
		return -1;
	}
	
	private void hostear() throws Exception  {
		serverSocket = new ServerSocket(port);
		
		while(true) {
			socket = serverSocket.accept();
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			//System.out.println("Conexão feita com: "+socket.getInetAddress());
			Users novo = new Users(out, in);
			usuarios.add(novo);
			Thread thread = new Thread(novo);
			novo.setThread(thread);
			out.writeInt(user.length); // envia o máximo de players
			out.writeInt(jogadores_conectados);
			thread.start();
			
		}
	}
	
	public static void entrar_servidor(Users entrar, int pos) {
			jogadores_conectados++;
			//System.out.println("novo id: "+pos);
			entrar.setPlayerid(pos);
			user[pos] = entrar;
			usuarios.remove(entrar);
	}
}
