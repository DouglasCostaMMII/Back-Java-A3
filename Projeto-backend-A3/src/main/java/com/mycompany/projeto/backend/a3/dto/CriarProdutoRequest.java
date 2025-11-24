package com.mycompany.projeto.backend.a3.dto;

import java.math.BigDecimal;
import lombok.Data;


@Data
 public class CriarProdutoRequest {
    
    private String nome;
    private BigDecimal preco;
    private Integer quantidade; 
    private Integer quantidadeMinima; 
    private String status;
    private Long categoriaId; 
 }
