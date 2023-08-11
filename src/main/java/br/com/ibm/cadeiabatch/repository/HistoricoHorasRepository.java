package br.com.ibm.cadeiabatch.repository;

import java.sql.Date;
import java.util.Calendar;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import br.com.ibm.cadeiabatch.entity.Chamado;
import br.com.ibm.cadeiabatch.entity.HistoricoHoras;

@Repository
public interface HistoricoHorasRepository extends PagingAndSortingRepository<HistoricoHoras, Long> {
	
	@Query(value = "SELECT h.* FROM HISTORICO_HORAS H, CHAMADO C, USUARIO U WHERE H.CHAMADO_ID = C.ID AND C.RESPONSAVEL_ID = U.ID AND H.DATA = ?1 AND U.USUARIO = ?2", nativeQuery = true)
	List<HistoricoHoras> findByDataAndUsuario(Date data, String usuario);
			
	List<HistoricoHoras> findByChamadoAndDataGreaterThanEqualAndDataLessThanEqualOrderByDataAsc(Chamado chamado, Calendar start, Calendar end);
	
	HistoricoHoras findByChamadoAndData(Chamado chamado, Calendar data);
	
	List<HistoricoHoras> findByChamadoAndDataInOrderByDataAsc(Chamado chamado, List<Calendar> dataList);
	
	HistoricoHoras findByChamadoAndDataOrderByDataAsc(Chamado chamado, Calendar data);
	
	Page<HistoricoHoras> findAllByChamadoAndDataGreaterThanEqualOrderByDataAsc(Chamado chamado, Calendar cal, Pageable pageable);
	
}
