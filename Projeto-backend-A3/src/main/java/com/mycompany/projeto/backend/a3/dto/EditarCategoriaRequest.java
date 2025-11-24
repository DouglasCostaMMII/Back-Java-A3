/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projeto.backend.a3.dto;
import lombok.Data;

// JSON do Post Editar
@Data
public class EditarCategoriaRequest {
    private String nome;
    private String editStatus;
    private Long editCategoriaId;
    private String tamanho;
    private String embalagem;
}