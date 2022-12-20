package model.vo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProdutoVO {
	private int idproduto;
	private TipoProdutoVO tipoProduto;
	private String nome;
	private double preco;
	private LocalDateTime dataCadastro;
	private LocalDateTime dataExclusao;

	public ProdutoVO(int idproduto, TipoProdutoVO tipoProduto, String nome, double preco, LocalDateTime dataCadastro,
			LocalDateTime dataExclusao) {
		super();
		this.idproduto = idproduto;
		this.tipoProduto = tipoProduto;
		this.nome = nome;
		this.preco = preco;
		this.dataCadastro = dataCadastro;
		this.dataExclusao = dataExclusao;
	}

	public ProdutoVO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public int getIdproduto() {
		return idproduto;
	}

	public void setIdProduto(int idproduto) {
		this.idproduto = idproduto;
	}

	public TipoProdutoVO getTipoProduto() {
		return tipoProduto;
	}

	public void setTipoProduto(TipoProdutoVO tipoProduto) {
		this.tipoProduto = tipoProduto;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public double getPreco() {
		return preco;
	}

	public void setPreco(double preco) {
		this.preco = preco;
	}

	public LocalDateTime getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(LocalDateTime dataCadastro) {
		this.dataCadastro = dataCadastro;
	}

	public LocalDateTime getDataExclusao() {
		return dataExclusao;
	}

	public void setDataExclusao(LocalDateTime dataExclusao) {
		this.dataExclusao = dataExclusao;
	}

	public void imprimir() {
		System.out.printf("\n%3d  %-13s  %-20s  %-7s  %-24s  %-24s", this.getIdproduto(), this.getTipoProduto(),
				this.getNome(), this.getPreco(), this.validarData(this.getDataCadastro()),
				this.validarData(this.getDataExclusao()));
	}

	private String validarData(LocalDateTime data) {
		String resultado = "";
		if (data != null) {
			resultado = data.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
		}
		return resultado;
	}
}
