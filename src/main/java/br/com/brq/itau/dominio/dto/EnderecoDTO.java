package br.com.brq.itau.dominio.dto;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import br.com.brq.itau.dominio.entidade.Endereco;
import lombok.Data;

@Data
public class EnderecoDTO implements Serializable {

	private static final long serialVersionUID = 7309680245862563917L;
	
	@JsonProperty(value = "id")
	private Long id;
	
	@JsonProperty(value = "nome")
	private String nome;
	
	@JsonProperty(value = "rg")
	private String rg;
	
	@JsonProperty(value = "cpf")
	private String cpf;
	
	@JsonProperty(value = "cep")
	private String cep;
	
	@JsonProperty(value = "logradouro")
	private String logradouro;
	
	@JsonProperty(value = "complemento")
	private String complemento;
	
	@JsonProperty(value = "bairro")
	private String bairro;
	
	@JsonProperty(value = "localidade")
	private String localidade;
	
	@JsonProperty(value = "uf")
	private String uf;
	
	@JsonProperty(value = "ibge")
	private String ibge;

	public static Endereco toEntity (EnderecoDTO enderecoDTO) {
		Endereco endereco = new Endereco();
		endereco.setCep(enderecoDTO.getCep());
		endereco.setLogradouro(enderecoDTO.getLogradouro());
		endereco.setComplemento(enderecoDTO.getComplemento());
		endereco.setBairro(enderecoDTO.getBairro());
		endereco.setLocalidade(enderecoDTO.getLocalidade());
		endereco.setUf(enderecoDTO.getUf());
		endereco.setIbge(enderecoDTO.getIbge());
		endereco.setNome(enderecoDTO.getNome());
		endereco.setRg(enderecoDTO.getRg());
		endereco.setCpf(enderecoDTO.getCpf());
		return endereco;
	}

}
