package br.com.brq.itau.servico;

import br.com.brq.itau.dominio.dto.EnderecoDTO;

public interface EnderecoServico {

	EnderecoDTO obterCep(String nome, String rg, String cpf, String cep);

}
