package com.marcosjtc.minhasfinancas.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.marcosjtc.minhasfinancas.model.entity.Lancamento;

public interface LancamentoRepository extends JpaRepository<Lancamento, Long> {

}
