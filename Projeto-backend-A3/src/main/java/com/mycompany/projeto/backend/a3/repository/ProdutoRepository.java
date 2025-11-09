package com.mycompany.projeto.backend.a3.repository;

import com.mycompany.projeto.backend.a3.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    @Query("SELECT c.nome AS categoria, COUNT(DISTINCT p.nome) AS quantidade " +
           "FROM Produto p " +
           "JOIN p.categoria c " +
           "GROUP BY c.nome " +
           "ORDER BY c.nome ASC")
    List<Object[]> contarProdutosPorCategoria();

    @Query("SELECT p.nome AS produto, " +
           "COALESCE(SUM(CASE WHEN m.tipo = 'ENTRADA' THEN m.quantidade ELSE 0 END), 0) AS totalEntradas, " +
           "COALESCE(SUM(CASE WHEN m.tipo = 'SAIDA' THEN m.quantidade ELSE 0 END), 0) AS totalSaidas " +
           "FROM MovimentacaoEstoque m " +
           "JOIN m.produto p " +
           "GROUP BY p.nome")
    List<Object[]> buscarResumoMovimentacoes();
}