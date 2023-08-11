package br.com.ibm.cadeiabatch.entity;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import br.com.ibm.cadeiabatch.enums.CategoriaEnum;
import br.com.ibm.cadeiabatch.enums.TipoChamadoEnum;

@Entity
public class Chamado {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	
	private Long numero;
	
	private String descricao;
	
	private boolean fechado;
	
	@ManyToOne
	private Usuario responsavel;
	
	@Enumerated(EnumType.ORDINAL)
	private TipoChamadoEnum tipo;
	
	@Enumerated(EnumType.ORDINAL)
	private CategoriaEnum categoria;
	
	@OneToMany(mappedBy="chamado", fetch=FetchType.LAZY,cascade=CascadeType.ALL)
	private List<HistoricoHoras> historico;
	
	private BigDecimal total;

	public Chamado() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getNumero() {
		return numero;
	}

	public void setNumero(Long numero) {
		this.numero = numero;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public boolean isFechado() {
		return fechado;
	}

	public void setFechado(boolean fechado) {
		this.fechado = fechado;
	}

	public Usuario getResponsavel() {
		return responsavel;
	}

	public void setResponsavel(Usuario responsavel) {
		this.responsavel = responsavel;
	}

	public TipoChamadoEnum getTipo() {
		return tipo;
	}

	public void setTipo(TipoChamadoEnum tipo) {
		this.tipo = tipo;
	}

	public CategoriaEnum getCategoria() {
		return categoria;
	}

	public void setCategoria(CategoriaEnum categoria) {
		this.categoria = categoria;
	}

	public List<HistoricoHoras> getHistorico() {
		return historico;
	}

	public void setHistorico(List<HistoricoHoras> historico) {
		this.historico = historico;
	}

	public BigDecimal getTotal() {
		return total;
	}
	
	public void calculaTotal() {
		total = new BigDecimal(0);
		
		for (HistoricoHoras historicoHoras : historico) {
			this.total =  this.total.add(historicoHoras.getHoras()).setScale(1,RoundingMode.HALF_DOWN);
		}
	}
	
}
