package br.com.brq.itau.util.excecao;

import lombok.Getter;

@Getter
public enum TipoProblema {

	DADOS_INVALIDOS("dados-invalidos", "Dados inválidos"),
	ACESSO_NEGADO("acesso-negado", "Acesso negado"),
	ERRO_DE_SISTEMA("erro-de-sistema", "Erro de sistema"),
	PARAMETRO_INVALIDO("parametro-invalido", "Parâmetro inválido"),
	MENSAGEM_INCOMPREENSIVEL("mensagem-incompreensivel", "Mensagem incompreensível"),
	RECURSO_NAO_ENCONTRADO("recurso-nao-encontrado", "Recurso não encontrado"),
	ENTIDADE_EM_USO("entidade-em-uso", "Entidade em uso"),
	ERRO_NEGOCIO("erro-negocio", "Violação de regra de negócio");
	
	private String titulo;
	
	private String uri;
	
	TipoProblema(String path, String titulo) {
		this.uri = "https://localhost:800/" + path;
		this.titulo = titulo;
	}
	
}
