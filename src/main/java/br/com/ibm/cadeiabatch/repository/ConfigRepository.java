package br.com.ibm.cadeiabatch.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import br.com.ibm.cadeiabatch.entity.Config;



public interface ConfigRepository extends CrudRepository<Config, Long>{
	
	@Query("select c from Config c where c.nomeConfig = ?1")
	Config buscarPorNomeConfig(String nome);
}