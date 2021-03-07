package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import database.SinglePostgresqlConnection;

public class DaoItem {
	private Connection connection;
	
	public DaoItem() {
		connection = SinglePostgresqlConnection.getConnection();
	}
	
	public static void main(String[] args) {
		DaoItem daoItem = new DaoItem();
	}
	
	public ArrayList<int[]> pegar_itens_da_bag(int id_bag) {
		ArrayList<int[]> retorno = new ArrayList<int[]>();
		String sql = "select i.id, i.tipo, i.quantidade from item i where i.bag = "+id_bag+";";
		int total_colunas = 3;
		/*
		 * id do item no bd
		 * tipo do item
		 * quantidade do item
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

	public int criar_ou_atualizar_item(int id, int tipo, int bag, int quant) {
		String sql = "select * from item where id = "+id+";";//
		PreparedStatement statement;
		try {
			statement = connection.prepareStatement(sql);
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				if (quant > 0) {
					sql = "update item set tipo = "+tipo+", bag = "+bag+", quantidade = "+quant+" where id = "+id+";";
					connection.prepareStatement(sql).execute();
					connection.commit();
					return id;
				}else {
					sql = "delete from item where id = "+id+";";
					connection.prepareStatement(sql).execute();
					connection.commit();
					return id;
				}
			}else if (quant > 0){
				sql = "INSERT INTO public.item(tipo, bag, quantidade) VALUES ("+tipo+", "+bag+", "+quant+");";
				connection.prepareStatement(sql).execute();
				connection.commit();
				sql = "select currval('id_item_auto_increment');";
				statement = connection.prepareStatement(sql);
				resultSet = statement.executeQuery();
				resultSet.next();
				return resultSet.getInt(1); // retorna o novo id
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -2;
	}
	
}
