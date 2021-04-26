package br.com.brq.itau.util.excecao;

public abstract class EntidadeNaoEncontradaExcecao extends NegocioExcecao {

	private static final long serialVersionUID = 1L;

	protected EntidadeNaoEncontradaExcecao(String mensagem) {
		super(mensagem);
	}
	
}
