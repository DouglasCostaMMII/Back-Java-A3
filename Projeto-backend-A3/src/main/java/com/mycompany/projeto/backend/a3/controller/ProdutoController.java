package com.mycompany.projeto.backend.a3.controller;

import com.mycompany.projeto.backend.a3.model.Produto;
import com.mycompany.projeto.backend.a3.model.Categoria; // Importa o modelo de Categoria
import com.mycompany.projeto.backend.a3.repository.ProdutoRepository;
import com.mycompany.projeto.backend.a3.repository.CategoriaRepository; // Necessário para buscar a categoria
import com.mycompany.projeto.backend.a3.dto.CriarProdutoRequest;     // DTO de criação
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
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired // INJEÇÃO NECESSÁRIA para buscar o objeto Categoria
    private CategoriaRepository categoriaRepository;

    // Obter todos os produtos
    @GetMapping("/produtos")
    public ResponseEntity<List<Produto>> getProdutos() {
        List<Produto> results = produtoRepository.findAll();
        return ResponseEntity.ok(results);
    }

    @PostMapping("/produto/criar")
    public ResponseEntity<?> addProduto(@RequestBody CriarProdutoRequest request) {

        // 1. Busca a Categoria pelo ID fornecido no DTO
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(request.getCategoriaId());

        // 2. Valida se a Categoria existe
        if (categoriaOptional.isEmpty()) {
            return new ResponseEntity<>(
                Map.of("Erro", "Categoria não encontrada com o ID: " + request.getCategoriaId()), 
                HttpStatus.NOT_FOUND // 404
            );
        }

        try {
            Categoria categoria = categoriaOptional.get();
            
            // 3. Mapeia o DTO para a Entidade Produto
            Produto novoProduto = new Produto();
            novoProduto.setNome(request.getNome());
            novoProduto.setPreco(request.getPreco());
            novoProduto.setQuantidade(request.getQuantidade());
            novoProduto.setQuantidadeEstoque(request.getQuantidadeEstoque());
            novoProduto.setQuantidadeMinima(request.getQuantidadeMinima());
            
            // 4. Associa o objeto Categoria ao novo Produto
            novoProduto.setCategoria(categoria); 
            
            // Define o status em maiúsculas por convenção, caso seu banco exija
            if (request.getStatus() != null) {
                novoProduto.setStatus(request.getStatus().toUpperCase()); 
            } else {
                // Pode definir um status padrão, se necessário
                novoProduto.setStatus("ATIVO"); 
            }
            
            // 5. Salva no banco de dados
            Produto produtoSalvo = produtoRepository.save(novoProduto);
            
            // Retorna o status 201 Created e o ID do novo produto
            return new ResponseEntity<>(
                Map.of("Mensagem", "Produto adicionado com sucesso", "ProdutoId", produtoSalvo.getProdutoId()), 
                HttpStatus.CREATED // 201
            );
            
        } catch (Exception e) {
            // Em caso de erro de banco de dados ou conversão
            return new ResponseEntity<>(
                Map.of("Erro", "Erro interno ao adicionar Produto: " + e.getMessage()), 
                HttpStatus.INTERNAL_SERVER_ERROR // 500
            );
        }
    }

    @PutMapping("/produto/editar/{id}")
    public ResponseEntity<?> editarProduto(@PathVariable Long id, @RequestBody CriarProdutoRequest request) {
        
        // 1. Busca o Produto Existente
        Optional<Produto> produtoOptional = produtoRepository.findById(id);

        if (produtoOptional.isEmpty()) {
            return new ResponseEntity<>(
                Map.of("Erro", "Produto não encontrado com o ID: " + id), 
                HttpStatus.NOT_FOUND
            );
        }

        // 2. Obtém a instância do Produto para edição
        Produto produtoExistente = produtoOptional.get();
        
        // 3. Verifica se a Categoria precisa ser alterada
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(request.getCategoriaId());
        
        if (categoriaOptional.isEmpty()) {
            return new ResponseEntity<>(
                Map.of("Erro", "Categoria não encontrada com o ID: " + request.getCategoriaId()), 
                HttpStatus.NOT_FOUND
            );
        }
        
        // 4. Mapeia e Atualiza os Campos
        Categoria novaCategoria = categoriaOptional.get();
        
        produtoExistente.setNome(request.getNome());
        produtoExistente.setPreco(request.getPreco());
        produtoExistente.setQuantidade(request.getQuantidade());
        produtoExistente.setQuantidadeEstoque(request.getQuantidadeEstoque());
        produtoExistente.setQuantidadeMinima(request.getQuantidadeMinima());
        produtoExistente.setCategoria(novaCategoria); // Atualiza a categoria
        
        if (request.getStatus() != null) {
            produtoExistente.setStatus(request.getStatus().toUpperCase());
        }

        try {
            // 5. Salva a instância atualizada
            produtoRepository.save(produtoExistente);
            
            return new ResponseEntity<>(
                Map.of("Mensagem", "Produto ID " + id + " atualizado com sucesso"), 
                HttpStatus.OK
            );
            
        } catch (Exception e) {
            return new ResponseEntity<>(
                Map.of("Erro", "Erro interno ao atualizar Produto: " + e.getMessage()), 
                HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }


   

}