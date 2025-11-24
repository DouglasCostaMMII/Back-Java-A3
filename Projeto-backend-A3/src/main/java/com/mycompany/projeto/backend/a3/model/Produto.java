package com.mycompany.projeto.backend.a3.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import lombok.Data;

@Data
@Entity
@Table(name = "produtos")
public class Produto {

    // CHAVE PRIMÁRIA
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "produtoid")
    private Long produtoId; 

    @Column(name = "nome", nullable = false)
    private String nome;
    
    // Coluna 'quantidade'
    @Column(name = "quantidade") 
    private Integer quantidade;

    // Coluna 'preco'
    @Column(name = "preco", nullable = false, precision = 10, scale = 2) 
    private BigDecimal preco; 

    
    // Coluna 'quantidade_minima'
    @Column(name = "quantidade_minima", nullable = false)
    private Integer quantidadeMinima;
    
    // Coluna 'status'
    @Column(name = "status", nullable = false)
    private String status; 
    
// --- Relacionamento com Categoria (Chave Estrangeira) ---
    @ManyToOne(fetch = FetchType.EAGER) 
    @JoinColumn(name = "categoria_id", nullable = false) // <--- ALTERADO para 'categoriaid' (sem underscore)
    private Categoria categoria;

    
    // --- Construtor Padrão (necessário para JPA) ---
    public Produto() {
    }
}