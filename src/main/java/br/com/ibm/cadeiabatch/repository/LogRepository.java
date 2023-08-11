package br.com.ibm.cadeiabatch.repository;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.ibm.cadeiabatch.entity.Log;
import br.com.ibm.cadeiabatch.enums.Nivel;

public interface LogRepository extends CrudRepository<Log, Long>{
	
	@Query("select l from Log l where l.dataCriacao >= ?1 and l.dataCriacao <= ?2 order by l.dataHoraCriacao desc")
	List<Log> buscarPorPeriodo(Date dataInicial, Date dataFinal);
	
	@Query("select l from Log l where l.dataCriacao <= ?1 and l.dataCriacao >= ?2 order by l.dataHoraCriacao desc")
	List<Log> logsRecentes(Date dataInicial, Date dataFinal);
	
	@Query(value="select l from Log l order by l.dataHoraCriacao desc")
	Page<Log> logsRecentes(Pageable pageable);
	
	@Query(value = "select l from Log l where l.dataHoraCriacao >= ?1 and l.dataHoraCriacao <= ?2 and l.incidente = ?3 and l.nivel = ?4 order by l.dataHoraCriacao desc")
	List<Log> buscarPorMalha(Calendar dataInicial, Calendar dataFinal, long incidente, Nivel nivel);
	
	@Query(value = "select l from Log l where l.dataHoraCriacao >= ?1 and l.dataHoraCriacao <= ?2 and l.optionalMail = false order by l.dataHoraCriacao desc")
	List<Log> buscarPorMalhaData(Calendar dataInicial, Calendar dataFim); 

	@Query(value = "select l from Log l where l.dataHoraCriacao >= ?1 and l.dataHoraCriacao <= ?2 and l.nivel <> 0 and l.optionalMail = false order by l.dataHoraCriacao desc")
	List<Log> buscarPorMalhaDataParaReport(Calendar dataInicial, Calendar dataFim);
	
	@Query(value = "select l from Log l where l.dataHoraCriacao >= ?1 and l.dataHoraCriacao <= ?2 and l.incidente = ?3 order by l.dataHoraCriacao desc")
	List<Log> buscarPorMalhaDataIncidente(Calendar dataInicial, Calendar dataFinal, long incidente);
	
	@Query(value = "select l from Log l where l.dataHoraCriacao >= ?1 and l.dataHoraCriacao <= ?2 and l.nivel = ?3 order by l.dataHoraCriacao desc")
	List<Log> buscarPorMalhaDataNivel(Calendar dataInicial, Calendar dataFinal, Nivel nivel);
	
	@Query(value = "select l from Log l where incidente = ?1 order by l.dataHoraCriacao desc")
	List<Log> buscarPorMalhaIncidente(long incidente);
	
	@Query(value = "select l from Log l where l.incidente = ?1 and l.nivel = ?2 order by l.dataHoraCriacao desc")
	List<Log> buscarPorMalhaIncidenteNivel(long incidente, Nivel nivel);
	
	@Query(value = "select l from Log l where nivel = ?1 order by l.dataHoraCriacao desc")
	List<Log> buscarPorMalhaNivel(Nivel nivel);
	
	@Query(value = "select l from Log l where l.dataHoraCriacao >= ?1 and l.dataHoraCriacao <= ?2 and l.optionalMail = true order by l.dataHoraCriacao desc")
	List<Log> buscarPorMalhaDataOptionalMail(Calendar dataInicial, Calendar dataFim);
	
}