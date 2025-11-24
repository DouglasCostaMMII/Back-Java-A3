/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projeto.backend.a3.dto;

// JSON do Post Editar
public class EditarCategoriaRequest {
    private String nome;
    private String editStatus;
    private Long editCategoriaId;
    private String tamanho;
    private String embalagem;
    
    // Getters e Setters
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    
    public String getEditStatus() { return editStatus; }
    public void setEditStatus(String editStatus) { this.editStatus = editStatus; }
    
    public Long getEditCategoriaId() { return editCategoriaId; }
    public void setEditCategoriaId(Long editCategoriaId) { this.editCategoriaId = editCategoriaId; }
    
    public String getTamanho() {return tamanho;}
    public void setTamanho(String Tamanho) { this.tamanho = Tamanho; }
    
    public String getEmbalagem(){ return embalagem;}
    public void setEmbalagem(String Embalagem){ this.embalagem = Embalagem;}
}
