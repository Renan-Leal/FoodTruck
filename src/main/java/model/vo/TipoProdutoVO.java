package model.vo;

public enum TipoProdutoVO {
	COMIDA(1), BEBIDA(2), SOBREMESA(3);

	private int valor;

	private TipoProdutoVO(int valor) {
		this.valor = valor;
	}

	public int getValor() {
		return valor;
	}

	public static TipoProdutoVO getTipoProdutoVOPorValor(int valor) {
		TipoProdutoVO tipoProdutoVO = null;
		for (TipoProdutoVO elemento : TipoProdutoVO.values()) {
			if (elemento.getValor() == valor) {
				tipoProdutoVO = elemento;
			}
		}
		return tipoProdutoVO;
	}

}
