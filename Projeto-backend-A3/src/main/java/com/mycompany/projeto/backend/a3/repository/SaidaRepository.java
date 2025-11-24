package com.mycompany.projeto.backend.a3.repository;


import com.mycompany.projeto.backend.a3.model.SaidaMov;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;


    
@Repository
public interface SaidaRepository extends JpaRepository<SaidaMov, Long> {

    @Query("SELECT s FROM SaidaMov s JOIN FETCH s.produto JOIN FETCH s.categoria ORDER BY s.dataHora DESC")
    List<SaidaMov> findAllComDetalhes();

}
