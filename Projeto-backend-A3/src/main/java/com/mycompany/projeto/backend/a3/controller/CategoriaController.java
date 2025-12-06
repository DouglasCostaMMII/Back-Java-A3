/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.projeto.backend.a3.controller;

import com.mycompany.projeto.backend.a3.model.Categoria;
import com.mycompany.projeto.backend.a3.repository.CategoriaRepository;
import com.mycompany.projeto.backend.a3.dto.EditarCategoriaRequest;
import com.mycompany.projeto.backend.a3.dto.AlterarStatusRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class CategoriaController {

    @Autowired // Injeta o repositório
    private CategoriaRepository categoriaRepository;

    // Obter todas categorias
    @GetMapping("/categorias")
    public ResponseEntity<List<Categoria>> getCategorias() {
        List<Categoria> results = categoriaRepository.findAll();
        return ResponseEntity.ok(results);
    }

    // Criar Categorias
    @PostMapping("/categoria/criar")
    public ResponseEntity<?> addCategoria(@RequestBody Categoria novaCategoria) {

        try {
            novaCategoria.setStatus(novaCategoria.getStatus().toUpperCase());
            categoriaRepository.save(novaCategoria);
            return new ResponseEntity<>(Map.of("Mensagem", "Categoria adicionada com sucesso"), HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("Erro", "erro ao adicionar Categoria"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Editar Categoria
    @PostMapping("/categoria/editar")
    public ResponseEntity<?> editarCategoria(@RequestBody EditarCategoriaRequest request) {
        Optional<Categoria> opcional = categoriaRepository.findById(request.getEditCategoriaId());

        if (opcional.isEmpty()) {
            return new ResponseEntity<>(Map.of("Erro", "Categoria não encontrada"), HttpStatus.NOT_FOUND);
        }

        try {
            Categoria categoria = opcional.get();
            categoria.setNome(request.getNome());
            categoria.setStatus(request.getEditStatus().toUpperCase());
            categoria.setTamanho(request.getTamanho());
            categoria.setEmbalagem(request.getEmbalagem());

            categoriaRepository.save(categoria);
            return ResponseEntity.ok(Map.of("Mensagem", "Categoria Modificada com sucesso"));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("Erro", "erro ao Modificar Categoria"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Alterar Status
    @PostMapping("/categoria/alt_status")
    public ResponseEntity<?> alterarStatus(@RequestBody AlterarStatusRequest request) {

        Optional<Categoria> opcional = categoriaRepository.findById(request.getCategoriaid());

        if (opcional.isEmpty()) {
            return new ResponseEntity<>(Map.of("Erro", "Categoria não encontrada"), HttpStatus.NOT_FOUND);
        }

        try {
            Categoria categoria = opcional.get();
            categoria.setStatus(request.getStatus().toUpperCase());

            categoriaRepository.save(categoria);
            return ResponseEntity.ok(Map.of("Mensagem", "Status da Categoria Modificada com sucesso"));
        } catch (Exception e) {
            return new ResponseEntity<>(Map.of("Erro", "erro ao Modificar Status da Categoria"),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}