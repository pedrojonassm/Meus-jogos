package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SingleConnection {
	
	private static Connection connection = null;
	private static final String url = "jdbc:sqlite:secrets.db";
	
	static {
		conectar();
	}
	
	public SingleConnection() {
		conectar();
	}
	
	private static void conectar() {
		if (connection == null) {
			try {
				connection = DriverManager.getConnection(url);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
	}

	public static Connection getConnection() {
		return connection;
	}
	
	public static void clear_table() {
		// delete if exists any account saved
		String sql = "delete from usuario;";
		try {
			connection.prepareStatement(sql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void criar_tabela() {
		String sql = "CREATE TABLE if not exists usuario(login character varying(255),senha character varying(255));";
		try {
			connection.prepareStatement(sql).execute();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void salvar_email_senha(String login, String senha) {
		String sql = "insert into usuario(login, senha) values ('"+login+"', '"+senha+"')";
		criar_tabela();
		clear_table();
		try {
			connection.prepareStatement(sql).execute();
		} catch (SQLException e) {
			if (e.getMessage().contains("duplicate key")) {
				System.out.println("é, deu certo");
			}
			e.printStackTrace();
		}
	}
	
	public static String[] pegar_email_senha() {
		String sql = "select * from usuario;";
		criar_tabela();
		PreparedStatement statement;
		String[] retorno = null;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				retorno = new String[2];
				retorno[0] = resultSet.getString("login");
				retorno[1] = resultSet.getString("senha");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorno;
	}
}
