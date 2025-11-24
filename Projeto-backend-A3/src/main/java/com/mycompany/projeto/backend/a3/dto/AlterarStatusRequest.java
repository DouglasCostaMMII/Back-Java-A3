/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projeto.backend.a3.dto;



public class AlterarStatusRequest {
    private Long categoriaid;
    private String status;


    // Getters e Setters
    public Long getCategoriaid() { return categoriaid; }
    public void setCategoriaid(Long categoriaid) { this.categoriaid = categoriaid; }
    
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

}
