
package com.mycompany.projeto.backend.a3.dto;


public class AtualizarStatusProdutoRequest {

  private Long produtoId; 
    private String status; // ATIVO ou INATIVO

    // Getters e Setters
    public Long getProdutoId() {
        return produtoId;
    }

    public void setProdutoId(Long produtoId) {
        this.produtoId = produtoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}