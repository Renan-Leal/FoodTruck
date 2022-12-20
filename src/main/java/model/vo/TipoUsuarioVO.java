package model.vo;

public enum TipoUsuarioVO {

	// enumerando os usuários e seus tipos
	ADMINISTRADOR(1), CLIENTE(2), FUNCIONARIO(3), ENTREGADOR(4);

	// Atributo que a classe enumerate recebe
	private int valor;

	// construtor para atribuir valor ao usuário cadastrado no sistema
	private TipoUsuarioVO(int valor) {
		this.valor = valor;
	}

	// pegar o valor
	public int getValor() {
		return valor;
	}

	// Dado um valor(1,2,3,4), retorna o tipo do usuário.
	// Essa estrutura de for funciona da seguinte forma: Tipo do Objeto, nome de uma
	// variável qualquer: array de elementos do tipo informado anteriormente.
	// A função .values() retorna um array com os valores, ou seja, no primeiro loop
	// o valor é ADM e assim por diante.
	public static TipoUsuarioVO getIipoUsuarioVOPorValor(int valor) {
		TipoUsuarioVO tipoUsuarioVO = null;
		for (TipoUsuarioVO elemento : TipoUsuarioVO.values()) {
			if (elemento.getValor() == valor) {
				tipoUsuarioVO = elemento;
			}
		}
		return tipoUsuarioVO;
	}

}
