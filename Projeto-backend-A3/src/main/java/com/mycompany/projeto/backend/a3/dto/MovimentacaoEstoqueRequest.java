
package com.mycompany.projeto.backend.a3.dto;

import lombok.Data;
import java.time.LocalDateTime;


@Data
public class MovimentacaoEstoqueRequest {

    private Long produtoId;
    private String tipo;
    private Integer quantidade;
    private String nomeProduto;
    private String nomeCategoria;
    private LocalDateTime data; 

}