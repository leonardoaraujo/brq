package br.com.brq.itau.servico.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.brq.itau.dominio.dto.EnderecoDTO;
import br.com.brq.itau.dominio.entidade.Endereco;
import br.com.brq.itau.dominio.repositorio.EnderecoRepositorio;
import br.com.brq.itau.servico.EnderecoServico;
import br.com.brq.itau.util.excecao.NegocioExcecao;

@Service
public class EnderecoServicoImpl implements EnderecoServico {
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private EnderecoRepositorio enderecoRepositorio;
	
	private Map<String, String> params = new HashMap<>();
	
	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	@Bean
	public RestTemplate restTemplate() {
	    return new RestTemplate();
	}

	@Override
	public EnderecoDTO obterCep(String nome, String rg, String cpf, String cep) {
		String uri = "http://viacep.com.br/ws/{cep}/json/";
		EnderecoDTO enderecoDTO;
		params.put("cep", cep);
		
		try {
			enderecoDTO = restTemplate.getForObject(uri, EnderecoDTO.class, params);
			Endereco endereco = EnderecoDTO.toEntity(enderecoDTO);
			Endereco enderecoRetorno = enderecoRepositorio.save(endereco);
			enderecoDTO.setId(enderecoRetorno.getId());
			return enderecoDTO;
		} catch (Exception e) {
			throw new NegocioExcecao("Cep Não Encontrado.");
		}

	}

}
