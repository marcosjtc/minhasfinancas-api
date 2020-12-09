package com.marcosjtc.minhasfinancas.service;

import java.util.List;

import com.marcosjtc.minhasfinancas.model.entity.Lancamento;
import com.marcosjtc.minhasfinancas.model.enums.StatusLancamento;

public interface LancamentoService {
	
	Lancamento salvar(Lancamento lancamento);
	
	Lancamento atualizar(Lancamento lancamento);
	
	void deletar(Lancamento lancamento);
	
	List<Lancamento> buscar(Lancamento lancamento);
	
	void atualizarStatus(Lancamento lancamento, StatusLancamento status);
	
	void validar(Lancamento lancamento);
	

}
