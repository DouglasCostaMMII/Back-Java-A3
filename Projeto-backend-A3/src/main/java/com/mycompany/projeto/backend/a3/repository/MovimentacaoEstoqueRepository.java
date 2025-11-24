package com.mycompany.projeto.backend.a3.repository;

import com.mycompany.projeto.backend.a3.model.MovimentacaoEstoque;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MovimentacaoEstoqueRepository extends JpaRepository<MovimentacaoEstoque, Long> {

    @Query("""
        SELECT m.produto.nome,
               SUM(CASE WHEN m.tipo = 'ENTRADA' THEN m.quantidade ELSE 0 END) AS totalEntradas,
               SUM(CASE WHEN m.tipo = 'SAIDA' THEN m.quantidade ELSE 0 END) AS totalSaidas
        FROM MovimentacaoEstoque m
        GROUP BY m.produto.nome
    """)
    List<Object[]> buscarResumoMovimentacoes();
}