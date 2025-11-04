package com.mycompany.projeto.backend.a3.repository;

import com.mycompany.projeto.backend.a3.model.Produto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface de acesso a dados para a entidade Produto.
 * Extende JpaRepository para fornecer métodos CRUD (Create, Read, Update, Delete).
 */
@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {

    // A JpaRepository já fornece: save(), findById(), findAll(), delete(), etc.
    
    // Você pode adicionar métodos personalizados aqui, se precisar.
    // Exemplo: Buscar produtos por nome
    // List<Produto> findByNomeContainingIgnoreCase(String nome);
    
    // Exemplo: Buscar produtos por status
    // List<Produto> findByStatus(String status);
}