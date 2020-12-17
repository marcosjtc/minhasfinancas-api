package com.marcosjtc.minhasfinancas.api.resource;

import java.util.List;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.marcosjtc.minhasfinancas.api.dto.AtualizaStatusDTO;
import com.marcosjtc.minhasfinancas.api.dto.LancamentoDTO;
import com.marcosjtc.minhasfinancas.exception.RegraNegocioException;
import com.marcosjtc.minhasfinancas.model.entity.Lancamento;
import com.marcosjtc.minhasfinancas.model.entity.Usuario;
import com.marcosjtc.minhasfinancas.model.enums.StatusLancamento;
import com.marcosjtc.minhasfinancas.model.enums.TipoLancamento;
import com.marcosjtc.minhasfinancas.service.LancamentoService;
import com.marcosjtc.minhasfinancas.service.UsuarioService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/lancamentos")
@RequiredArgsConstructor
public class LancamentoResource {
	
	private final LancamentoService service;
	private final UsuarioService usuarioService;
    
	//Spring injeta os dois parâmetros
	//Notation @RequiredArgsConstructor elimina o método abaixo e injeta os objetos.
	//Tem que colocar a palavra final nas variáveis acima
	//public LancamentoResource(LancamentoService service,UsuarioService usuarioService ) {
	//	this.service = service;
	//	this.usuarioService = usuarioService;
	//}
	
	//Não precisa colocar url. Ao colocar a url "/api/lancamentos",
	//o método buscar será acionado.
	@GetMapping
	public ResponseEntity buscar(
			@RequestParam(value="descricao", required=false) String descricao,
			@RequestParam(value="mes",required=false) Integer mes,
			@RequestParam(value="ano",required=false) Integer ano,
			@RequestParam("usuario") Long  idUsuario) {
		
		//Poderia passar os parâmetros assim
		//@RequestParam java.util.Map<String, String> params
		
		Lancamento lancamentoFiltro = new Lancamento();
		lancamentoFiltro.setDescricao(descricao);
		lancamentoFiltro.setMes(mes);
		lancamentoFiltro.setAno(ano);
		
       Optional<Usuario> usuario =  usuarioService.obterPorId(idUsuario);
       if (!usuario.isPresent()) {
    	   return ResponseEntity.badRequest().body("Não foi possível realizar a consulta. Usuário não encontrado para o id informado");
       }else {
    	   lancamentoFiltro.setUsuario(usuario.get());
       }
       
       List<Lancamento> lancamentos = service.buscar(lancamentoFiltro);
       return ResponseEntity.ok(lancamentos);
		
	}
	
	//Post é utilizado toda vez que vai criar um recurso no servidor
	@PostMapping
	public ResponseEntity salvar( @RequestBody LancamentoDTO dto) {
		try {
			Lancamento  entidade = converter(dto);
			entidade = service.salvar(entidade);
			return new ResponseEntity(entidade, HttpStatus.CREATED);
		} catch (RegraNegocioException e){
			return ResponseEntity.badRequest().body(e.getMessage());
		}
		
	}
	
	//Put é para atualizar um recurso que está no servidor.
	//Quando passar a url ("/api/lancamentos/id") vai ser chamado
	@PutMapping("{id}")
	public ResponseEntity atualizar( @PathVariable("id") Long id, @RequestBody LancamentoDTO dto) {
		return service.obterPorId(id).map( entity -> {
			try {
				Lancamento lancamento = converter(dto);
				lancamento.setId(entity.getId());
				service.atualizar(lancamento);
				return ResponseEntity.ok(lancamento);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(() -> new ResponseEntity("Lançamento não encontrado na base de Dados", HttpStatus.BAD_REQUEST));
		
	}
	
	//Não irá receber JSON apenas o id. Por isso não recebe DTO
	@DeleteMapping("{id}")
	public ResponseEntity deletar(@PathVariable("id") Long id) {
		return service.obterPorId(id).map( entidade -> {
		       service.deletar(entidade);
		       return new ResponseEntity(HttpStatus.NO_CONTENT);
		}).orElseGet(()-> 
		    new ResponseEntity("Lançamento não encontrado na base de Dados", HttpStatus.BAD_REQUEST));
	}
	
	@PutMapping("{id}/atualiza-status")
	public ResponseEntity atualizarStatus(@PathVariable("id") Long id, @RequestBody AtualizaStatusDTO dto) {
		
		return service.obterPorId(id).map( entity -> {
			//Método valueOf vai procurar no enum a string que for passada
			StatusLancamento statusSelecionado = StatusLancamento.valueOf(dto.getStatus());
			if(statusSelecionado == null) {
			   return ResponseEntity.badRequest().body("Não foi possível enviar o status do lançamento, envie um status válido.");	
			}
			
			try {
				entity.setStatus(statusSelecionado);
				service.atualizar(entity);
				return ResponseEntity.ok(entity);
			} catch (RegraNegocioException e) {
				return ResponseEntity.badRequest().body(e.getMessage());
			}
		}).orElseGet(()-> 
	    new ResponseEntity("Lançamento não encontrado na base de Dados", HttpStatus.BAD_REQUEST));
		
	}
	
	//Conversão de DTO em lançamento
	private Lancamento converter(LancamentoDTO dto) {
		Lancamento lancamento = new Lancamento();
		lancamento.setId(dto.getId());
		lancamento.setDescricao(dto.getDescricao());
		lancamento.setAno(dto.getAno());
		lancamento.setMes(dto.getMes());
		lancamento.setValor(dto.getValor());
		
		Usuario usuario = usuarioService
		.obterPorId(dto.getUsuario())
		.orElseThrow(() -> new RegraNegocioException("Usuário não encontrado para o id informado"));
		
		lancamento.setUsuario(usuario);
		if (dto.getTipo() != null) {
			lancamento.setTipo(TipoLancamento.valueOf(dto.getTipo()));
		}
		if (dto.getStatus() != null) {
			lancamento.setStatus(StatusLancamento.valueOf(dto.getStatus()));
		}
		
		
		return lancamento;
	}
	
	

}
