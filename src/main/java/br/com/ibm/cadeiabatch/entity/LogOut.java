package br.com.ibm.cadeiabatch.entity;

import java.util.Calendar;
import java.util.Date;

import br.com.ibm.cadeiabatch.enums.Nivel;

public class LogOut {
	
	private long id;
	
	private long incidente;

    private Nivel nivel;
    
    private String usuario;

    private String frente;

	private String job;
	
	private String descricao;

	private Date dataCriacao;

    private Calendar dataHoraCriacao;
	
	private Date dataAtualizacao;
	
	private String dataHoraCriaçãoFormatado;
	
	private Calendar dataHoraAtualizacao;
	
	public LogOut() {
		super();
    }
    public LogOut(Log log) {
        super();
        this.id = log.getId();
		this.incidente = log.getIncidente();
		this.nivel = log.getNivel();
        this.descricao = log.getDescricao();
        this.job = log.getJob();
        this.usuario = log.getUsuario().getNome();
        this.frente = log.getUsuario().getArea();
		this.dataCriacao = log.getDataCriacao();
		this.dataHoraCriacao = log.getDataHoraCriacao();
		this.dataAtualizacao = log.getDataAtualizacao();
		this.dataHoraAtualizacao = log.getDataHoraAtualizacao();
		this.dataHoraCriaçãoFormatado = log.getDataHoraCriacaoFormatado();
	}
	public LogOut(long id, long incidente, Nivel nivel, String descricao, String usuario, String frente, Date dataCriacao,
			Calendar dataHoraCriacao, Date dataAtualizacao, Calendar dataHoraAtualizacao) {
		super();
		this.id = id;
		this.incidente = incidente;
		this.nivel = nivel;
		this.descricao = descricao;
        this.usuario = usuario;
        this.frente = frente;
		this.dataCriacao = dataCriacao;
		this.dataHoraCriacao = dataHoraCriacao;
		this.dataAtualizacao = dataAtualizacao;
		this.dataHoraAtualizacao = dataHoraAtualizacao;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIncidente() {
		return incidente;
	}
	public void setIncidente(long incidente) {
		this.incidente = incidente;
	}
	public Nivel getNivel() {
		return nivel;
	}
	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}
	public String getjob() {
		return job;
	}
	public void setjob(String job) {
		this.job = job;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public String getUsuario() {
		return usuario;
	}
	public void setUsuario(String usuario) {
		this.usuario = usuario;
    }
    public String getFrente() {
		return frente;
	}
	public void setFrente(String frente) {
		this.frente = frente;
	}
	public Date getDataCriacao() {
		return dataCriacao;
	}
	public void setDataCriacao(Date dataCriacao) {
		this.dataCriacao = dataCriacao;
	}
	public Calendar getDataHoraCriacao() {
		return dataHoraCriacao;
	}
	public String getDataHoraCriacaoFormatado(){
		return dataHoraCriaçãoFormatado;
	}
	public void setDataHoraCriacao(Calendar dataHoraCriacao) {
		this.dataHoraCriacao = dataHoraCriacao;
	}
	public Date getDataAtualizacao() {
		return dataAtualizacao;
	}
	public void setDataAtualizacao(Date dataAtualizacao) {
		this.dataAtualizacao = dataAtualizacao;
	}
	public Calendar getDataHoraAtualizacao() {
		return dataHoraAtualizacao;
	}
	public void setDataHoraAtualizacao(Calendar dataHoraAtualizacao) {
		this.dataHoraAtualizacao = dataHoraAtualizacao;
	}
}