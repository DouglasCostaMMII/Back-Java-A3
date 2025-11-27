package com.mycompany.projeto.backend.a3.repository;

import com.mycompany.projeto.backend.a3.model.MovimentacaoEstoque;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {




@Query("SELECT m.produto.nome, SUM(m.quantidade) as total " +
       "FROM MovimentacaoEstoque m " +
       "WHERE m.tipo = 'ENTRADA' " +
       "GROUP BY m.produto.nome " +
       "ORDER BY total DESC") 
List<Object[]> findTopEntradas(Pageable pageable);


@Query("SELECT m.produto.nome, SUM(m.quantidade) as total " +
       "FROM MovimentacaoEstoque m " +
       "WHERE m.tipo = 'SAIDA' " +
       "GROUP BY m.produto.nome " +
       "ORDER BY total DESC") 
List<Object[]> findTopSaidas(Pageable pageable);
}