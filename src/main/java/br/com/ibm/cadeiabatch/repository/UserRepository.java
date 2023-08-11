package br.com.ibm.cadeiabatch.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.ibm.cadeiabatch.entity.Usuario;

public interface UserRepository extends JpaRepository<Usuario, Long>{
	
	@Query("select u from Usuario u where u.usuario = ?1")
	Usuario buscarPorNome(String nome);
	
	@Query(value = "select u.* from usuario u\r\n" + 
			"   where u.usuario not in (\r\n" + 
			"	select c.usuario\r\n" + 
			"	from chamado a, historico_horas b, usuario c\r\n" + 
			"	where a.id = b.chamado_id\r\n" + 
			"	and a.responsavel_id = c.id\r\n" + 
			"	and b.data >= ?1\r\n" + 
			"	and b.data <= ?2)"    +
			"   and u.nivel < 2 ", nativeQuery = true)
    List<Usuario> listarUsuariosPendentes(String startDate, String finalDate);
	
}