package br.com.ibm.cadeiabatch.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

public class ExtratoHoras {
		
	private BigInteger numero;
	private String descricao;
	private Integer tipo;
	private Integer categoria;
	private Date data;
	private Integer mes;
	private BigDecimal horas;
	private String nome;
	private String nome_empresa;
	
	public ExtratoHoras() {
		super();
	}

	public ExtratoHoras(BigInteger numero, String descricao, Integer tipo, Integer categoria, Date data, Integer mes,
			BigDecimal horas, String nome, String nome_empresa) {
		super();
		this.numero = numero;
		this.descricao = descricao;
		this.tipo = tipo;
		this.categoria = categoria;
		this.data = data;
		this.mes = mes;
		this.horas = horas;
		this.nome = nome;
		this.nome_empresa = nome_empresa;
	}

	public BigInteger getNumero() {
		return numero;
	}

	public void setNumero(BigInteger numero) {
		this.numero = numero;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Integer getTipo() {
		return tipo;
	}

	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}

	public Integer getCategoria() {
		return categoria;
	}

	public void setCategoria(Integer categoria) {
		this.categoria = categoria;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public Integer getMes() {
		return mes;
	}

	public void setMes(Integer mes) {
		this.mes = mes;
	}

	public BigDecimal getHoras() {
		return horas;
	}

	public void setHoras(BigDecimal horas) {
		this.horas = horas;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getNome_empresa() {return nome_empresa;}

	public void setNome_empresa(String nome_empresa) { this.nome_empresa = nome_empresa;}
}
