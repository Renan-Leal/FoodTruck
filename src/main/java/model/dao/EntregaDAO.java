package model.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import model.vo.EntregaVO;
import model.vo.SituacaoEntregaVO;
import model.vo.VendaVO;

public class EntregaDAO {

	public boolean cadastrarEntregaDAO(EntregaVO entregaVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "INSERT INTO entrega (idVenda, idEntregador, SituacaoEntrega) VALUES ("
				+ entregaVO.getIdVenda() + ", " + entregaVO.getIdEntregador() + ", "
				+ entregaVO.getSituacaoEntrega().getValor() + ")";
		try {
			int resultado = stmt.executeUpdate(query);
			if (resultado == 1) {
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método cadastrarEntregaDAO");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean atualizarSituacaoEntregaDAO(VendaVO vendaVO) {
		EntregaVO entregaVO = this.consultarEntregaPorIdVendaDAO(vendaVO.getIdVenda());
		entregaVO.setSituacaoEntrega(
				SituacaoEntregaVO.getSituacaoEntregaVOPorValor(entregaVO.getSituacaoEntrega().getValor() + 1));
		// Se a situação de entrega atualizada for igual a 4 que é o "tamanho" das
		// possibilidades de situações de entrega então a entrega foi efetuada
		if (entregaVO.getSituacaoEntrega().getValor() == SituacaoEntregaVO.PEDIDO_ENTREGUE.getValor()) {
			entregaVO.setDataEntrega(LocalDateTime.now());
		}
		if (entregaVO.getSituacaoEntrega().getValor() > SituacaoEntregaVO.PEDIDO_ENTREGUE.getValor()) {
			return false;
		} else {
			return this.atualizarEntregaDAO(entregaVO);

		}

	}

	public EntregaVO consultarEntregaPorIdVendaDAO(int idVenda) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		EntregaVO entregaVO = new EntregaVO();
		// Tabela de entrega = 'e'
		// Tabela de situacao de entrega = 'se'
		String query = "SELECT " + "e.idEntrega" + ", e.idVenda" + ", e.idEntregador" + ", se.descricao"
				+ ", e.dataEntrega " + "FROM entrega e, situacaoEntrega se "
				+ "WHERE e.situacaoentrega = se.ordem " + "AND e.idVenda = " + idVenda;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				entregaVO.setIdEntrega(Integer.parseInt(resultado.getString(1)));
				entregaVO.setIdVenda(Integer.parseInt(resultado.getString(2)));
				entregaVO.setIdEntregador(Integer.parseInt(resultado.getString(3)));
				entregaVO.setSituacaoEntrega(SituacaoEntregaVO.valueOf(resultado.getString(4)));
				if (resultado.getString(5) != null) {
					entregaVO.setDataEntrega(LocalDateTime.parse(resultado.getString(5),
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				}
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método consultarEntregaPorIdVendaDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return entregaVO;
	}

	private boolean atualizarEntregaDAO(EntregaVO entregaVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "UPDATE entrega SET SituacaoEntrega = " + entregaVO.getSituacaoEntrega().getValor();
		if (entregaVO.getDataEntrega() != null) {
			query += ", dataEntrega = '" + entregaVO.getDataEntrega() + "'";
		}
		query += " WHERE idEntrega = " + entregaVO.getIdEntrega();
		try {

			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método atualizarEntregaDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean cancelarEntregaDAO(VendaVO vendaVO, int situacao) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		String query = "UPDATE entrega SET situacaoentrega = '" + situacao + "'," + " dataentrega = '" + vendaVO.getDataCancelamento()
					+ "' WHERE idvenda = " + vendaVO.getIdVenda(); 
		try {
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método cancelarEntregaDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}
}
