package com.marcosjtc.minhasfinancas.model.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcosjtc.minhasfinancas.model.entity.Usuario;

//Interface provê os métodos padrões (CRUD)
//Se quiser pode customizar métodos no corpo da interface
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
	
	//Optional porque o usuário pode não ser encontrado por n existir
	//Se existir traz um optional com email caso contrário um optional vazio.
	//Para o springdata este é o chamado querymethod.
	//Com o querymethod não é necessário dizer qual o sql que será utilizado para consultar
	//o usuário por email na base de dados (select * from Usuario where email = email)
	//ao colocar a notação findBy + o nome da propriedade(email) que está buscando na entidade Usuario
	//dentro de UsuarioRepository. O springdata vai procurar no BD com o parâmetro que foi passado.
	//O springdata faz a geração automática do sql em tempo de execução.
	//Pode fazer concatenação findByEmailandNome(String email, String nome);
	//Optional<Usuario> findByEmail(String email);
	
	//Convenção abaixo para retornar um boolean 
	//select * from usuario where exists()
	boolean existsByEmail(String email);
	
	Optional<Usuario> findByEmail(String email);
	//Poderia retornar apenas o objeto Usuario findByEmail(String email);

}
