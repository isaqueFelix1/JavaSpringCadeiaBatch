package br.com.ibm.cadeiabatch.entity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.ibm.cadeiabatch.enums.Nivel;
import br.com.ibm.cadeiabatch.enums.TipoChamadoEnum;

@Entity
public class Log {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	private long incidente;
	
	@Enumerated(EnumType.ORDINAL)
	private Nivel nivel;

	private String job;
	
	private String descricao;
	
	@Enumerated(EnumType.ORDINAL)
	private TipoChamadoEnum tipo;
	
	@ManyToOne
	private Usuario usuario;
	
	private boolean optionalMail;
	
	@Temporal(TemporalType.DATE)
	private Date dataCriacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataHoraCriacao;
	
	@Temporal(TemporalType.DATE)
	private Date dataAtualizacao;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar dataHoraAtualizacao;
	
	public Log() {
		super();
	}
	public Log(long id, long incidente, Nivel nivel, String descricao, Usuario usuario, Date dataCriacao,
			Calendar dataHoraCriacao, Date dataAtualizacao, Calendar dataHoraAtualizacao, boolean optionalMail) {
		super();
		this.id = id;
		this.incidente = incidente;
		this.nivel = nivel;
		this.descricao = descricao;
		this.usuario = usuario;
		this.dataCriacao = dataCriacao;
		this.dataHoraCriacao = dataHoraCriacao;
		this.dataAtualizacao = dataAtualizacao;
		this.dataHoraAtualizacao = dataHoraAtualizacao;
		this.optionalMail = optionalMail;
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
	public String getJob() {
		return job;
	}
	public void setJob(String job) {
		this.job = job;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
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
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		return format.format(dataHoraCriacao.getTime());
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
	public TipoChamadoEnum getTipo() {
		return tipo;
	}
	public void setTipo(TipoChamadoEnum tipo) {
		this.tipo = tipo;
	}
	public boolean isOptionalMail() {
		return optionalMail;
	}
	public void setOptionalMail(boolean optionalMail) {
		this.optionalMail = optionalMail;
	}
	
	public String getDescricaoFormat() {
		String descricaoFormat = null;		
		descricaoFormat = this.descricao.replace("#", "</br>");
		return descricaoFormat;
	}
}