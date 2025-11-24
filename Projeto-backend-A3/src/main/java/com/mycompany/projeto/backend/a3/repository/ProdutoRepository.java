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

       @Query(value = "SELECT p.nome AS produto, " +
           "(SELECT COALESCE(SUM(e.quantidade), 0) FROM entradas e WHERE e.produto_id = p.produtoid) AS totalEntradas, " +
           "(SELECT COALESCE(SUM(s.quantidade), 0) FROM saidas s WHERE s.produto_id = p.produtoid) AS totalSaidas " +
           "FROM produtos p", nativeQuery = true)
    List<Object[]> buscarResumoMovimentacoes();

    long countByQuantidadeLessThan(Integer quantidadeMinima);

}