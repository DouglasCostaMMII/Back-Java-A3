package com.mycompany.projeto.backend.a3.dto;


import lombok.Data;
import lombok.AllArgsConstructor;

@Data
@AllArgsConstructor
public class ResumoRelatorio {
    private Long totalProdutos;
    private Long produtosEstoqueBaixo;
}
