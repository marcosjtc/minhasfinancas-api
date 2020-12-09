package com.marcosjtc.minhasfinancas.service;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.marcosjtc.minhasfinancas.exception.ErroAutenticacao;
import com.marcosjtc.minhasfinancas.exception.RegraNegocioException;
import com.marcosjtc.minhasfinancas.model.entity.Usuario;
import com.marcosjtc.minhasfinancas.model.repository.UsuarioRepository;
import com.marcosjtc.minhasfinancas.service.impl.UsuarioServiceImpl;

//@SpringBootTest depois do mock
@RunWith(SpringRunner.class) //Cria contexto de injeção de dependência.
@ActiveProfiles("test")
public class UsuarioServiceTest {
	
	//Coloca a interface. Todas as vezes que coloca a interface o springboot
	//vai no conteiner de injeção de dependência procurar a implementação.
	//caso encontre vai injetar aqui.
	//@Autowired 
	@SpyBean
	UsuarioServiceImpl service;
	
	//@Autowired
	@MockBean //Cria um mock que é um bean gerenciável. Cria instâncias fakes de usuário repostiory
	UsuarioRepository repository;
	
	@Test(expected = Test.None.class) //Para n lançar erro.
	public void deveSalvarUmUsuario() {
		//cenário. Não vai lançar nenhum erro.
		Mockito.doNothing().when(service).validarEmail(Mockito.anyString());
		Usuario usuario = Usuario.builder().
				id(1l).
				nome("nome").
				email("email@email.com").
				senha("senha").
				build();
		
		Mockito.when(repository.save(Mockito.any(Usuario.class))).thenReturn(usuario);
		
		//Ação
		Usuario usuarioSalvo = service.salvarUsuario(new Usuario());
		
		//Verificação
		Assertions.assertThat(usuarioSalvo).isNotNull();
		Assertions.assertThat(usuarioSalvo.getId()).isEqualTo(1l);
		Assertions.assertThat(usuarioSalvo.getEmail()).isEqualTo("email@email.com");
		Assertions.assertThat(usuarioSalvo.getNome()).isEqualTo("nome");
		Assertions.assertThat(usuarioSalvo.getSenha()).isEqualTo("senha");
		
		
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void naoDeveSalvarUmUsuarioComEmailjaCadastrado() {
		//Cenário
		String email = "email@email.com";
		Usuario usuario = Usuario.builder().email(email).build();
		Mockito.doThrow(RegraNegocioException.class).when(service).validarEmail(email);
		
		//ação
		service.salvarUsuario(usuario);
		
		//Verificação (Não é para chamar o método de salvar)
		Mockito.verify(repository,Mockito.never()).save(usuario);
		
	}
	
	//@Before //Será executado antes da execução de todos os testes.
	//Pode retirar as injeções (@Autowired). Serão testes unitários.
/*	public void setUp() {
		
		//O spy é um mock que chama os métodos originais, ao contrário do mock que chama os métodos fakes.
		service = Mockito.spy(UsuarioServiceImpl.class);
		
		
		//Saiu depois de @MockBean
		//repository = Mockito.mock(UsuarioRepository.class);
		//service = new UsuarioServiceImpl(repository); retirado para testar com spy
		
		
	} método retirado para testar spy de uma forma padrão*/ 
	
	@Test(expected = Test.None.class)
	public void deveAutenticarUmUsuarioComSucesso() {
		//Cenário
		String email = "email@email.com";
		String senha = "senha";
		
		
		Usuario usuario = Usuario.builder().email(email).senha(senha).id(1l).build();
		Mockito.when(repository.findByEmail(email)).thenReturn(Optional.of(usuario));
		
		//Ação
		Usuario result = service.autenticar(email, senha);
		
		//Verificação
		Assertions.assertThat(result).isNotNull();
	}
	
	@Test
	public void deveLancarErroQUandoNaoEncontrarUsuarioCadastradoComOEmailInformado() {
		//Cenário
		//Optional<Usuario> usuario = Optional.empty();
		
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.empty());
		
		//ação
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "senha") );
		
		//verificação
		Assertions.assertThat(exception).
		isInstanceOf(ErroAutenticacao.class).
		hasMessage("Usuario não encontrado para o email informado");
		
		
		//service.autenticar("email@email.com", "senha");
		
		
	}
	
	//@Test(expected = ErroAutenticacao.class) Quando coloca o Junit vem azul e traz expected erro!
	@Test
	public void deveLancarErroQuandoSenhaNaoBater() {
		//Cenário
		String senha = "senha";
		Usuario usuario = Usuario.builder().email("email@email.com").senha(senha).build();
		Mockito.when(repository.findByEmail(Mockito.anyString())).thenReturn(Optional.of(usuario));
		
		//ação
		//service.autenticar("email@email.com", "123");
		
		Throwable exception = Assertions.catchThrowable(() -> service.autenticar("email@email.com", "123") );
		Assertions.assertThat(exception).isInstanceOf(ErroAutenticacao.class).hasMessage("Senha inválida");
		
	}
	
	//Classe faz o teste esperar que nenhuma exceção seja lançada
	@Test(expected = Test.None.class)
	public void deveValidarEmail() {
		//cenário
		
		//Serve para testar. Cria uma instância fake de classes.
		//UsuarioRepository usuarioRepositoryMock = Mockito.mock(UsuarioRepository.class);
		
		//repository.deleteAll(); saiu depois do mock
		
		//Como retorna falso, não irá lançar exceção.
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(false);
		
		//ação
		service.validarEmail("marcosjtc@bol.com.br");
		
		
	}
	
	@Test(expected = RegraNegocioException.class)
	public void deveLancarErroAoValidarEmailQuandoExistirEmailCadastrado() {
		//cenário. saiu depois do mock
		//Usuario usuario = Usuario.builder().nome("usuario").email("email@email.com").build();
		Mockito.when(repository.existsByEmail(Mockito.anyString())).thenReturn(true);
		
		
		//ação
		service.validarEmail("email@email.com");
		
	}

}
