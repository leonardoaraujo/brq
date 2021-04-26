package br.com.brq.itau.dominio.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class EnderecoPesquisaDTO implements Serializable {

	private static final long serialVersionUID = 1L;

	@JsonProperty(value = "nome")
	private String nome;
	
	@JsonProperty(value = "rg")
	private String rg;
	
	@JsonProperty(value = "cpf")
	private String cpf;
	
	@JsonProperty(value = "cep")
	private String cep;
}
