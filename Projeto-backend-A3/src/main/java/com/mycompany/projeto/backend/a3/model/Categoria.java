/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projeto.backend.a3.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "categorias") // Nome exato da sua tabela no MySQL
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoriaid;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "status", nullable = false)
    private String status;
    
    @Column(name = "tamanho", nullable = false)
    private String tamanho;
    
    @Column(name = "embalagem", nullable = false)
    private String embalagem;

}
