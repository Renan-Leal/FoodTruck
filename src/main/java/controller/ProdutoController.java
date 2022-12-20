package controller;

import java.util.ArrayList;

import model.bo.ProdutoBO;
import model.vo.ProdutoVO;
import model.vo.TipoProdutoVO;

public class ProdutoController {

	public static ArrayList<ProdutoVO> consultarTodosProdutosVigentesController() {
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.consultarTodosProdutosVigentesBO();
	}

	public ArrayList<TipoProdutoVO> consultarTipoProdutoController() {
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.consultarTipoProdutoBO();

	}

	public ProdutoVO cadastrarProdutoController(ProdutoVO produtoVO) {
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.cadastrarProdutoBO(produtoVO);
	}

	public ProdutoVO consultarProdutoController(ProdutoVO produtoVO) {
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.consultarProdutoBO(produtoVO);
	}

	public boolean atualizarProdutoController(ProdutoVO produtoVO) {
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.atualizarProdutoBO(produtoVO);

	}

	public boolean excluirProdutoController(ProdutoVO produtoVO) {
		ProdutoBO produtoBO = new ProdutoBO();
		return produtoBO.excluirProdutoBO(produtoVO);
	}

}
