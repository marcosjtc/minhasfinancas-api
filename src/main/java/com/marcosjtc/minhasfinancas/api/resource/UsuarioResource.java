package com.marcosjtc.minhasfinancas.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.marcosjtc.minhasfinancas.api.dto.UsuarioDTO;
import com.marcosjtc.minhasfinancas.exception.ErroAutenticacao;
import com.marcosjtc.minhasfinancas.exception.RegraNegocioException;
import com.marcosjtc.minhasfinancas.model.entity.Usuario;
import com.marcosjtc.minhasfinancas.service.UsuarioService;

//Retorno dos métodos serão o corpo da resposta que está enviando
//Com estas notações não precisa a tag @AuotWired. As tags abaixo fazem a injeção.
@RestController
@RequestMapping("/api/usuarios") // Todas as requisições que começãrem com "api/usuarios" entram neste controller
public class UsuarioResource {
	
	//Mapeamento para o método get para a url especificada.
	/*@GetMapping("/")
	public String helloWorld() {
		return "hello world";
	} */
	
	private UsuarioService service;
	
	//Construtor para injeção de dependência
	public UsuarioResource(UsuarioService service) {
		this.service = service;
	}
	
	//Há um problema pois já existe um @PostMapping para a raiz.
	//Está sendo tratado pelo método salvar
	//Se tentar subir outr post para raiz ocorrerá erro.
	//Tem que colocar uma URL no método para que não ocorra erro.
	//Para acessar será "api/usuarios/autenticar"
	@PostMapping("/autenticar")
	public ResponseEntity autenticar (@RequestBody UsuarioDTO dto) {
		
		try {	
			Usuario usuarioAutenticado = service.autenticar(dto.getEmail(), dto.getSenha());
			return ResponseEntity.ok(usuarioAutenticado);
		} catch (ErroAutenticacao e) {
			return ResponseEntity.badRequest().body(e.getMessage());
			
		}
		
	}
	
	//@RequestBody transforma o json em dto.
	//Tem que vir em um formato igual das propriedades do dto.
	//Qdo fizer a requisição do tipo post para ("api/usuarios")  vai para este método. 
	@PostMapping
	public ResponseEntity salvar (@RequestBody UsuarioDTO dto) {
		
		Usuario usuario = Usuario.builder()
				.nome(dto.getNome())
				.email(dto.getEmail())
				.senha(dto.getSenha()).build();
		
		try {
			Usuario usuarioSalvo = service.salvarUsuario(usuario);
			return new ResponseEntity(usuarioSalvo, HttpStatus.CREATED);
		} catch (RegraNegocioException e) {
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	

}
