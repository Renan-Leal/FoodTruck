package model.bo;

import java.util.ArrayList;
import java.util.Random;

import model.dao.EntregaDAO;
import model.dao.UsuarioDAO;
import model.dao.VendaDAO;
import model.vo.EntregaVO;
import model.vo.SituacaoEntregaVO;
import model.vo.UsuarioVO;
import model.vo.VendaVO;

public class EntregaBO {

	public boolean cadastrarEntregaBO(int idVenda) {
		boolean retorno = true;
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		ArrayList<UsuarioVO> listaEntregadores = usuarioDAO.consultarListaEntregadores();
		if (listaEntregadores.isEmpty()) {
			System.out.println("Não existem entregadores cadastrados na base de dados!");
			retorno = false;
		} else {
			Random gerador = new Random();
			UsuarioVO entregador = listaEntregadores.get(gerador.nextInt(listaEntregadores.size()));
			EntregaVO entregaVO = new EntregaVO(0, idVenda, entregador.getIdUsuario(),
					SituacaoEntregaVO.PEDIDO_REALIZADO, null);
			EntregaDAO entregaDAO = new EntregaDAO();
			boolean resultado = entregaDAO.cadastrarEntregaDAO(entregaVO);
			if (!resultado) {
				System.out.println("Houve um problema ao tentar cadastrar a entrega!");
				retorno = false;
			}
		}
		return retorno;
	}

	public boolean atualizarSituacaoEntregaBO(VendaVO vendaVO) {
		boolean retorno = false;
		EntregaDAO entregaDAO = new EntregaDAO();
		VendaBO vendaBO = new VendaBO();
		boolean resultado = vendaBO.verificarVendaParaAtualizarSituacaoEntrega(vendaVO);
		if (resultado) {
			retorno = entregaDAO.atualizarSituacaoEntregaDAO(vendaVO);
		}
		return retorno;
	}

	public boolean cancelarEntregaBO(VendaVO vendaVO) {
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
							resultado = entregaDAO.cancelarEntregaDAO(vendaVO, SituacaoEntregaVO.ENTREGA_CANCELADA.getValor());

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
						System.out.println("\nNão existe entrega para ser cancelada");
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

}
