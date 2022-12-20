package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class UsuarioDAO {

	public UsuarioVO realizarLoginDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;

		String query = "SELECT "
				+ "u.idusuario"
				+ ", tipo.descricao"
				+ ", u.nome"
				+ ", u.cpf"
				+ ", u.email"
				+ ", u.telefone"
				+ ", u.datacadastro"
				+ ", u.dataexpiracao " 
				+ "FROM USUARIO u, TIPOUSUARIO tipo "
				+ "WHERE u.login like '" + usuarioVO.getLogin() + "' " + "AND u.senha like '" + usuarioVO.getSenha()
				+ "' " + "AND u.idTipoUsuario = tipo.idTipoUsuario";

		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				usuarioVO.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuarioVO.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuarioVO.setNome(resultado.getString(3));
				usuarioVO.setCpf(resultado.getString(4));
				usuarioVO.setEmail(resultado.getString(5));
				usuarioVO.setTelefone(resultado.getString(6));
				usuarioVO.setDataCadastro(LocalDateTime.parse(resultado.getString(7),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				if (resultado.getString(8) != null) {
					usuarioVO.setDataExpiracao(LocalDateTime.parse(resultado.getString(8),
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

				}
			}
		} catch (SQLException erro) {
			System.out.println("ERRO ao executar a query do método realizarLoginDAO");
			System.out.println("ERRO: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);

		}
		return usuarioVO;
	}

	public ArrayList<TipoUsuarioVO> consultarTipoUsuarioDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<TipoUsuarioVO> listaTipoUsuarioVO = new ArrayList<TipoUsuarioVO>();
		String query = "SELECT descricao FROM tipousuario";
		try {
			resultado = stmt.executeQuery(query);
			while (resultado.next()) {
				TipoUsuarioVO tipoUsuarioVO = TipoUsuarioVO.valueOf(resultado.getString(1));
				listaTipoUsuarioVO.add(tipoUsuarioVO);
			}

		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarTipoUsuarioDAO.");
			System.out.println("Erro: " + erro.getMessage());

		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}

		return listaTipoUsuarioVO;
	}

	public boolean verificarExistenciaRegistroPorCpfDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT cpf FROM usuario WHERE cpf = '" + usuarioVO.getCpf() + "' ";
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarExistenciaRegistroPorCpfDAO.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public UsuarioVO cadastrarUsuarioDAO(UsuarioVO usuarioVO) {
		String query = "INSERT INTO usuario(idtipousuario, nome, cpf, email, telefone, datacadastro,"
				+ "login, senha) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
		Connection conn = Banco.getConnection();
		PreparedStatement pstmt = Banco.getPreparedStatementWithPk(conn, query);
		try {
			pstmt.setInt(1, usuarioVO.getTipoUsuario().getValor());
			pstmt.setString(2, usuarioVO.getNome());
			pstmt.setString(3, usuarioVO.getCpf());
			pstmt.setString(4, usuarioVO.getEmail());
			pstmt.setString(5, usuarioVO.getTelefone());
			pstmt.setObject(6, usuarioVO.getDataCadastro());
			pstmt.setString(7, usuarioVO.getLogin());
			pstmt.setString(8, usuarioVO.getSenha());
			pstmt.execute();
			ResultSet resultado = pstmt.getGeneratedKeys();
			if (resultado.next()) {
				usuarioVO.setIdUsuario(Integer.parseInt(resultado.getString(1)));

			}

		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarExistenciaRegistroPorCpfDAO.");
			System.out.println("Erro: " + erro.getMessage());

		} finally {
			Banco.closeStatement(pstmt);
			Banco.closeConnection(conn);
		}
		return usuarioVO;
	}

	public boolean excluirUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;

		String query = "UPDATE usuario SET dataexpiracao = '" + usuarioVO.getDataExpiracao() + "' "
				+ "WHERE idusuario = " + usuarioVO.getIdUsuario();

		try {
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método excluirUsuarioDAO.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean verificarDesligamentoUsuarioPorIdUsuarioDAO(int idUsuario) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT dataExpiracao FROM usuario WHERE idusuario = " + idUsuario;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				String dataExpiracao = resultado.getString(1);
				if (dataExpiracao != null) {
					retorno = true;
				}
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarExistenciaRegistroPorIdUsuarioDAO.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean verificarExistenciaRegistroPorIdUsuarioDAO(int idUsuario) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		boolean retorno = false;
		String query = "SELECT idusuario FROM usuario WHERE idusuario = " + idUsuario;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método verificarExistenciaRegistroPorIdUsuarioDAO.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public boolean atualizarUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;

		String query = "UPDATE usuario SET "
				+ "idTipoUsuario = '" + usuarioVO.getTipoUsuario().getValor()
				+ "', nome = '" + usuarioVO.getNome() 
				+ "', cpf = '" + usuarioVO.getCpf() 
				+ "', email = '" + usuarioVO.getEmail()
				+ "', telefone = '" + usuarioVO.getTelefone() + "', datacadastro = '" + usuarioVO.getDataCadastro()
				+ "', login = '" + usuarioVO.getLogin() 
				+ "', senha = '" + usuarioVO.getSenha() 
				+ "' WHERE idusuario = " + usuarioVO.getIdUsuario();
		try {
			if (stmt.executeUpdate(query) == 1) {
				retorno = true;
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método atualizarUsuarioDAO.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;

	}

	public ArrayList<UsuarioVO> consultarTodosUsuariosDAO() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<UsuarioVO> listaUsuarioVO = new ArrayList<UsuarioVO>();

		String query = "SELECT "
				+ "u.idusuario"
				+ ", tipo.descricao"
				+ ", u.nome, u.cpf"
				+ ", u.email"
				+ ", u.telefone"
				+ ", u.datacadastro"
				+ ", u.dataexpiracao"
				+ ", u.login"
				+ ", u.senha "
				+ "FROM usuario as u, tipousuario as tipo " 
				+ "WHERE u.idtipousuario = tipo.idtipousuario";

		try {
			resultado = stmt.executeQuery(query);
			while (resultado.next()) {
				UsuarioVO usuario = new UsuarioVO();
				usuario.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuario.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuario.setNome(resultado.getString(3));
				usuario.setCpf(resultado.getString(4));
				usuario.setEmail(resultado.getString(5));
				usuario.setTelefone(resultado.getString(6));
				usuario.setDataCadastro(LocalDateTime.parse(resultado.getString(7),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				if (resultado.getString(8) != null) {
					usuario.setDataExpiracao(LocalDateTime.parse(resultado.getString(8),
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				}
				usuario.setLogin(resultado.getString(9));
				usuario.setSenha(resultado.getString(10));
				listaUsuarioVO.add(usuario);
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarTodosUsuariosDAO.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return listaUsuarioVO;
	}

	public UsuarioVO consultarUsuarioDAO(UsuarioVO usuarioVO) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		UsuarioVO usuario = new UsuarioVO();

		String query = "SELECT "
				+ "u.idusuario"
				+ ", tipo.descricao"
				+ ", u.nome"
				+ ", u.cpf"
				+ ", u.email"
				+ ", u.telefone"
				+ ", u.datacadastro"
				+ ", u.dataexpiracao"
				+ ", u.login"
				+ ", u.senha "
				+ "FROM usuario as u, tipousuario as tipo " 
				+ "WHERE u.idtipousuario = tipo.idtipousuario " + "AND u.idusuario = " + usuarioVO.getIdUsuario();

		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				usuario.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuario.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuario.setNome(resultado.getString(3));
				usuario.setCpf(resultado.getString(4));
				usuario.setEmail(resultado.getString(5));
				usuario.setTelefone(resultado.getString(6));
				usuario.setDataCadastro(LocalDateTime.parse(resultado.getString(7),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				if (resultado.getString(8) != null) {
					usuario.setDataExpiracao(LocalDateTime.parse(resultado.getString(8),
							DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				}
				usuario.setLogin(resultado.getString(9));
				usuario.setSenha(resultado.getString(10));
			}
		} catch (SQLException erro) {
			System.out.println("Erro ao executar a query do método consultarUsuariosDAO.");
			System.out.println("Erro: " + erro.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return usuario;
	}

	public boolean verificarExistenciaPorUsuarioDAO(int idUsuario) {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		boolean retorno = false;
		ResultSet resultado = null;
		String query = "SELECT idUsuario FROM usuario WHERE idUsuario = " + idUsuario;
		try {
			resultado = stmt.executeQuery(query);
			if (resultado.next()) {
				retorno = true;
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método verificarExistenciaPorUsuarioDAO!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return retorno;
	}

	public ArrayList<UsuarioVO> consultarListaEntregadores() {
		Connection conn = Banco.getConnection();
		Statement stmt = Banco.getStatement(conn);
		ResultSet resultado = null;
		ArrayList<UsuarioVO> listaUsuariosVO = new ArrayList<UsuarioVO>();
		String query = "SELECT "
				+ "u.idUsuario"
				+ ", tipo.descricao"
				+ ", u.nome"
				+ ", u.cpf"
				+ ", u.email"
				+ ", u.telefone"
				+ ", u.dataCadastro"
				+ ", u.login"
				+ ", u.senha "
				+ "FROM usuario u, tipoUsuario tipo " 
				+ "WHERE u.idTipoUsuario = tipo.idTipoUsuario " + "AND u.dataExpiracao is NULL "
				// Usa 'toString()' pq queremos apenas as descrições, não o id do entregador que
				// viria junto com ele
				+ "AND tipo.descricao like '" + TipoUsuarioVO.ENTREGADOR.toString() + "'";
		try {
			resultado = stmt.executeQuery(query);
			while (resultado.next()) {
				UsuarioVO usuarioVO = new UsuarioVO();
				usuarioVO.setIdUsuario(Integer.parseInt(resultado.getString(1)));
				usuarioVO.setTipoUsuario(TipoUsuarioVO.valueOf(resultado.getString(2)));
				usuarioVO.setNome(resultado.getString(3));
				usuarioVO.setCpf(resultado.getString(4));
				usuarioVO.setEmail(resultado.getString(5));
				usuarioVO.setTelefone(resultado.getString(6));
				usuarioVO.setDataCadastro(LocalDateTime.parse(resultado.getString(7),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
				usuarioVO.setLogin(resultado.getString(8));
				usuarioVO.setSenha(resultado.getString(9));
				listaUsuariosVO.add(usuarioVO);
			}
		} catch (SQLException e) {
			System.out.println("Erro ao executar a query do método consultarListaEntregadores!");
			System.out.println("Erro: " + e.getMessage());
		} finally {
			Banco.closeResultSet(resultado);
			Banco.closeStatement(stmt);
			Banco.closeConnection(conn);
		}
		return listaUsuariosVO;
	}

}
