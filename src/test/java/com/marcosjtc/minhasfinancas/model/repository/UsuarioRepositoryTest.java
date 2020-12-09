package com.marcosjtc.minhasfinancas.model.repository;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.marcosjtc.minhasfinancas.model.entity.Usuario;

//Teste de integração precisa de recursos externos a aplicação. 
//Neste caso está acessando o BD
//@SpringBootTest
@DataJpaTest //Cria uma instância do BD da memória e ao finalizar apaga da memória. 
//Ao concluir o teste executa rollback
@AutoConfigureTestDatabase(replace = Replace.NONE)
@RunWith(SpringRunner.class)
//Procura o application-test para carregar as configurações.
@ActiveProfiles("test")
public class UsuarioRepositoryTest {
	
	@Autowired
	UsuarioRepository repository;
	
	//EntityManager configurado apenas para teste
	@Autowired 
	TestEntityManager entityManager;
	
	@Test
	public void deveVerificarAExistenciaDeUmEmail() {
		//Três elementos para o teste de integração
		//1 - Cenário
		//Usuario usuario = Usuario.builder().nome("Marcos").email("marcos@email.com").build();
		Usuario usuario = criarUsuario();
		//repository.save(usuario);
		entityManager.persist(usuario);
		
		//2 - ação/execução
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//3 - verificação
		Assertions.assertThat(result).isTrue();
		
	}
	
	@Test
	public void deveRetornarFalsoQuandoNaoHouverUsuarioCadastradoComOEmail() {
		//cenário . n precisa mais por causa do rollback
		//repository.deleteAll();
		
		//Ação
		boolean result = repository.existsByEmail("usuario@email.com");
		
		//Verificação
		Assertions.assertThat(result).isFalse();
		
	}

	@Test
	public void devePersistirUmUsuarioNaBaseDeDados() {
		//cenário
		Usuario usuario = criarUsuario();
		
		//ação
		Usuario usuarioSalvo = repository.save(usuario);
		
		//verificacao
		Assertions.assertThat(usuario.getId()).isNotNull();
		
	}
	
	@Test
	public void deveBuscarUmUsuarioPorEmail() {
		
		//cenário
				Usuario usuario = criarUsuario();
				entityManager.persist(usuario); //classe n pode ter id, se n lança exceção.
				
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");	
		Assertions.assertThat(result.isPresent()).isTrue();
		
	}
	
	@Test
	public void deveRetornarVazioAoBuscarUsuarioPorEmailQuandoNaoExisteNaBase() {
		
				
		//verificacao
		Optional<Usuario> result = repository.findByEmail("usuario@email.com");	
		Assertions.assertThat(result.isPresent()).isFalse();
		
	}
	
	public static Usuario criarUsuario() {
		return  Usuario.
				builder().
				nome("usuario").
				email("usuario@email.com").
				senha("senha").
				build();
	}
	

}
