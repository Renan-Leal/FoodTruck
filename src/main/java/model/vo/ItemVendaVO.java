package model.vo;

public class ItemVendaVO {
	private int idItemVenda;
	private VendaVO idVenda;
	private int idProduto;
	private int quantidade;

	public ItemVendaVO(int idItemVenda, VendaVO idVenda, int idProduto, int quantidade) {
		super();
		this.idItemVenda = idItemVenda;
		this.idVenda = idVenda;
		this.idProduto = idProduto;
		this.quantidade = quantidade;
	}

	public ItemVendaVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getIdItemVenda() {
		return idItemVenda;
	}

	public void setIdItemVenda(int idItemVenda) {
		this.idItemVenda = idItemVenda;
	}

	public VendaVO getIdVenda() {
		return idVenda;
	}

	public void setIdVenda(VendaVO idVenda) {
		this.idVenda = idVenda;
	}

	public int getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(int idProduto) {
		this.idProduto = idProduto;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

}
