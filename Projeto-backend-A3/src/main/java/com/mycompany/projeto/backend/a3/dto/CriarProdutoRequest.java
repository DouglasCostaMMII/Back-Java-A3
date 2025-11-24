package com.mycompany.projeto.backend.a3.dto;

import java.math.BigDecimal;

/**
 * DTO para receber dados necessários para a criação de um novo Produto.
 * Usado no POST para /api/produto/criar.
 */
public class CriarProdutoRequest {
    
    private String nome;
    private BigDecimal preco;
    private Integer quantidade; 
    private Integer quantidadeMinima; 
    private String status;
    
    // Campo essencial para ligar o produto à sua categoria
    private Long categoriaId; 

    // --- Getters e Setters ---

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    
    public BigDecimal getPreco() { return preco; }
    public void setPreco(BigDecimal preco) { this.preco = preco; }

    public Integer getQuantidadeMinima() { return quantidadeMinima; }
    public void setQuantidadeMinima(Integer quantidadeMinima) { this.quantidadeMinima = quantidadeMinima; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    
    public Long getCategoriaId() { return categoriaId; }
    public void setCategoriaId(Long categoriaId) { this.categoriaId = categoriaId; }
}