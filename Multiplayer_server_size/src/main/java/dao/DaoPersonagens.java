package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.SinglePostgresqlConnection;

public class DaoPersonagens {
	// to the table of personagens
	private Connection connection;
	//*
	public static void main(String[] args) {
		DaoPersonagens dao = new DaoPersonagens();
		//dao.criar_personagem(6, "pedrojonassm");
	}
	//*/
	
	public DaoPersonagens() {
		connection = SinglePostgresqlConnection.getConnection();
		deslogar_geral();
	}
	
	public int[] pegar_coisas_para_o_cliente(int id) {
		// pegar o total_capturar, o level, o exp e o poder de admin
		int[] retorno = new int[4];
		
		String sql = "select total_capturas, level, exp, admin_power_level from personagens where id = "+id+";";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				for (int i = 0; i < retorno.length; i++) {
					retorno[i] = resultSet.getInt(i+1);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return retorno;
	}
	
	public int criar_personagem(int accountID, String nickname) {
		String sql = "INSERT INTO personagens (nickname, conta) VALUES ('"+nickname+"', "+accountID+");";
		try {
			connection.prepareStatement(sql).execute();
			connection.commit();
			return 1;
		} catch (SQLException e) {
			if (e.getMessage().contains("duplicate key")) {
				System.out.println("é, deu certo");
				return -1;
			}
			e.printStackTrace();
		}
		return 0;
	}
	
	public boolean deslogar_geral() {
		String sql = "update personagens set online = false;";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			if (statement.executeUpdate() == 1) {
				connection.commit();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean logar_deslogar_personagem(int accountID, String nick, boolean acao) {
		String sql = "UPDATE public.personagens SET online="+acao+" WHERE conta= "+accountID+" and nickname= '"+nick+"';";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			if (statement.executeUpdate() == 1) {
				connection.commit();
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public ArrayList<String> pegar_personagens_de_uma_conta(int accountID) {
		String sql = "select * from personagens where conta = "+accountID+";";
		ArrayList<String> retorno = new ArrayList<String>();
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				retorno.add(resultSet.getString("nickname"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public int pegar_id_personagem(String nickname) {
		String sql = "select id from personagens where nickname = '"+nickname+"';";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				return resultSet.getInt("id");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
}
