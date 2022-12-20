package controller;

import java.util.ArrayList;

import model.bo.UsuarioBO;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class UsuarioController {

	public UsuarioVO realizarLoginController(UsuarioVO usuarioVO) {
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.realizarLoginBO(usuarioVO);
	}

	public ArrayList<TipoUsuarioVO> consultarTipoUsuario() {
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.consultarTipoUsuarioBO();
	}

	public UsuarioVO cadastrarUsuarioController(UsuarioVO usuarioVO) {
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.cadastrarUsuarioBO(usuarioVO);
	}

	public boolean excluirUsuarioController(UsuarioVO usuarioVO) {
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.excluirUsuarioBO(usuarioVO);
	}

	public boolean atualizarUsuarioController(UsuarioVO usuarioVO) {
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.atualizarUsuarioBO(usuarioVO);

	}

	public static ArrayList<UsuarioVO> consultarTodosUsuariosController() {
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.consultarTodosUsuarioBO();
	}

	public UsuarioVO consultarUsuarioController(UsuarioVO usuarioVO) {
		UsuarioBO usuarioBO = new UsuarioBO();
		return usuarioBO.consultarUsuarioBO(usuarioVO);
	}

}
