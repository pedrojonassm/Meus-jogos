package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import database.SinglePostgresqlConnection;

public class DaoToken {
	// for the table register_token in database
	private Connection connection;

	public DaoToken() {
		connection = SinglePostgresqlConnection.getConnection();
		apagar_tokens_vencidos();
	}
	/*
	 public static void main(String[] args) {
		daoToken dao = new daoToken();
	}//*/
	public void apagar_tokens_vencidos() {
		String sql = "DELETE FROM register_token WHERE delete_date < now();";
		try {
			connection.prepareStatement(sql).execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public int validaToken(String token) {
		String sql = "select * from register_token where token = '"+token+"';";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {// se tiver resultado, quer dizer que encontrou, visto que Token é Unique
				if (resultSet.getBoolean("redefinir_senha")) {
					return 2;
				}
				return 1;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public void deletar_token(String token) {
		String sql = "DELETE FROM register_token WHERE token='"+token+"';";
		try {
			connection.prepareStatement(sql).execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean insert_token(String token, boolean redefinir) {
		String sql = "INSERT INTO register_token( token, redefinir_senha) VALUES ('"+token+"', "+redefinir+");";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			statement.execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
}
