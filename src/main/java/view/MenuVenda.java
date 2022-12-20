package view;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

import controller.EntregaController;
import controller.ProdutoController;
import controller.VendaController;
import model.vo.ItemVendaVO;
import model.vo.ProdutoVO;
import model.vo.TipoUsuarioVO;
import model.vo.UsuarioVO;
import model.vo.VendaVO;

public class MenuVenda {
	private static final int OPCAO_MENU_CADASTRAR_VENDA = 1;
	private static final int OPCAO_MENU_CANCELAR_VENDA = 2;
	private static final int OPCAO_MENU_CANCELAR_ENTREGA = 3;
	private static final int OPCAO_MENU_SITUACAO_ENTREGA = 4;
	private static final int OPCAO_MENU_VENDA_VOLTAR = 9;

	private static int NUMERO_PEDIDO = 0;
	private static double PERCENTUAL = 0.05;

	Scanner input = new Scanner(System.in);

	public void apresentarMenuVenda(UsuarioVO usuarioVO) {
		int opcao = this.apresentarOpcoesMenu(usuarioVO);
		while (opcao != OPCAO_MENU_VENDA_VOLTAR) {
			switch (opcao) {
			case OPCAO_MENU_CADASTRAR_VENDA: {
				if (!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ENTREGADOR)) {
					this.cadastrarVenda(usuarioVO);
				}
				break;
			}
			case OPCAO_MENU_CANCELAR_VENDA: {
				if (!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ENTREGADOR)) {
					this.cancelarVenda();
				}
				break;
			}
			case OPCAO_MENU_CANCELAR_ENTREGA: {
				if (!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ENTREGADOR)) {
					this.cancelarEntrega();
				}
				break;
			}
			case OPCAO_MENU_SITUACAO_ENTREGA: {
				if (!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.CLIENTE)) {
					this.atualizarSituacaoEntrega();
				}
				break;
			}
			default: {
				System.out.println("\nOpção Inválida!!!");
			}
			}
			opcao = this.apresentarOpcoesMenu(usuarioVO);
		}
	}

	private int apresentarOpcoesMenu(UsuarioVO usuarioVO) {
		System.out.println("\n---------- Sistema FoodTruck ----------");
		System.out.println("\n---------- Menu Vendas ----------");
		System.out.println("\nOpções: ");
		// O ponto e exclamacao inverte a sentenca. Ou seja, qualquer um dos perfil de
		// usuario, menos o entregador podem usufruir do cadastro e do cancelamento das
		// vendas
		if (!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.ENTREGADOR)) {
			System.out.println(OPCAO_MENU_CADASTRAR_VENDA + " - Cadastrar Venda");
			System.out.println(OPCAO_MENU_CANCELAR_VENDA + " - Cancelar Venda");
			System.out.println(OPCAO_MENU_CANCELAR_ENTREGA + " - Cancelar Entrega");
		}
		// O único usuario que não pode atualizar o status da entrega é o cliente
		if (!usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.CLIENTE)) {
			System.out.println(OPCAO_MENU_SITUACAO_ENTREGA + " - Situação Entrega");
		}
		System.out.println(OPCAO_MENU_VENDA_VOLTAR + " - Voltar");
		System.out.print("\nDigite uma opção: ");

		return Integer.parseInt(input.nextLine());
	}

	
	private void cadastrarVenda(UsuarioVO usuarioVO) {
		ArrayList<ProdutoVO> listaProdutosVO = this.listarProdutos();
		VendaVO vendaVO = new VendaVO();
		if (usuarioVO.getTipoUsuario().equals(TipoUsuarioVO.CLIENTE)) {
			vendaVO.setIdUsuario(usuarioVO.getIdUsuario());
		} else {
			System.out.print("Informe o código do cliente: ");
			vendaVO.setIdUsuario(Integer.parseInt(input.nextLine()));
		}
		vendaVO.setNumeroPedido(this.gerarNumeroPedido());
		vendaVO.setDataVenda(LocalDateTime.now());
		double subTotal = 0;
		ArrayList<ItemVendaVO> listaItemVendaVO = new ArrayList<ItemVendaVO>();
		String continuar = "N";
		do {
			ItemVendaVO itemVendaVO = new ItemVendaVO();
			subTotal += this.cadastrarItemVendaVO(itemVendaVO, listaProdutosVO);
			listaItemVendaVO.add(itemVendaVO);
			System.out.print("Deseja incluir mais um item? [S - N]: ");
			continuar = input.nextLine();
		} while (continuar.equalsIgnoreCase("S"));
		vendaVO.setListaItemVendaVO(listaItemVendaVO);
		System.out.print("Pedido é para entregar [S - N]: ");
		String opcaoEntrega = input.nextLine();
		if (opcaoEntrega.toUpperCase().equals("S") || opcaoEntrega.toUpperCase().equals("N")) {
			double taxaEntrega = subTotal * PERCENTUAL;
			double totalConta = subTotal;
			if (opcaoEntrega.toUpperCase().equals("S")) {
				vendaVO.setFlagEntrega(true);
				vendaVO.setTaxaEntrega(taxaEntrega);
				totalConta += taxaEntrega;
			}
			System.out.println("Total da Conta - R$ " + totalConta);
			if (this.validarCamposCadastro(vendaVO)) {
				VendaController vendaController = new VendaController();
				vendaVO = vendaController.cadastrarVendaController(vendaVO);
				if (vendaVO.getIdVenda() != 0) {
					System.out.println("\nVenda cadastrada com sucesso.");
				} else {
					System.out.println("Não foi possível cadastrar a venda.");
				}
			} else {
				System.out.println("Você digitou uma opção inválida.");
			}
		}
	}

	private ArrayList<ProdutoVO> listarProdutos() {
		ArrayList<ProdutoVO> listaProdutosVO = ProdutoController.consultarTodosProdutosVigentesController();
		System.out.println("\n---------- Lista de Produtos ----------");
		System.out.printf("\n%3s  %-13s  %-20s  %-7s  %-24s  ", "ID", "TIPO PRODUTO", "NOME", "PRECO", "DATA CADASTRO");
		for (int i = 0; i < listaProdutosVO.size(); i++) {
			listaProdutosVO.get(i).imprimir();
		}
		System.out.println("\n");
		return listaProdutosVO;
	}

	private int gerarNumeroPedido() {
		if (NUMERO_PEDIDO < 99) {
			NUMERO_PEDIDO++;
		} else {
			NUMERO_PEDIDO = 0;
		}
		return NUMERO_PEDIDO;
	}

	// Seria bom fazer verificar se o código digitado está na lista de produtos
	private double cadastrarItemVendaVO(ItemVendaVO itemVendaVO, ArrayList<ProdutoVO> listaProdutosVO) {
		System.out.print("Informe o código do produto: ");
		itemVendaVO.setIdProduto(Integer.parseInt(input.nextLine()));
		System.out.print("Informe a quantidade do produto: ");
		itemVendaVO.setQuantidade(Integer.parseInt(input.nextLine()));
		double valor = 0;
		for (ProdutoVO produto : listaProdutosVO) {
			if (produto.getIdproduto() == itemVendaVO.getIdProduto()) {
				valor = produto.getPreco() * itemVendaVO.getQuantidade();
			}
		}
		return valor;
	}

	private boolean validarCamposCadastro(VendaVO vendaVO) {
		boolean resultado = true;
		System.out.println();
		if (vendaVO.getIdUsuario() == 0) {
			System.out.println("O campo código do usuário é obrigatório");
			resultado = false;
		}
		if (vendaVO.getListaItemVendaVO() == null || vendaVO.getListaItemVendaVO().isEmpty()) {
			System.out.println("O campo dos produtos vendidos é obrigatório.");
			resultado = false;
		}
		return resultado;
	}

	// Será informado o código da venda e a data e hora do cancelamento
	private void cancelarVenda() {
		VendaVO vendaVO = new VendaVO();
		System.out.print("\nInforme o código da venda: ");
		vendaVO.setIdVenda(Integer.parseInt(input.nextLine()));
		System.out.print("Digite a data do cancelamento no formato dd//MM/yyyy HH:mm:ss: ");
		vendaVO.setDataCancelamento(
				LocalDateTime.parse(input.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

		if (vendaVO.getIdVenda() == 0 || vendaVO.getDataCancelamento() == null) {
			System.out.println("Os campos código da venda e data de cancelamento são obrigatórios.");
		} else {
			VendaController vendaController = new VendaController();
			boolean resultado = vendaController.cancelarVendaController(vendaVO);
			if (resultado) {
				System.out.println("\nVenda cancelada com sucesso.");
			} else {
				System.out.println("Não foi possível cancelar a venda.");
			}
		}
	}

	private void cancelarEntrega() {
		VendaVO vendaVO = new VendaVO();
		System.out.print("\nInforme o código da venda: ");
		vendaVO.setIdVenda(Integer.parseInt(input.nextLine()));
		System.out.print("Digite a data do cancelamento no formato dd//MM/yyyy HH:mm:ss: ");
		vendaVO.setDataCancelamento(
				LocalDateTime.parse(input.nextLine(), DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")));

		if (vendaVO.getIdVenda() == 0 || vendaVO.getDataCancelamento() == null) {
			System.out.println("Os campos código da venda e data de cancelamento são obrigatórios.");
		} else {
			EntregaController entregaController = new EntregaController();
			boolean resultado = entregaController.cancelarEntregaController(vendaVO);
			if (resultado) {
				System.out.println("\nEntrega cancelada com sucesso.");
			} else {
				System.out.println("Não foi possível cancelar a entrega.");
			}
		}
	}
	
	
	// Será informado o código da venda
	private void atualizarSituacaoEntrega() {
		VendaVO vendaVO = new VendaVO();
		System.out.print("\nInforme o código da venda: ");
		vendaVO.setIdVenda(Integer.parseInt(input.nextLine()));

		EntregaController entregaController = new EntregaController();
		boolean resultado = entregaController.atualizarSituacaoEntregaController(vendaVO);
		if (resultado) {
			System.out.println("\nSituação da entrega da venda atualizada com sucesso.");
		} else {
			System.out.println("Não foi possível atualizar a situação da entrega da venda");
		}
	}
}
