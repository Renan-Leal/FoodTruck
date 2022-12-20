package view;

import java.util.Scanner;

import controller.UsuarioController;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;

public class Login {

	private static final int OPCAO_MENU_LOGIN = 1;
	private static final int OPCAO_MENU_CRIAR_CONTA = 2;
	private static final int OPCAO_MENU_SAIR = 3;

	Scanner input = new Scanner(System.in);

	public void apresentarMenuLogin() {
		int opcao = this.apresentarOpcoesMenu();
		while (opcao != OPCAO_MENU_SAIR) {
			switch (opcao) {
			case OPCAO_MENU_LOGIN: {
				UsuarioVO usuarioVO = this.realizarLogin();
				if (usuarioVO.getIdUsuario() != 0 && usuarioVO.getDataExpiracao() == null) {
					System.out.println("\nUsuário Logado: " + usuarioVO.getLogin());
					System.out.println("Perfil: " + usuarioVO.getTipoUsuario());
					Menu menu = new Menu();
					menu.apresentarMenu(usuarioVO);
				}
				break;
			}
			case OPCAO_MENU_CRIAR_CONTA: {
				this.cadastrarNovoUsuario();
				break;
			}
			case OPCAO_MENU_SAIR: {
				System.out.println("SAINDO. . .");
				break;
			}
			default: {
				System.out.print("\nOpção inválida!!!");
				break;
			}
			}
			opcao = this.apresentarOpcoesMenu();
		}

	}


	private int apresentarOpcoesMenu() {
		System.out.println("---------- Sistema FoodTruck ----------");
		System.out.println("\nOpcoes: ");
		System.out.println(OPCAO_MENU_LOGIN + " - Entrar");
		System.out.println(OPCAO_MENU_CRIAR_CONTA + " - Criar Conta");
		System.out.println(OPCAO_MENU_SAIR + " - Sair");
		System.out.print("\nDigite uma opção: ");
		return Integer.parseInt(input.nextLine());
	}

	private UsuarioVO realizarLogin() {
		UsuarioVO usuarioVO = new UsuarioVO();
		System.out.println("----------- informações ------------");
		System.out.print("Login: ");
		usuarioVO.setLogin(input.nextLine());
		System.out.print("Senha: ");
		usuarioVO.setSenha(input.nextLine());

		if (usuarioVO.getLogin().isEmpty() || usuarioVO.getSenha().isEmpty()) {
			System.out.println("Os campos de login e senha são obrigatórios!");
		} else {
			UsuarioController usuarioController = new UsuarioController();
			usuarioVO = usuarioController.realizarLoginController(usuarioVO);
			if (usuarioVO.getIdUsuario() == 0) {
				System.out.println("Usuário não encontrado!");
			}
			if (usuarioVO.getDataExpiracao() != null) {
				System.out.println("Usuário expirado!");

			}

		}
		return usuarioVO;
	}
	

	private void cadastrarNovoUsuario() {
		UsuarioVO usuarioVO = new UsuarioVO();
		usuarioVO.setTipoUsuario(TipoUsuarioVO.CLIENTE);
		
		MenuUsuario menuUsuario = new MenuUsuario();
		menuUsuario.cadastrarNovoUsuario(usuarioVO);
		
		
	}

}
