package com.marcosjtc.minhasfinancas.service.impl;


import java.util.Optional;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.marcosjtc.minhasfinancas.exception.ErroAutenticacao;
import com.marcosjtc.minhasfinancas.exception.RegraNegocioException;
import com.marcosjtc.minhasfinancas.model.entity.Usuario;
import com.marcosjtc.minhasfinancas.model.repository.UsuarioRepository;
import com.marcosjtc.minhasfinancas.service.UsuarioService;

//Notação para que o conteiner do spring gerencie esta classe.
//Framework vai criar um instância e vai incluir em uma conteiner
//Quando necessitar será injetado em outras classes
@Service 

//Assistente do eclipse gera a assinatura dos métodos 
public class UsuarioServiceImpl implements UsuarioService {
	
	
	//Camada de serviço vai acessar a camada de modelo via repository para acessar o BD.
	//Service não pode acessar diretamente o BD. (Conforme arquitetura).
	
	private UsuarioRepository repository;

	//para o UsuarioServiceImpl funcionar terá que ser instanciado com instância de UsuarioRepository
	//@Autowired Injeta dependência para a classe UsuarioRepository.
	//Vai criar uma instância e vai colocar no conteiner.
	//Pode ser colocada aqui no construtor, pode ser no atributo UsuarioRepository ou em um método setRepository
	//@Autowired
	
	public UsuarioServiceImpl(UsuarioRepository repository) {
		super();
		this.repository = repository;
	}
    
	public Usuario autenticar(String email, String senha) {
		Optional<Usuario> usuario = repository.findByEmail(email);
		
		if(!usuario.isPresent()) {
			throw new ErroAutenticacao("Usuario não encontrado para o email informado");
		}
		
		if (! usuario.get().getSenha().equals(senha)) {
			throw new ErroAutenticacao("Senha inválida");
		}
		
		return usuario.get();
	}

	//Abre uma transação na base de dados
	//Executa o método do usuário e depois que salvar vai fazer commit
	@Transactional 
	public Usuario salvarUsuario(Usuario usuario) {
		validarEmail(usuario.getEmail());
		return repository.save(usuario);
	}

	public void validarEmail(String email) {
		// TODO Auto-generated method stub
		boolean existe = repository.existsByEmail(email);
		if (existe) {
          throw new RegraNegocioException("Já existe um usuário cadastrado com este email.");		
        }
	}

	@Override
	public Optional<Usuario> obterPorId(Long id) {
		// TODO Auto-generated method stub
		return repository.findById(id);
	}

}
