package com.marcosjtc.minhasfinancas.service;

import java.util.Optional;

import com.marcosjtc.minhasfinancas.model.entity.Usuario;

//Definição dos métodos para trabalhar com a entidade usuário.
public interface UsuarioService {
	//Ao receber o email vai no BD buscar o usuário.
	Usuario autenticar(String email, String senha);
	
	//Salar um usuário que não foi salvo no BD.
	Usuario salvarUsuario(Usuario usuario);
	
	//Email só pode ser cadastrado uma vez.
	void validarEmail(String email);
	
	//Obtém usuário por id
	Optional<Usuario> obterPorId(Long id);

}
