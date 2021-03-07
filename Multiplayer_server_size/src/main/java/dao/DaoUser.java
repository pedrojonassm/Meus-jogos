package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Struct;

import database.SinglePostgresqlConnection;

public class DaoUser {
	// for the table usuario in database
	private Connection connection;
	
	public DaoUser() {
		connection = SinglePostgresqlConnection.getConnection();
		deslogar_geral();
	}
	
	// testar coisas
	public static void main(String[] args) {
		DaoUser dao = new DaoUser();
		System.out.println("fim do teste");
	}
	
	public void deslogar_geral() {
		String sql = "update usuario set logado = false;";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			if (statement.executeUpdate() == 1) {
				connection.commit();
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean validarLogin(String login, String senha) throws Exception{
		String sql = "select * from usuario where login = '"+login+"' and senha = '"+senha+"';";
		PreparedStatement statement = connection.prepareStatement(sql);
		ResultSet resultSet = statement.executeQuery();
		if (resultSet.next()) {// se tiver resultado, quer dizerque encontrou
			return true;
		}
		return false;
	}
	
	public boolean email_cadastrado(String email) {
		String sql = "select * from usuario where email = '"+email+"';";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			
			if (resultSet.next()) {// se tiver resultado, quer dizer que encontrou
				return true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean set_password_by_email(String email, String password) {
		String sql = "UPDATE usuario SET senha= '"+password+"' WHERE email='"+email+"';";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			statement.executeUpdate();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean criar_usuario(String email, String login, String password) {
		String sql = "INSERT INTO public.usuario(login, senha, email) VALUES ('"+login+"', '"+password+"', '"+email+"');";
		try {
			connection.prepareStatement(sql).execute();
			connection.commit();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public int logar(String email, String senha) {
		String sql = "SELECT * FROM usuario where login = '"+email+"' and senha = '"+senha+"';";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				if (resultSet.getBoolean("logado")) {
					return -1; // Usuário já está logado
				}
				int idBD = resultSet.getInt("id");
				logar_deslogar(idBD, true);
				return idBD; // retorna o id do usuário
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}
	
	public String get_login_by_email(String email) {
		String sql = "select * from usuario where email = '"+email+"';";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			
			if (resultSet.next()) {// se tiver resultado, quer dizer que encontrou
				return resultSet.getString("login");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public boolean set_password_by_login(String login, String senha) {
		String sql = "UPDATE usuario SET senha= '"+senha+"' WHERE login='"+login+"';";
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

	public void logar_deslogar(int idBD, boolean acao) {
		String sql = "UPDATE usuario SET logado = "+acao+" WHERE id='"+idBD+"';";
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			if (statement.executeUpdate() == 1) {
				connection.commit();
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
