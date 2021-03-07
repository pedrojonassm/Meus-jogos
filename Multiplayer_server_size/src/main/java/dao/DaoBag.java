package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.SinglePostgresqlConnection;

public class DaoBag {
	private Connection connection;
	
	public DaoBag() {
		connection = SinglePostgresqlConnection.getConnection();
	}
	
	public static void main(String[] args) {
		//DaoBag daoBag = new DaoBag();
		//daoBag.criar_bag(6, 4, 3);
	}
	
	public void criar_bag(int tipo, int bag_que_esta_dentro, int dono_da_bag) {
		String sql = "call criar_bag("+tipo+", "+bag_que_esta_dentro+", "+dono_da_bag+");";
		try {
			connection.prepareStatement(sql).execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public ArrayList<int[]> pegar_bags(int id_char) {
		ArrayList<int[]> retorno = new ArrayList<int[]>();
		String sql = "select b.id, b.item_id, i.tipo from bag b, item i where i.id = b.item_id and b.personagem = "+id_char+" and b.id != 0";
		int total_colunas = 3;
		/*
		 * id da bag no bd
		 * id do item no bd
		 * tipo de item
		 */
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				int[] linha = new int[total_colunas];
				for (int i = 0; i < total_colunas; i++) {
					linha[i] = resultSet.getInt(i+1);
				}
				retorno.add(linha);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return retorno;
	}

	public void setar_personagem(int id_bag, int personagem) {
		
		String sql;
		if (personagem == -1) {
			sql = "update bag set personagem = NULL where id = "+id_bag+";";
		}else {
			sql = "update bag set personagem = "+personagem+" where id = "+id_bag+";";
		}
		try {
			connection.prepareStatement(sql).execute();
			connection.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
}
