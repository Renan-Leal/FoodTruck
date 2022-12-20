package model.bo;

import model.dao.EntregaDAO;
import model.dao.ProdutoDAO;
import model.dao.UsuarioDAO;
import model.dao.VendaDAO;
import model.vo.EntregaVO;
import model.vo.ItemVendaVO;
import model.vo.SituacaoEntregaVO;
import model.vo.VendaVO;

public class VendaBO {

	public VendaVO cadastrarVendaBO(VendaVO vendaVO) {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		// Verificar se o usuario existe
		if (usuarioDAO.verificarExistenciaPorUsuarioDAO(vendaVO.getIdUsuario())) {
			ProdutoDAO produtoDAO = new ProdutoDAO();
			boolean listaItensEValida = true;
			for (ItemVendaVO itemVendaVO : vendaVO.getListaItemVendaVO()) {
				// Verificar se o produto selecionado existe na tabela de produto
				// Verificar se a lista é valida
				if (!produtoDAO.verificarExistenciaRegistroProdutoPorIdProdutoDAO(itemVendaVO.getIdProduto())) {
					System.out
							.println("O produto de id " + itemVendaVO.getIdProduto() + " não existe na base de dados.");
					listaItensEValida = false;
				}
			}
			if (listaItensEValida) {
				VendaDAO vendaDAO = new VendaDAO();
				// precisa cadastrar a venda pq para lançar o item de venda é necessário do id
				// da venda
				vendaVO = vendaDAO.cadastrarVendaDAO(vendaVO);
				// se existe uma venda o id é >0 e portanto entra como true
				if (vendaVO.getIdVenda() != 0) {
					// Cadastrar os itens de venda
					boolean resultado = vendaDAO.cadastrarItemVendaDAO(vendaVO);
					// se for positivo é pq a lista de itens já está completa e aí não entra
					if (!resultado) {
						System.out.println("\nNão foi possível incluir algum item do produto.");
					}
					// se a vendaVO tem entrega então é true e entra no processo
					if (vendaVO.isFlagEntrega()) {
						// se tem alguma regra é passado para o BO, se for apenas uma validação é feito
						// pelo DAO
						EntregaBO entregaBO = new EntregaBO();
						resultado = entregaBO.cadastrarEntregaBO(vendaVO.getIdVenda());
						if (!resultado) {
							System.out.println("\nNão foi possível cadastrar a Entrega.");
						}
					}
				} else {
					System.out.println("\nNão foi possível cadastrar a Venda.");
				}
			}
		} else {
			System.out.println("O usuário desta venda não existe na base de dados.");
		}
		return vendaVO;
	}

	public boolean cancelarVendaBO(VendaVO vendaVO) {
		boolean retorno = false;
		EntregaDAO entregaDAO = new EntregaDAO();
		VendaDAO vendaDAO = new VendaDAO();

		// Verificar se a venda existe na base de dados
		boolean vendaBanco = vendaDAO.consultarVendaDAO(vendaVO);
		if (vendaBanco) {
			boolean resultado = vendaDAO.verificarCancelamentoPorIdVendaDAO(vendaVO.getIdVenda());
			// Verificar se a venda já está cancelada na base de dados
			if (!resultado) {
				// Verificar se a data de cancelamento é posterior a data de venda
				if (vendaDAO.verificarDataCancelamentoVendaDAO(vendaVO)) {
					// Se houver entrega verificar se a entrega já foi realizada ou se esta em rota
					// de entrega
					if (!vendaVO.isFlagEntrega()) {
						EntregaVO entregaVO = entregaDAO.consultarEntregaPorIdVendaDAO(vendaVO.getIdVenda());
						if (entregaVO.getSituacaoEntrega().getValor() <= SituacaoEntregaVO.PREPARANDO_PEDIDO
								.getValor()) {
							resultado = vendaDAO.cancelarVendaDAO(vendaVO);
							entregaDAO.cancelarEntregaDAO(vendaVO, SituacaoEntregaVO.PEDIDO_CANCELADO.getValor());
							
							if (resultado) {
								retorno = vendaDAO.cancelarVendaDAO(vendaVO);

							}
						} else {
							entregaVO.getSituacaoEntrega();
							System.out.println(
									"Não foi possível cancelar a entrega pois já se encontra: " + SituacaoEntregaVO
											.getSituacaoEntregaVOPorValor(entregaVO.getSituacaoEntrega().getValor()));
						}
					} else {
						resultado = vendaDAO.cancelarVendaDAO(vendaVO);
						if (resultado) {
							retorno = true;
						}
					}
				} else {
					System.out.println("\nA data de cancelamento é anterior à data de venda. Tem algo errado!");
				}
			} else {
				System.out.println("\nVenda ja se encontra cancelada na base de dados.");
			}
		} else {
			System.out.println("\nVenda não localizada na base de dados.");
		}

		return retorno;
	}

	// Verificar se a venda existe na base de dados
	// Verificar se a venda possui entrega
	// Verificar se a venda não foi cancelada
	// Verificar se a venda já foi entregue e mudar a situacao da entrega
	public boolean verificarVendaParaAtualizarSituacaoEntrega(VendaVO vendaVO) {
		VendaDAO vendaDAO = new VendaDAO();
		EntregaDAO entregaDAO = new EntregaDAO();
		boolean retorno = false;
		if (vendaDAO.verificarExistenciaRegistroPorIdVendaDAO(vendaVO.getIdVenda())) {
			if (vendaDAO.verificarVendaPossuiEntrega(vendaVO.getIdVenda())) {
				if (!vendaDAO.verificarCancelamentoPorIdVendaDAO(vendaVO.getIdVenda())) {
					if (entregaDAO.consultarEntregaPorIdVendaDAO(vendaVO.getIdVenda()).getDataEntrega() == null) {
						retorno = true;

					} else {
						System.out.println("\nVenda já teve a entrega relizada.");
					}
				} else {
					System.out.println("\nVenda já se encontra cancelada na base de dados.");
				}
			} else {
				System.out.println("\nVenda não possui entrega.");
			}
		} else {
			System.out.println("\nVenda não localizada na base de dados.");
		}
		return retorno;
	}
}
