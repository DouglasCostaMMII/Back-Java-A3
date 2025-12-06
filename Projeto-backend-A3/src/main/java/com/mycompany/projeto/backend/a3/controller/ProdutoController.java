package com.mycompany.projeto.backend.a3.controller;

import com.mycompany.projeto.backend.a3.model.Produto;
import com.mycompany.projeto.backend.a3.model.Categoria;

import com.mycompany.projeto.backend.a3.repository.ProdutoRepository;
import com.mycompany.projeto.backend.a3.repository.CategoriaRepository;

import com.mycompany.projeto.backend.a3.dto.AtualizarStatusProdutoRequest;
import com.mycompany.projeto.backend.a3.dto.CriarProdutoRequest;
import com.mycompany.projeto.backend.a3.dto.MovimentacaoEstoqueRequest;
import com.mycompany.projeto.backend.a3.dto.EditarProdutoRequest;

import com.mycompany.projeto.backend.a3.model.EntradaMov;
import com.mycompany.projeto.backend.a3.model.SaidaMov;

import com.mycompany.projeto.backend.a3.repository.EntradaRepository;
import com.mycompany.projeto.backend.a3.repository.SaidaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = { RequestMethod.POST, RequestMethod.GET, RequestMethod.PUT,
        RequestMethod.OPTIONS })
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private EntradaRepository entradaRepository;

    @Autowired
    private SaidaRepository saidaRepository;

    @GetMapping("/produtos")
    public ResponseEntity<List<Produto>> getProdutos() {
        List<Produto> results = produtoRepository.findAll();
        return ResponseEntity.ok(results);
    }

    @PostMapping("/produto/criar")
    public ResponseEntity<?> addProduto(@RequestBody CriarProdutoRequest request) {

        Optional<Categoria> categoriaOptional = categoriaRepository.findById(request.getCategoriaId());
        if (categoriaOptional.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("Erro", "Categoria não encontrada com o ID: " + request.getCategoriaId()),
                    HttpStatus.NOT_FOUND // 404
            );
        }

        try {
            Categoria categoria = categoriaOptional.get();

            Produto novoProduto = new Produto();
            novoProduto.setNome(request.getNome());
            novoProduto.setPreco(request.getPreco());
            novoProduto.setQuantidadeMinima(request.getQuantidadeMinima());
            novoProduto.setCategoria(categoria);

            if (request.getStatus() != null) {
                novoProduto.setStatus(request.getStatus().toUpperCase());
            } else {
                novoProduto.setStatus("ATIVO");
            }

            Produto produtoSalvo = produtoRepository.save(novoProduto);

            return new ResponseEntity<>(
                    Map.of("Mensagem", "Produto adicionado com sucesso", "ProdutoId", produtoSalvo.getProdutoId()),
                    HttpStatus.CREATED);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("Erro", "Erro interno ao adicionar Produto: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/produto/editar")
    public ResponseEntity<?> editarProduto(@RequestBody EditarProdutoRequest request) { // Use o DTO com o campo ID

        Long produtoId = request.getProdutoId();

        Optional<Produto> produtoOptional = produtoRepository.findById(produtoId); // Usa o ID do DTO

        if (produtoOptional.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("Erro", "Produto não encontrado com o ID: " + produtoId),
                    HttpStatus.NOT_FOUND);
        }

        Produto produtoExistente = produtoOptional.get();
        Optional<Categoria> categoriaOptional = categoriaRepository.findById(request.getCategoriaId());

        if (categoriaOptional.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("Erro", "Categoria não encontrada com o ID: " + request.getCategoriaId()),
                    HttpStatus.NOT_FOUND);
        }

        Categoria novaCategoria = categoriaOptional.get();

        produtoExistente.setNome(request.getNome());
        produtoExistente.setPreco(request.getPreco());
        produtoExistente.setQuantidadeMinima(request.getQuantidadeMinima());
        produtoExistente.setCategoria(novaCategoria); // Garante a atualização da Categoria

        if (request.getStatus() != null) {
            produtoExistente.setStatus(request.getStatus().toUpperCase());
        }

        try {

            produtoRepository.save(produtoExistente);

            return new ResponseEntity<>(
                    Map.of("Mensagem", "Produto ID " + produtoId + " atualizado com sucesso"),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("Erro", "Erro interno ao atualizar Produto: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR // 500
            );
        }
    }

    // API para Movimentação de Estoque (Aumentar/Diminuir)
    @PostMapping("/produtos/movimentar-estoque")
    public ResponseEntity<?> movimentarEstoque(@RequestBody MovimentacaoEstoqueRequest request) {

        Optional<Produto> produtoOptional = produtoRepository.findById(request.getProdutoId());

        if (produtoOptional.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("Erro", "Produto não encontrado com o ID: " + request.getProdutoId()),
                    HttpStatus.NOT_FOUND // 404
            );
        }

        Produto produto = produtoOptional.get();
        Integer quantidadeMovimentada = request.getQuantidade();
        String tipo = request.getTipo() != null ? request.getTipo().toUpperCase() : "";

        if (quantidadeMovimentada == null || quantidadeMovimentada <= 0) {
            return new ResponseEntity<>(
                    Map.of("Erro", "A quantidade para movimentação deve ser maior que zero."),
                    HttpStatus.BAD_REQUEST // 400
            );
        }

        try {

            if ("ENTRADA".equals(tipo)) {

                Integer quantidadeAtual = produto.getQuantidade();
                if (quantidadeAtual == null) {
                    quantidadeAtual = 0;
                }
                Integer novaQuantidade = quantidadeAtual + request.getQuantidade();
                produto.setQuantidade(novaQuantidade);
                EntradaMov novaEntrada = new EntradaMov();
                novaEntrada.setProduto(produto);
                novaEntrada.setQuantidade(quantidadeMovimentada);
                novaEntrada.setCategoria(produto.getCategoria());
                novaEntrada.setDataHora(LocalDateTime.now());
                entradaRepository.save(novaEntrada);

            } else if ("SAIDA".equals(tipo)) {
                if (produto.getQuantidade() < quantidadeMovimentada) { // Usa produto.getQuantidade() para validação
                    return new ResponseEntity<>(
                            Map.of("Erro",
                                    "Estoque insuficiente para a saída. Saldo atual: " + produto.getQuantidade()),
                            HttpStatus.BAD_REQUEST // 400
                    );
                }

                produto.setQuantidade(produto.getQuantidade() - quantidadeMovimentada);
                SaidaMov novaSaida = new SaidaMov();
                novaSaida.setProduto(produto);
                novaSaida.setQuantidade(quantidadeMovimentada);
                novaSaida.setCategoria(produto.getCategoria());
                novaSaida.setDataHora(LocalDateTime.now());

                saidaRepository.save(novaSaida);

            } else {
                return new ResponseEntity<>(
                        Map.of("Erro", "Tipo de movimentação inválido. Use 'ENTRADA' ou 'SAIDA'."),
                        HttpStatus.BAD_REQUEST);
            }

            Produto produtoAtualizado = produtoRepository.save(produto);

            return new ResponseEntity<>(
                    Map.of("Mensagem",
                            "Estoque do Produto ID " + produto.getProdutoId() + " atualizado para "
                                    + produtoAtualizado.getQuantidade(),
                            "Tipo", tipo),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("Erro", "Erro interno ao movimentar estoque: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // API para Atualizar Status
    @PostMapping("/produtos/status")
    public ResponseEntity<?> atualizarStatus(@RequestBody AtualizarStatusProdutoRequest request) {

        Long produtoId = request.getProdutoId();
        String novoStatus = request.getStatus();

        if (produtoId == null || novoStatus == null || novoStatus.trim().isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("Erro", "Os campos 'produtoId' e 'status' são obrigatórios."),
                    HttpStatus.BAD_REQUEST);
        }

        Optional<Produto> produtoOptional = produtoRepository.findById(produtoId);
        if (produtoOptional.isEmpty()) {
            return new ResponseEntity<>(
                    Map.of("Erro", "Produto não encontrado com o ID: " + produtoId),
                    HttpStatus.NOT_FOUND);
        }

        Produto produto = produtoOptional.get();

        try {
            String statusFormatado = novoStatus.toUpperCase();
            if (!statusFormatado.equals("ATIVO") && !statusFormatado.equals("INATIVO")) {
                return new ResponseEntity<>(
                        Map.of("Erro", "O status deve ser 'ATIVO' ou 'INATIVO'."),
                        HttpStatus.BAD_REQUEST // 400
                );
            }

            produto.setStatus(statusFormatado);
            produtoRepository.save(produto);

            return new ResponseEntity<>(
                    Map.of("Mensagem", "Status do Produto ID " + produtoId + " atualizado para: " + statusFormatado),
                    HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(
                    Map.of("Erro", "Erro interno ao atualizar status do Produto: " + e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}