package br.com.brq.itau.servico.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;

import br.com.brq.itau.controlador.api.EnderecoRestControlador;
import br.com.brq.itau.dominio.repositorio.EnderecoRepositorio;

@ExtendWith(MockitoExtension.class)
class EnderecoServicoImplTest {

	@Mock
	EnderecoServicoImpl servico;
	
	@InjectMocks
	EnderecoRestControlador controlador;
	
	@Mock
	RestTemplate restTemplate;

	@Mock
	EnderecoRepositorio repositorio;
	
	private Map<String, String> params = new HashMap<>();

	@Test
	void deveBuscarTodasPessoas() {
		params.put("cep", "71060211");
		servico.setParams(params);
		
		assertEquals(200, controlador.obterCep("Teste", "222", "124881", "71060221").getStatusCodeValue());
	}

}
