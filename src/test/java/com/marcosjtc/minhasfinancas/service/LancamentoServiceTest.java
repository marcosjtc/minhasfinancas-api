package com.marcosjtc.minhasfinancas.service;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.domain.Example;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.marcosjtc.minhasfinancas.exception.RegraNegocioException;
import com.marcosjtc.minhasfinancas.model.entity.Lancamento;
import com.marcosjtc.minhasfinancas.model.entity.Usuario;
import com.marcosjtc.minhasfinancas.model.enums.StatusLancamento;
import com.marcosjtc.minhasfinancas.model.repository.LancamentoRepository;
import com.marcosjtc.minhasfinancas.model.repository.LancamentoRepositoryTest;
import com.marcosjtc.minhasfinancas.service.impl.LancamentoServiceImpl;

@RunWith(SpringRunner.class)
@ActiveProfiles("Test")
public class LancamentoServiceTest {
	
	@SpyBean
	LancamentoServiceImpl service;
	
	@MockBean
	LancamentoRepository repository;
	
	@Test
	public void deveSalvarUmLancamento() {
		//Cenários
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		//Não vai gerar erro quando chamar o método
		Mockito.doNothing().when(service).validar(lancamentoASalvar);
		
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		Mockito.when(repository.save(lancamentoASalvar)).thenReturn(lancamentoSalvo);
		
		//execucao
		Lancamento lancamento = service.salvar(lancamentoASalvar);
		
		//Verificacao
		Assertions.assertThat(lancamento.getId()).isEqualTo(lancamentoSalvo.getId());
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(StatusLancamento.PENDENTE);
		
	}
	
	@Test
	public void naoDeveSalvarUmLancamentoQuandoHouverErroDeValidacao() {
		//Cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
		Mockito.doThrow(RegraNegocioException.class).when(service).validar(lancamentoASalvar);
		
		//Execução e verificação
		Assertions.catchThrowableOfType(()-> service.salvar(lancamentoASalvar), RegraNegocioException.class);
		//Quando chama o método salvar de lancamento vai gerar a exception,
		//mas não vai chamar repository.save(lancamento)
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
		
	}
	
	@Test
	public void deveAtualizarUmLancamento() {
		//Cenários
		Lancamento lancamentoSalvo = LancamentoRepositoryTest.criarLancamento();
		lancamentoSalvo.setId(1l);
		lancamentoSalvo.setStatus(StatusLancamento.PENDENTE);
		
		//Não vai gerar erro quando chamar o método
		Mockito.doNothing().when(service).validar(lancamentoSalvo);
		
		
		Mockito.when(repository.save(lancamentoSalvo)).thenReturn(lancamentoSalvo);
		
		//execucao
		service.atualizar(lancamentoSalvo);
		
		//Verificacao
		Mockito.verify(repository, Mockito.times(1)).save(lancamentoSalvo);
		
		
	}
	
	@Test
	public void deveLancarErroAoTentarAtualizarUmLancamentoQueAindaNaoFoiSalvo() {
		//Cenário
		Lancamento lancamentoASalvar = LancamentoRepositoryTest.criarLancamento();
				
		//Execução e verificação
		Assertions.catchThrowableOfType(()-> service.atualizar(lancamentoASalvar), NullPointerException.class);
		//Quando chama o método salvar de lancamento vai gerar a exception,
		//mas não vai chamar repository.save(lancamento)
		Mockito.verify(repository, Mockito.never()).save(lancamentoASalvar);
		
	}
	
	@Test
	public void deveDeletarUmLancamento() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		//execucao
		service.deletar(lancamento);
		
		//verificacao
		Mockito.verify(repository).delete(lancamento);
		
	}
	
	@Test
	public void deveLancarErroAoTentarDeletarUmLancamentoQueAindaNaoFoiSalvo() {
		
		        //cenário
				Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
				
				
				//execucao
				Assertions.catchThrowableOfType(()-> service.deletar(lancamento), NullPointerException.class);
				
				//verificacao
				Mockito.verify(repository,Mockito.never()).delete(lancamento);
		
	}
	
	@Test
	public void deveFiltrarLancamentos() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		List<Lancamento> lista = Arrays.asList(lancamento); 
		Mockito.when(repository.findAll(Mockito.any(Example.class))).thenReturn(lista);
		
		//Execucao
		List<Lancamento> resultado = service.buscar(lancamento);
		
		//verificacoes
		Assertions
		.assertThat(resultado)
		.isNotEmpty()
		.hasSize(1)
		.contains(lancamento);
		
	}
	
	@Test
	public void deveAtualizarOStatusDeUmLancamento() {
		//cenário
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		lancamento.setStatus(StatusLancamento.PENDENTE);
		
		StatusLancamento novoStatus = StatusLancamento.EFETIVADO;
		Mockito.doReturn(lancamento).when(service).atualizar(lancamento);
		
		//execucao
		service.atualizarStatus(lancamento, novoStatus);
		
		//verificacoes
		Assertions.assertThat(lancamento.getStatus()).isEqualTo(novoStatus);
		Mockito.verify(service).atualizar(lancamento);
		
	}
	
	@Test
	public void deveObterUmLancamentoPorID() {
		//cenário
		Long id = 1L;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.of(lancamento));
		
		//execucao
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		//verificacao
		Assertions.assertThat(resultado.isPresent()).isTrue();
		
	}
	
	@Test
	public void deveRetornarVazioQuandoOLancamentoNaoExiste() {
		//cenário
		Long id = 1L;
		
		Lancamento lancamento = LancamentoRepositoryTest.criarLancamento();
		lancamento.setId(1l);
		
		Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
		
		//execucao
		Optional<Lancamento> resultado = service.obterPorId(id);
		
		//verificacao
		Assertions.assertThat(resultado.isPresent()).isFalse();
		
	}
	
	@Test
	public void deveLancarErrosAoValidarUmLancamento() {
		Lancamento lancamento = new Lancamento();
		
		Throwable erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida");
		
		lancamento.setDescricao("");
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe uma Descrição válida");
		
		
		lancamento.setDescricao("Salario");
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");
		
		lancamento.setMes(0);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");
		
		lancamento.setMes(13);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Mês válido");
		
		lancamento.setMes(1);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido");
		
		lancamento.setAno(202);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Ano válido");
		
		lancamento.setAno(2020);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		
		lancamento.setUsuario(new Usuario());
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Usuário.");
		
		lancamento.getUsuario().setId(1L);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido");
		
		lancamento.setValor(BigDecimal.ZERO);
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Valor válido");
		
		lancamento.setValor(BigDecimal.valueOf(1));
		
		erro = Assertions.catchThrowable(()-> service.validar(lancamento));
		Assertions.assertThat(erro).isInstanceOf(RegraNegocioException.class).hasMessage("Informe um Tipo de Lançamento");
		
		
		
		
		
	}

	

}
