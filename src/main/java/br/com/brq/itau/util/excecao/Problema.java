package br.com.brq.itau.util.excecao;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Getter;

@ApiModel("Problema")
@JsonInclude(Include.NON_NULL)
@Getter
@Builder
public class Problema {

	@ApiModelProperty(example = "400", position = 1)
	private Integer estadoHttp;
	
	@ApiModelProperty(example = "2019-12-01T18:09:02.70844Z", position = 5)
	private OffsetDateTime dataHora;
	
	@ApiModelProperty(example = "https://localhost:8080/dados-invalidos", position = 10)
	private String tipo;
	
	@ApiModelProperty(example = "Dados inválidos", position = 15)
	private String titulo;
	
	@ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.", 
			position = 20)
	private String detalhe;
	
	@ApiModelProperty(example = "Um ou mais campos estão inválidos. Faça o preenchimento correto e tente novamente.", 
			position = 25)
	private String mensagemUsuario;
	
	@ApiModelProperty(value = "Lista de objetos ou campos que geraram o erro (opcional)", 
			position = 30)
	private List<Object> objetos;
	
	@ApiModel("ObjetoProblema")
	@Getter
	@Builder
	public static class Object {
		
		@ApiModelProperty(example = "preco")
		private String nome;
		
		@ApiModelProperty(example = "O preço é obrigatório")
		private String mensagemUsuario;
		
	}
	
}
