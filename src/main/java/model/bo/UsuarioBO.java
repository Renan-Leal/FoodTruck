package model.bo;

import java.util.ArrayList;

import model.dao.UsuarioDAO;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class UsuarioBO {

	public UsuarioVO realizarLoginBO(UsuarioVO usuarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.realizarLoginDAO(usuarioVO);
	}

	public ArrayList<TipoUsuarioVO> consultarTipoUsuarioBO() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		return usuarioDAO.consultarTipoUsuarioDAO();
	}

	public UsuarioVO cadastrarUsuarioBO(UsuarioVO usuarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if (usuarioDAO.verificarExistenciaRegistroPorCpfDAO(usuarioVO)) {
			System.out.println("\nUsuário já cadastrado!");
		} else
			usuarioVO = usuarioDAO.cadastrarUsuarioDAO(usuarioVO);
		return usuarioVO;
	}

	public boolean excluirUsuarioBO(UsuarioVO usuarioVO) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if (usuarioDAO.verificarExistenciaRegistroPorIdUsuarioDAO(usuarioVO.getIdUsuario())) {
			if (usuarioDAO.verificarDesligamentoUsuarioPorIdUsuarioDAO(usuarioVO.getIdUsuario())) {
				System.out.println("\nUsuário já se encontra desligado da base de dados! ");
			} else {
				resultado = usuarioDAO.excluirUsuarioDAO(usuarioVO);
			}
		} else {
			System.out.println("\nUsuário não existe na base de dados! ");
		}
		return resultado;
	}

	public boolean atualizarUsuarioBO(UsuarioVO usuarioVO) {
		boolean resultado = false;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if (usuarioDAO.verificarExistenciaRegistroPorIdUsuarioDAO(usuarioVO.getIdUsuario())) {
			if (usuarioDAO.verificarDesligamentoUsuarioPorIdUsuarioDAO(usuarioVO.getIdUsuario())) {
				System.out.println("\nUsuário já se encontra desligado da base de dados! ");
			} else {
				resultado = usuarioDAO.atualizarUsuarioDAO(usuarioVO);
			}
		} else {
			System.out.println("\nUsuário não existe na base de dados! ");
		}
		return resultado;
	}

	public ArrayList<UsuarioVO> consultarTodosUsuarioBO() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		ArrayList<UsuarioVO> listaUsuarioVO = usuarioDAO.consultarTodosUsuariosDAO();
		if (listaUsuarioVO.isEmpty()) {
			System.out.println("\nLista de usuários está vazia!");
		}
		return listaUsuarioVO;
	}

	public UsuarioVO consultarUsuarioBO(UsuarioVO usuarioVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		UsuarioVO usuario = usuarioDAO.consultarUsuarioDAO(usuarioVO);
		if (usuario == null) {
			System.out.println("\nUsuário não localizado");
		}
		return usuario;
	}

}
