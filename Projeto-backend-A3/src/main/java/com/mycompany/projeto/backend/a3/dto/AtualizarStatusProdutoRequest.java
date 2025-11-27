
package com.mycompany.projeto.backend.a3.dto;

import lombok.Data;

@Data
public class AtualizarStatusProdutoRequest {

  private Long produtoId; 
    private String status; 

}