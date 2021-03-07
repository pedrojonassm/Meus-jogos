package database;

import java.sql.Connection;
import java.sql.DriverManager;

/*
 * Responsável por fazer a conexão com o banco de dados
 * baixar o arquivo do database e colocar em web-inf/lib
 * para isso, só pesquisar [nome do banco] jbdc driver
 */

public class SinglePostgresqlConnection {
	private static String jbdc = "jdbc:postgresql://localhost:5432/", banco = "JavaPokemonDB",
			password = "floquinho", user = "javaPokemon-database-user";
	private static Connection connection = null;
	
	static { // sempre que chamar a classe é executado essa função
		conectar();
	}
	
	public SinglePostgresqlConnection() {
		conectar();
	}

	private static void conectar() {
		try {
			if (connection == null) {
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(jbdc+banco, user, password);
				connection.setAutoCommit(false);
			}
		}catch(Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao conectar com o banco de dados");
		}
	}
	
	public static Connection getConnection() {
		return connection;
	}

}
