package view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import controller.ProdutoController;
import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class MenuProduto {

	private static final int OPCAO_MENU_CADASTRAR_PRODUTO = 1;
	private static final int OPCAO_MENU_CONSULTAR_PRODUTO = 2;
	private static final int OPCAO_MENU_ATUALIZAR_PRODUTO = 3;
	private static final int OPCAO_MENU_EXCLUIR_PRODUTO = 4;
	private static final int OPCAO_MENU_PRODUTO_VOLTAR = 9;

	private static final int OPCAO_MENU_CONSULTAR_TODOS_PRODUTOS = 1;
	private static final int OPCAO_MENU_CONSULTAR_UM_PRODUTO = 2;
	private static final int OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR = 9;

	Scanner input = new Scanner(System.in);

	public void apresentarMenuProduto() {
		int opcao = this.apresentarOpcoesMenu();
		while (opcao != OPCAO_MENU_PRODUTO_VOLTAR) {
			switch (opcao) {
			case OPCAO_MENU_CADASTRAR_PRODUTO: {
				// Método cadastrar produto
				ProdutoVO produtoVO = new ProdutoVO();
				this.cadastrarProduto(produtoVO);
				break;
			}
			case OPCAO_MENU_CONSULTAR_PRODUTO: {
				// Método consultar produto
				this.consultarProduto();
				break;
			}
			case OPCAO_MENU_ATUALIZAR_PRODUTO: {
				// método atualizar produto
				this.atualizarProduto();
				break;
			}
			case OPCAO_MENU_EXCLUIR_PRODUTO: {
				// método excluir produto
				this.excluirProduto();
				break;
			}

			default: {
				System.out.println("\nOpção inválida!!");

			}

			}
			opcao = this.apresentarOpcoesMenu();

		}

	}

	private int apresentarOpcoesMenu() {
		System.out.println("\n---------- Sistema FoodTruck ----------");
		System.out.println("---------- Menu de Produto ----------");
		System.out.println("\nOpções: ");
		System.out.println(OPCAO_MENU_CADASTRAR_PRODUTO + " - Cadastrar produto");
		System.out.println(OPCAO_MENU_CONSULTAR_PRODUTO + " - Consultar produto");
		System.out.println(OPCAO_MENU_ATUALIZAR_PRODUTO + " - Atualizar produto");
		System.out.println(OPCAO_MENU_EXCLUIR_PRODUTO + " - Excluir produto");
		System.out.println(OPCAO_MENU_PRODUTO_VOLTAR + " - Voltar");
		System.out.print("\nDigite uma opção: ");
		return Integer.parseInt(input.nextLine());

	}

	private void cadastrarProduto(ProdutoVO produtoVO) {
		if (produtoVO.getTipoProduto() == null) {
			do {
				produtoVO.setTipoProduto(TipoProdutoVO.getTipoProdutoVOPorValor(this.apresentarOpcoesTipoProduto()));
			} while (produtoVO.getTipoProduto() == null);
		}
		System.out.print("Digite o nome: ");
		produtoVO.setNome(input.nextLine());

		System.out.print("Digite o preço: ");
		produtoVO.setPreco(Double.parseDouble(input.nextLine()));

		produtoVO.setDataCadastro(LocalDateTime.now());

		if (this.validarCamposCadastro(produtoVO)) {
			ProdutoController produtoController = new ProdutoController();
			produtoVO = produtoController.cadastrarProdutoController(produtoVO);
			if (produtoVO.getIdproduto() != 0) {
				System.out.println("Produto cadastrado com sucesso!");
			} else
				System.out.println("Que pena!! Não foi possível cadastrar o produto!");
		}

	}

	private int apresentarOpcoesTipoProduto() {
		ProdutoController produtoController = new ProdutoController();
		ArrayList<TipoProdutoVO> listaTipoProdutoVO = produtoController.consultarTipoProdutoController();
		System.out.print("\n ----- Tipo de Produto -----");
		System.out.println("\nOpções: ");
		for (int i = 0; i < listaTipoProdutoVO.size(); i++) {
			System.out.println(listaTipoProdutoVO.get(i).getValor() + "-" + listaTipoProdutoVO.get(i));
		}
		System.out.print("\nDigite uma opção: ");
		return Integer.parseInt(input.nextLine());

	}

	private boolean validarCamposCadastro(ProdutoVO produtoVO) {
		boolean resultado = true;
		System.out.println();

		if (produtoVO.getNome() == null || produtoVO.getNome().isEmpty()) {
			System.out.println("O campo nome é obrigatório!");
			resultado = false;
		}

		if (Double.isNaN(produtoVO.getPreco())) {
			System.out.println("O campo preço é obrigatório!");
			resultado = false;
		}

		if (produtoVO.getDataCadastro() == null) {
			System.out.println("O campo data de cadastro é obrigatório!");
			resultado = false;
		}

		return resultado;

	}

	private void consultarProduto() {
		int opcao = this.apresentarOpcoesConsulta();
		ProdutoController produtoController = new ProdutoController();
		while (opcao != OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR) {
			switch (opcao) {
			case OPCAO_MENU_CONSULTAR_TODOS_PRODUTOS: {
				opcao = OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR;

				ArrayList<ProdutoVO> listaProdutoVO = ProdutoController.consultarTodosProdutosVigentesController();
				System.out.print("\n---------- RESULTADO DA CONSULTA ----------");
				System.out.printf("\n%3s  %-13s   %-20s  %-11s  %-25s", "ID", "TIPO PRODUTO", "NOME", "PREÇO",
						"DATA CADASTRO");
				for (int i = 0; i < listaProdutoVO.size(); i++) {
					listaProdutoVO.get(i).imprimir();
				}
				System.out.println();
				break;
			}
			case OPCAO_MENU_CONSULTAR_UM_PRODUTO: {
				opcao = OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR;
				ProdutoVO produtoVO = new ProdutoVO();
				System.out.print("\nInforme o código do produto: ");
				produtoVO.setIdProduto(Integer.parseInt(input.nextLine()));
				if (produtoVO.getIdproduto() != 0) {
					ProdutoVO produto = produtoController.consultarProdutoController(produtoVO);
					System.out.print("\n---------- RESULTADO DA CONSULTA ----------");
					System.out.printf("\n%3s  %-13s   %-20s  %-11s  %-25s  %-13s  ", "ID", "TIPO PRODUTO", "NOME",
							"PREÇO", "DATA CADASTRO", "DATA EXCLUSÃO");
					produto.imprimir();
					System.out.println();
				} else {
					System.out.println("O campo código do produto é obrigatório");
				}
				break;
			}
			default: {
				System.out.println("\nOpção inválida!");
				opcao = this.apresentarOpcoesConsulta();
			}

			}

		}

	}

	private int apresentarOpcoesConsulta() {
		System.out.println("\nInforme o tipo de consulta a ser realizada: ");
		System.out.println(OPCAO_MENU_CONSULTAR_TODOS_PRODUTOS + " - Consultar todos os produtos");
		System.out.println(OPCAO_MENU_CONSULTAR_UM_PRODUTO + " - Consultar um produto específico");
		System.out.println(OPCAO_MENU_CONSULTAR_PRODUTO_VOLTAR + " - Voltar");
		System.out.print("\nDigite uma opção: ");
		return Integer.parseInt(input.nextLine());
	}

	private void atualizarProduto() {
		ProdutoVO produtoVO = new ProdutoVO();
		System.out.print("\nInforme o código do produto: ");
		produtoVO.setIdProduto(Integer.parseInt(input.nextLine()));

		if (produtoVO.getTipoProduto() == null) {
			do {
				produtoVO.setTipoProduto(TipoProdutoVO.getTipoProdutoVOPorValor(this.apresentarOpcoesTipoProduto()));
			} while (produtoVO.getTipoProduto() == null);
		}
		System.out.print("Digite o nome: ");
		produtoVO.setNome(input.nextLine());

		System.out.print("Digite o preço: ");
		produtoVO.setPreco(Double.parseDouble(input.nextLine()));

		produtoVO.setDataCadastro(LocalDateTime.now());

		if (this.validarCamposCadastro(produtoVO)) {
			ProdutoController produtoController = new ProdutoController();
			boolean resultado = produtoController.atualizarProdutoController(produtoVO);
			if (resultado) {
				System.out.println("Produto atualizado com sucesso!");
			} else
				System.out.println("Não foi possível atualizar o produto!");
		}

	}

	private void excluirProduto() {
		ProdutoVO produtoVO = new ProdutoVO();
		System.out.print("\nInforme o código do produto: ");
		produtoVO.setIdProduto(Integer.parseInt(input.nextLine()));
		System.out.print("Digite a data de expiração no formato dd/MM/yyyy HH:mm:ss: ");
		produtoVO.setDataExclusao(LocalDateTime.parse(input.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));
		if (produtoVO.getIdproduto() == 0 || produtoVO.getDataExclusao() == null) {
			System.out.println("Os campos código do produto e data de expiração são obrigatórios!");
		} else {
			ProdutoController produtoController = new ProdutoController();
			boolean resultado = produtoController.excluirProdutoController(produtoVO);
			if (resultado) {
				System.out.println("\nProduto excluído com sucesso! ");
			} else {
				System.out.println("\nNão foi possivel excluir o produto! ");
			}
		}

	}

}
