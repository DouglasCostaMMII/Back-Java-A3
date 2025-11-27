package com.mycompany.projeto.backend.a3.repository;


import com.mycompany.projeto.backend.a3.model.SaidaMov;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;

import java.util.List;


    
@Repository
public interface SaidaRepository extends JpaRepository<SaidaMov, Long> {

    @Query("SELECT s FROM SaidaMov s JOIN FETCH s.produto JOIN FETCH s.categoria ORDER BY s.dataHora DESC")
    List<SaidaMov> findAllComDetalhes();

    @Query("SELECT s.produto.nome, SUM(s.quantidade) as total " +
           "FROM SaidaMov s " +
           "GROUP BY s.produto.nome " +
           "ORDER BY total DESC")
    List<Object[]> findProdutoComMaisSaidas(Pageable pageable);

}
