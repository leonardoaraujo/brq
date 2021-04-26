package br.com.brq.itau.dominio.entidade;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity(name = "endereco")
@Table(name="endereco",schema = "itau")
public class Endereco implements Serializable {

	private static final long serialVersionUID = 7309680245862563917L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;

	@Column(name="bairro", length = 45)
	private String bairro;

	@Column(name="cep", length = 8)
	private String cep;

	@Column(name="complemento", length = 45)
	private String complemento;

	@Column(name="cpf", length = 11)
	private String cpf;

	@Column(name="ddd")
	private Integer ddd;

	@Column(name="ibge", length = 45)
	private String ibge;

	@Column(name="localidade", length = 100)
	private String localidade;

	@Column(name="logradouro", length = 100)
	private String logradouro;

	@Column(name="nome", length = 100)
	private String nome;

	@Column(name="rg", length = 15)
	private String rg;


	@Column(name="uf", length = 2)
	private String uf;
}
