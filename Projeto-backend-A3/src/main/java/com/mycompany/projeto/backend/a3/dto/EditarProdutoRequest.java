package com.mycompany.projeto.backend.a3.dto;

import java.math.BigDecimal;
import lombok.Data;


@Data
public class EditarProdutoRequest {
    
    private Long produtoId;
    private String nome;
    private BigDecimal preco;
    private Integer quantidadeMinima; 
    private String status;
    private Long categoriaId; 
}