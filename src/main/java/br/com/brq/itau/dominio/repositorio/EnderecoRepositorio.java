package br.com.brq.itau.dominio.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.brq.itau.dominio.entidade.Endereco;

public interface EnderecoRepositorio extends JpaRepository<Endereco, Long>{

}
