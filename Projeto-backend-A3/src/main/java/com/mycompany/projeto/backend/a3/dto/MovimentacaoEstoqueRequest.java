
package com.mycompany.projeto.backend.a3.dto;


public class MovimentacaoEstoqueRequest {

    private Long produtoId;
    private String tipo; // ENTRADA ou SAIDA
    private Integer quantidade;

    // Getters e Setters

    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }
}