package br.com.ibm.cadeiabatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.ibm.cadeiabatch.entity.Categoria;
import br.com.ibm.cadeiabatch.entity.Usuario;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Long>{
	
	List<Categoria> findByUsuarios(Usuario usuario);
		
}