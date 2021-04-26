package br.com.brq.itau.util.excecao;

public class NegocioExcecao extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public NegocioExcecao(String mensagem) {
		super(mensagem);
	}
	
	public NegocioExcecao(String mensagem, Throwable causa) {
		super(mensagem, causa);
	}
	
}
