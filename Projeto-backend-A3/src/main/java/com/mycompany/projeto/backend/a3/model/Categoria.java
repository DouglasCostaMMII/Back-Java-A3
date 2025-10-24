/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projeto.backend.a3.model;

import jakarta.persistence.*;

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

    // Getters e Setters
    // (O Spring usa isso para ler e escrever os dados)

    public Long getCategoriaid() {
        return categoriaid;
    }

    public void setCategoriaid(Long categoriaid) {
        this.categoriaid = categoriaid;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
    
    public String getEmbalagem(){
        return embalagem;
    }

    public void setEmbalagem(String embalagem){
        this.embalagem = embalagem;
    }
    
    public String getTamanho(){
        return tamanho;
    }
    
    public void setTamanho(String tamanho){
        this.tamanho = tamanho;
    }
    
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
