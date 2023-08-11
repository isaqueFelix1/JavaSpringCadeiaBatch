package br.com.ibm.cadeiabatch.dao;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.ManagedBean;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.ibm.cadeiabatch.entity.Log;
import br.com.ibm.cadeiabatch.entity.PontoGrafico;
import br.com.ibm.cadeiabatch.enums.Nivel;
import br.com.ibm.cadeiabatch.repository.LogRepository;

@ManagedBean
public class LogDao {

	@Autowired
	private LogRepository repository;

	@Autowired
	private EntityManager em;

	private final String SELECT_MONTHLY_GRAFIC = "select lpad(month(data_criacao),2,'0'), substr(year(data_criacao),3) " 
	+ ", sum(case when nivel = 0 then 1 else 0 end) as info"
	+ ",sum(case when nivel = 1 then 1 else 0 end) as warn" 
	+ ", sum(case when nivel = 2 then 1 else 0 end) as erro"
	+ ", count(1) as total from log";
	private final String GROUP_MONTHLY_GRAFIC = " group by lpad(month(data_criacao),2,'0'), substr(year(data_criacao),3)  " 
	+ "order by substr(year(data_criacao),3)  asc, lpad(month(data_criacao),2,'0') asc";

	private final String SELECT_DAILY_GRAFIC = "select lpad(day(data_criacao),2,'0'), lpad(month(data_criacao),2,'0'), substr(year(data_criacao),3)" 
	+ ", sum(case when nivel = 0 then 1 else 0 end) as info"
	+ ",sum(case when nivel = 1 then 1 else 0 end) as warn" 
	+ ", sum(case when nivel = 2 then 1 else 0 end) as erro"
	+ ", count(1) as total from log";
	private final String GROUP_DAILY_GRAFIC = " group by lpad(day(data_criacao),2,'0'), lpad(month(data_criacao),2,'0'), substr(year(data_criacao),3)  " 
	+ "order by substr(year(data_criacao),3)  asc, lpad(month(data_criacao),2,'0') asc, lpad(day(data_criacao),2,'0') asc ";

	public void incluir(Log log) {
		repository.save(log);
	}

	public List<Log> pesquisarPorPeriodoBanco(Date dataInicial, Date dataFinal) {
		return repository.buscarPorPeriodo(dataInicial, dataFinal);
	}

	public List<Log> pesquisarPorMalha(Calendar dataInicio, Calendar dataFim, long incidente, Nivel nivel) {
		return repository.buscarPorMalha(dataInicio, dataFim, incidente, nivel);
	}
	
	public List<Log> pesquisarPorMalhaData(Calendar dataInicio, Calendar dataFim) {
		return repository.buscarPorMalhaData(dataInicio, dataFim);
	}
	
	public List<Log> pesquisarPorMalhaDataIncidente(Calendar dataInicio, Calendar dataFim, long incidente) {
		return repository.buscarPorMalhaDataIncidente(dataInicio, dataFim, incidente);
	}
	
	public List<Log> pesquisarPorMalhaDataNivel(Calendar dataInicio, Calendar dataFim, Nivel nivel) {
		return repository.buscarPorMalhaDataNivel(dataInicio, dataFim, nivel);
	}
	
	public List<Log> pesquisarPorMalhaIncidente(long incidente) {
		return repository.buscarPorMalhaIncidente(incidente);
	}
	
	public List<Log> pesquisarPorMalhaIncidenteNivel(long incidente, Nivel nivel) {
		return repository.buscarPorMalhaIncidenteNivel(incidente, nivel);
	}
	
	public List<Log> pesquisarPorMalhaNivel(Nivel nivel) {
		return repository.buscarPorMalhaNivel(nivel);
	}	

	public List<Log> logsRecentesBanco(Date dataInicial, Date dataFinal) {
		return repository.logsRecentes(dataInicial, dataFinal);
	}
	public Page<Log> logsRecentesBanco(int rowcount) {
		Pageable page = PageRequest.of(0, rowcount);
		return repository.logsRecentes(page);
	}
	public List<PontoGrafico> buscarDadosGrafico(Date dataInicial, Date dataFinal, int nivel, String job, String tipo){
		List<PontoGrafico> retList = new ArrayList<PontoGrafico>();
		StringBuffer sql = new StringBuffer();
		if (tipo.equals("diario")){
			sql.append(SELECT_DAILY_GRAFIC);
		} else {
			sql.append(SELECT_MONTHLY_GRAFIC);
		}
		
		sql.append(" where data_criacao >= ? and data_criacao <= ? ");
		if (nivel != 3){
			sql.append(" and nivel = ? ");
		} 
		if (job != null && !job.isEmpty()){
			sql.append(" and job = ? ");
		}
		if (tipo.equals("diario")){
			sql.append(GROUP_DAILY_GRAFIC);
		} else {
			sql.append(GROUP_MONTHLY_GRAFIC);
		}
		Query query = em.createNativeQuery(sql.toString());
		query.setParameter(1, dataInicial);
		query.setParameter(2, dataFinal);
		int param = 3;
		if (nivel != 3){
			query.setParameter(param++, nivel);
		}
		if (job != null && !job.isEmpty()){
			query.setParameter(param++, job);
		}
		@SuppressWarnings("unchecked")
		List<Object[]> results = query.getResultList();
		for (int i = 0; i < results.size(); i++){
			Object[] teste = results.get(i);
			PontoGrafico graph = new PontoGrafico();
			int ind = 0;
			String day = null;
			if (tipo.equals("diario")){
				day = (String)teste[ind++];
			}
			String month = (String)teste[ind++];
			String year = (String)teste[ind++];
			String data = day == null ? month.toString() + "/" + year.toString() : day.toString() + "/" + month.toString() + "/" + year.toString();
			graph.setData(data);
			graph.setInfo(((BigInteger)teste[ind++]).intValue());
			graph.setWarn(((BigInteger)teste[ind++]).intValue());
			graph.setErro(((BigInteger)teste[ind++]).intValue());
			graph.setTotal(((BigInteger)teste[ind++]).intValue());
			retList.add(graph);
		}
		return retList;
	} 

}
