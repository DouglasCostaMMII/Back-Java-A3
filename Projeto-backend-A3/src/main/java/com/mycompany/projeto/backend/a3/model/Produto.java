package com.mycompany.projeto.backend.a3.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
// Não precisa mais de importações de Jackson aqui

/**
 * Representa a entidade Produto no banco de dados,
 * alinhada com as colunas reais da tabela 'produtos'.
 */
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

    // Coluna 'quantidade_estoque'
    @Column(name = "quantidade_estoque", nullable = false)
    private Integer quantidadeEstoque;
    
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

    // --- Getters e Setters ---

    public Long getProdutoId() { return produtoId; }
    public void setProdutoId(Long produtoId) { this.produtoId = produtoId; }
    
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }

    public Integer getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(Integer quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    
    public Integer getQuantidadeMinima() { return quantidadeMinima; }
    public void setQuantidadeMinima(Integer quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // Getter/Setter da Categoria
    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }
}