package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.vo.ItemVendaVO;
import model.vo.VendaVO;

public class VendaDAO {

	public VendaVO cadastrarVendaDAO(VendaVO vendaVO) {
		String query = "INSERT INTO venda (idUsuario, numeroPedido, dataVenda, flagEntrega";
		// no termo "isFlagEntrega" o IS faz o papel do costumeiro GET pois o tipo do
		// parametro de FlagEntrega é um booleano
		if (vendaVO.isFlagEntrega()) {
			// esses pontos de interrogações são as mascaras
			query += ", taxaEntrega) VALUES (?, ?, ?, ?, ?)";
		} else {
			query += ") VALUES (?, ?, ?, ?)";
		}
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);
		try {
			// para preencher as máscaras: no primeiro ponto de interrogação passa o
			// idUsuario, na segunda o numeroPedido e assim por diante
			pstmt.setInt(1, vendaVO.getIdUsuario());
			pstmt.setInt(2, vendaVO.getNumeroPedido());
			pstmt.setObject(3, vendaVO.getDataVenda());
			if (vendaVO.isFlagEntrega()) {
				pstmt.setInt(4, 1);
				pstmt.setDouble(5, vendaVO.getTaxaEntrega());
			} else {
				pstmt.setInt(4, 0);
			}
			pstmt.execute();
			ResultSet resultado = pstmt.getGeneratedKeys();
			if (resultado.next()) {
				vendaVO.setIdVenda(resultado.getInt(1));
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método cadastrarVendaDAO!");
			System.out.println("Erro: " + e.getMessage());
		}finally {
			Banco.closeStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return vendaVO;
	}

	public boolean cadastrarItemVendaDAO(VendaVO vendaVO) {
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = null;
		// para posteriormente fazer relatórios
		int contador = 0;
		boolean retorno = false;
		try {
			for (ItemVendaVO item : vendaVO.getListaItemVendaVO()) {
				String query = "INSERT INTO itemVenda (idVenda, idProduto, quantidade) VALUES (?, ?, ?)";
				pstmt = Banco.getPreparedStatementWithPk(conn, query);
				pstmt.setInt(1, vendaVO.getIdVenda());
				pstmt.setInt(2, item.getIdProduto());
				pstmt.setInt(3, item.getQuantidade());
				pstmt.execute();
				ResultSet resultado = pstmt.getGeneratedKeys();
				if (resultado.next()) {
					item.setIdItemVenda(resultado.getInt(1));
					contador++;
				}
			}
			// se o contador for igual ao numero de itens de venda então tudo foi cadastrado
			if (contador == vendaVO.getListaItemVendaVO().size()) {
				retorno = true;
			} else {
				System.out.println("Nem todos os produtos foram cadastrados.");
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método cadastrarItemVendaDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean consultarVendaDAO(VendaVO vendaVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT idVenda FROM venda WHERE idVenda = " + vendaVO.getIdVenda();
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarVendaDAO.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean cancelarVendaDAO(VendaVO vendaVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "UPDATE venda SET datacancelamento = '" + vendaVO.getDataCancelamento() + "' WHERE idvenda = "
				+ vendaVO.getIdVenda();
		try {
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método cancelarVendaDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean verificarExistenciaRegistroPorIdVendaDAO(int idVenda) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT idvenda FROM venda WHERE idvenda = " + idVenda;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método verificarExistenciaRegistroPorIdVendaDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean verificarVendaPossuiEntrega(int idVenda) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT flagEntrega FROM venda WHERE idvenda = " + idVenda;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				if (resultado.getString(1).equals("1")) {
					retorno = true;
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método verificarVendaPossuiEntrega!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean verificarCancelamentoPorIdVendaDAO(int idVenda) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT dataCancelamento FROM venda WHERE idvenda = " + idVenda;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				if (resultado.getString(1) != null) {
					retorno = true;
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método verificarCancelamentoPorIdVendaDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean verificarDataCancelamentoVendaDAO(VendaVO vendaVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT dataVenda FROM venda WHERE idVenda = " + vendaVO.getIdVenda();
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				LocalDateTime dataVenda = LocalDateTime.parse(resultado.getString(1),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
				if(vendaVO.getDataCancelamento().isAfter(dataVenda)) {
					retorno = true;
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método verificarDataCancelamentoVendaDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

}
