package br.com.ibm.cadeiabatch.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Usuario {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private String nome;
	
	private String usuario;

	private String area;
	
	private String email;
	
	@JsonIgnore
	private String senha;
	
	@OneToMany(mappedBy="usuario")
	private List<Log> logs;
	
	@OneToMany(mappedBy="responsavel", cascade=CascadeType.ALL)
	private List<Chamado> chamados;
		
	private Integer nivel;

	@ManyToMany
	private List<Empresa> empresas;

	private Integer empresaLogada;

	@JsonIgnore
	@ManyToMany
	@JoinTable(name = "CATEGORIA_USUARIO",
			joinColumns = @JoinColumn(name = "id_usuario"),
			inverseJoinColumns = @JoinColumn(name = "id_categoria"))
	private List<Categoria> categorias = new ArrayList<>();
	
	public Usuario() {
		super();
	}
	public Usuario(long id, String nome, String usuario, String email, String senha, List<Log> logs, Integer nivel) {
		super();
		this.id = id;
		this.nome = nome;
		this.usuario = usuario;
		this.email = email;
		this.senha = senha;
		this.logs = logs;	
		this.nivel = nivel;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getSenha() {
		return senha;
	}
	public void setSenha(String senha) {
		this.senha = senha;
	}
	public List<Log> getLogs() {
		return logs;
	}
	public void setLogs(List<Log> logs) {
		this.logs = logs;
	}
	public List<Chamado> getChamados() {
		return chamados;
	}
	public void setChamados(List<Chamado> chamados) {
		this.chamados = chamados;
	}
	public Integer getNivel() {
		return nivel;
	}
	public void setNivel(Integer nivel) {
		this.nivel = nivel;
	}
	public List<Categoria> getCategorias() {
		return categorias;
	}
	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	public List<Empresa> getEmpresas() {
		return empresas;
	}
	public void setEmpresa(List<Empresa> empresas) {
		this.empresas = empresas;
	}
	public Integer getEmpresaLogada() {	return empresaLogada;}
	public void setEmpresaLogada(Integer empresaLogada) {this.empresaLogada = empresaLogada;}
}
