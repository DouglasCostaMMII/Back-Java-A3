package com.mycompany.projeto.backend.a3.repository;

import com.mycompany.projeto.backend.a3.model.EntradaMov;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface EntradaRepository extends JpaRepository<EntradaMov, Long> {

@Query("SELECT e FROM EntradaMov e JOIN FETCH e.produto JOIN FETCH e.categoria ORDER BY e.dataHora DESC")
List<EntradaMov> findAllComDetalhes();

}