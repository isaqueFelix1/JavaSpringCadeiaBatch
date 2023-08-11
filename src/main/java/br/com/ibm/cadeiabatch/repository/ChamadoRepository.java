package br.com.ibm.cadeiabatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ibm.cadeiabatch.entity.Chamado;
import br.com.ibm.cadeiabatch.entity.Usuario;

@Repository
public interface ChamadoRepository extends JpaRepository<Chamado, Long>{
	
	List<Chamado> findByResponsavelAndFechadoIsFalseOrderByIdAsc(Usuario usuario);
	
	List<Chamado> findByResponsavelAndFechadoIsFalse(Usuario usuario);
	
	Chamado findByNumeroAndResponsavel(Long numero, Usuario usuario);
	
	Chamado findByIdAndResponsavel(Long id, Usuario usuario);
	
}
