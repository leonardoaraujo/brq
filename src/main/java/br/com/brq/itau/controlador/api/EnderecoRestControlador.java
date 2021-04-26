package br.com.brq.itau.controlador.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.brq.itau.dominio.dto.EnderecoDTO;
import br.com.brq.itau.servico.EnderecoServico;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/ceps")
@Api( tags = "Endereço")
public class EnderecoRestControlador {
	
	@Autowired
	private EnderecoServico enderecoServico;

	@GetMapping(produces = { "application/json" })
	@ApiOperation(value = "Pesquisa Endereço", tags = "Endereço")
	public ResponseEntity<EnderecoDTO> obterCep(@RequestParam(name = "nome") String nome,
			@RequestParam(name = "rg") String rg,
			@RequestParam(name = "cpf") String cpf,
			@RequestParam(name = "cep") String cep) {
		return new ResponseEntity<>(enderecoServico.obterCep(nome, rg, cpf, cep), HttpStatus.OK);
	}
}
