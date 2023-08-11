package br.com.ibm.cadeiabatch.dao;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.ibm.cadeiabatch.entity.ExtratoHoras;

@ManagedBean
public class ExtratoHorasDao {
	
	@Autowired
	private EntityManager em;
	
	public List<ExtratoHoras> extrairPorPeriodoBanco(Date dataInicial, Date dataFinal, Integer empresa, boolean consolidado){
		List<ExtratoHoras> extracao = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		
		if(consolidado) {
			sql.append("select c.nome, b.data, sum(b.horas) ");
			sql.append(" from chamado a, historico_horas b, usuario c");
			sql.append(" where a.id = b.chamado_id");
			sql.append(" and a.responsavel_id = c.id");
			sql.append(" and b.data >= ? and b.data<= ?");
		}else {
			sql.append("select a.numero, a.descricao, a.tipo, a.categoria, b.data, month(b.data), b.horas, c.nome, e.nome_empresa ");
			sql.append(" from chamado a, historico_horas b, usuario c, usuario_empresas d, empresa e");
			sql.append(" where a.id = b.chamado_id");
			sql.append(" and a.responsavel_id = c.id");
			sql.append(" and b.data >= ? and b.data<= ?");
			sql.append(" and c.id = d.usuario_id");
			sql.append(" and d.empresas_id = e.id");
			if(empresa != 2)
				sql.append(" and d.empresas_id = ?");
		}
				
		if(consolidado) {
			sql.append(" group by c.nome, b.data");
			sql.append(" order by c.nome, b.data");
		}else {
			sql.append(" order by a.numero, b.data, c.nome, e.nome_empresa");
		}
		
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter(1, dataInicial);
		query.setParameter(2, dataFinal);
		if(empresa != 2)
			query.setParameter(3, empresa);
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		for (int i = 0; i < results.size(); i++){
			int ind = 0;
			ExtratoHoras extratoHoras = new ExtratoHoras();
			Object[] obj = results.get(i);
			
			if(consolidado) {
				extratoHoras.setNome(((String)obj[ind++]));
				extratoHoras.setData(((Date)obj[ind++]));
				extratoHoras.setHoras(((BigDecimal)obj[ind++]));
			}else {
			
				extratoHoras.setNumero(((BigInteger)obj[ind++]));
				extratoHoras.setDescricao(((String)obj[ind++]));
				extratoHoras.setTipo(((Integer)obj[ind++]));
				extratoHoras.setCategoria(((Integer)obj[ind++]));
				extratoHoras.setData(((Date)obj[ind++]));
				extratoHoras.setMes(((Integer)obj[ind++]));
				extratoHoras.setHoras(((BigDecimal)obj[ind++]));
				extratoHoras.setNome(((String)obj[ind++]));
				extratoHoras.setNome_empresa(((String)obj[ind++]));
			}
			extracao.add(extratoHoras);
		}
		
		return extracao;
	}
	
	public List<ExtratoHoras> extrairPorPeriodoAndCategoriaBanco(Date dataInicial, Date dataFinal, Integer categoria){
		List<ExtratoHoras> extracao = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.numero, a.descricao, a.tipo, a.categoria, b.data, month(b.data), b.horas, c.nome ");
		sql.append(" from chamado a, historico_horas b, usuario c");
		sql.append(" where a.id = b.chamado_id");
		sql.append(" and a.responsavel_id = c.id");
		sql.append(" and b.data >= ? and b.data<= ?");
		sql.append(" and a.categoria = ?");
		sql.append(" order by a.numero, b.data, c.nome");
		
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter(1, dataInicial);
		query.setParameter(2, dataFinal);					
		query.setParameter(3, categoria);
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		for (int i = 0; i < results.size(); i++){
			int ind = 0;
			ExtratoHoras extratoHoras = new ExtratoHoras();
			Object[] obj = results.get(i);
			
			extratoHoras.setNumero(((BigInteger)obj[ind++]));
			extratoHoras.setDescricao(((String)obj[ind++]));
			extratoHoras.setTipo(((Integer)obj[ind++]));
			extratoHoras.setCategoria(((Integer)obj[ind++]));
			extratoHoras.setData(((Date)obj[ind++]));
			extratoHoras.setMes(((Integer)obj[ind++]));
			extratoHoras.setHoras(((BigDecimal)obj[ind++]));
			extratoHoras.setNome(((String)obj[ind++]));
			
			extracao.add(extratoHoras);
		}
		
		return extracao;
	}
	
	public List<ExtratoHoras> extrairPorPeriodoAndConsultorBanco(Date dataInicial, Date dataFinal, String consultor){
		List<ExtratoHoras> extracao = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.numero, a.descricao, a.tipo, a.categoria, b.data, month(b.data), b.horas, c.nome ");
		sql.append(" from chamado a, historico_horas b, usuario c");
		sql.append(" where a.id = b.chamado_id");
		sql.append(" and a.responsavel_id = c.id");
		sql.append(" and b.data >= ? and b.data<= ?");
		sql.append(" and upper(c.usuario) like upper(\'%" + consultor + "%\') ");
		sql.append(" order by a.numero, b.data, c.nome");
		
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter(1, dataInicial);
		query.setParameter(2, dataFinal);							
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		for (int i = 0; i < results.size(); i++){
			int ind = 0;
			ExtratoHoras extratoHoras = new ExtratoHoras();
			Object[] obj = results.get(i);
			
			extratoHoras.setNumero(((BigInteger)obj[ind++]));
			extratoHoras.setDescricao(((String)obj[ind++]));
			extratoHoras.setTipo(((Integer)obj[ind++]));
			extratoHoras.setCategoria(((Integer)obj[ind++]));
			extratoHoras.setData(((Date)obj[ind++]));
			extratoHoras.setMes(((Integer)obj[ind++]));
			extratoHoras.setHoras(((BigDecimal)obj[ind++]));
			extratoHoras.setNome(((String)obj[ind++]));
			
			extracao.add(extratoHoras);
		}
		
		return extracao;
	}

	public List<ExtratoHoras> extrairPorPeriodoAndConsultorAndCategoriaBanco(Date dataInicial, Date dataFinal, String consultor,
			Integer categoria) {
		List<ExtratoHoras> extracao = new ArrayList<>();
		StringBuffer sql = new StringBuffer();
		
		sql.append("select a.numero, a.descricao, a.tipo, a.categoria, b.data, month(b.data), b.horas, c.nome ");
		sql.append(" from chamado a, historico_horas b, usuario c");
		sql.append(" where a.id = b.chamado_id");
		sql.append(" and a.responsavel_id = c.id");
		sql.append(" and b.data >= ? and b.data<= ?");
		sql.append(" and upper(c.nome) like upper(\'%" + consultor + "%\') ");
		sql.append(" and a.categoria = ? ");
		sql.append(" order by a.numero, b.data, c.nome");
		
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter(1, dataInicial);
		query.setParameter(2, dataFinal);									
		query.setParameter(3, categoria);
		
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		for (int i = 0; i < results.size(); i++){
			int ind = 0;
			ExtratoHoras extratoHoras = new ExtratoHoras();
			Object[] obj = results.get(i);
			
			extratoHoras.setNumero(((BigInteger)obj[ind++]));
			extratoHoras.setDescricao(((String)obj[ind++]));
			extratoHoras.setTipo(((Integer)obj[ind++]));
			extratoHoras.setCategoria(((Integer)obj[ind++]));
			extratoHoras.setData(((Date)obj[ind++]));
			extratoHoras.setMes(((Integer)obj[ind++]));
			extratoHoras.setHoras(((BigDecimal)obj[ind++]));
			extratoHoras.setNome(((String)obj[ind++]));
			
			extracao.add(extratoHoras);
		}
		
		return extracao;

	}
}
