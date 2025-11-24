package com.mycompany.projeto.backend.a3.controller;
import com.mycompany.projeto.backend.a3.model.Produto;
import com.mycompany.projeto.backend.a3.model.Categoria;
import com.mycompany.projeto.backend.a3.model.MovimentacaoEstoque;
import com.mycompany.projeto.backend.a3.model.TipoMovimentacao;
import com.mycompany.projeto.backend.a3.repository.ProdutoRepository;
import com.mycompany.projeto.backend.a3.repository.MovimentacaoEstoqueRepository;
import com.mycompany.projeto.backend.a3.repository.CategoriaRepository;
import com.mycompany.projeto.backend.a3.dto.AtualizarStatusProdutoRequest;
import com.mycompany.projeto.backend.a3.dto.CriarProdutoRequest;
import com.mycompany.projeto.backend.a3.dto.EditarProdutoRequest;
import com.mycompany.projeto.backend.a3.dto.MovimentacaoEstoqueRequest; 
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
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.POST,RequestMethod.GET,RequestMethod.PUT,RequestMethod.OPTIONS})
public class ProdutoController {

    @Autowired
    private ProdutoRepository produtoRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private MovimentacaoEstoqueRepository MovimentacaoEstoqueRepository;

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

@PostMapping("/produto/editar") 
public ResponseEntity<?> editarProduto(@RequestBody EditarProdutoRequest request) { // Use o DTO com o campo ID

    // 💡 Captura o ID do Produto DIRETAMENTE do corpo da requisição (DTO)
    Long produtoId = request.getProdutoId(); 
    
    // 1. Busca o Produto Existente
    Optional<Produto> produtoOptional = produtoRepository.findById(produtoId); // Usa o ID do DTO

    if (produtoOptional.isEmpty()) {
        return new ResponseEntity<>(
            Map.of("Erro", "Produto não encontrado com o ID: " + produtoId), 
            HttpStatus.NOT_FOUND // 404
        );
    }

    // 2. Obtém a instância do Produto para edição
    Produto produtoExistente = produtoOptional.get();
    
    // 3. Verifica se a Categoria precisa ser alterada
    Optional<Categoria> categoriaOptional = categoriaRepository.findById(request.getCategoriaId());
    
    if (categoriaOptional.isEmpty()) {
        return new ResponseEntity<>(
            Map.of("Erro", "Categoria não encontrada com o ID: " + request.getCategoriaId()), 
            HttpStatus.NOT_FOUND // 404
        );
    }
    
    // 4. Mapeia e Atualiza os Campos
    Categoria novaCategoria = categoriaOptional.get();
    
    produtoExistente.setNome(request.getNome());
    produtoExistente.setPreco(request.getPreco());
    produtoExistente.setQuantidadeMinima(request.getQuantidadeMinima());
    produtoExistente.setCategoria(novaCategoria); // Garante a atualização da Categoria
    
    if (request.getStatus() != null) {
        produtoExistente.setStatus(request.getStatus().toUpperCase());
    }

    try {
        // 5. Salva a instância atualizada
        produtoRepository.save(produtoExistente);
        
        return new ResponseEntity<>(
            Map.of("Mensagem", "Produto ID " + produtoId + " atualizado com sucesso"), 
            HttpStatus.OK
        );
        
    } catch (Exception e) {
        return new ResponseEntity<>(
            Map.of("Erro", "Erro interno ao atualizar Produto: " + e.getMessage()), 
            HttpStatus.INTERNAL_SERVER_ERROR // 500
        );
    }
}

   
// API para Movimentação de Estoque (Aumentar/Diminuir)
    // Rota: POST /api/produto/movimentar-estoque
    @PostMapping("/produtos/movimentar-estoque")
public ResponseEntity<?> movimentarEstoque(@RequestBody MovimentacaoEstoqueRequest request) {

    Optional<Produto> produtoOptional = produtoRepository.findById(request.getProdutoId());

    if (produtoOptional.isEmpty()) {
        return new ResponseEntity<>(
                Map.of("Erro", "Produto não encontrado com o ID: " + request.getProdutoId()),
                HttpStatus.NOT_FOUND
        );
    }

    Produto produto = produtoOptional.get();
    Integer quantidadeMovimentada = request.getQuantidade();
    String tipo = request.getTipo() != null ? request.getTipo().toUpperCase() : "";

    if (quantidadeMovimentada == null || quantidadeMovimentada <= 0) {
        return new ResponseEntity<>(
                Map.of("Erro", "A quantidade para movimentação deve ser maior que zero."),
                HttpStatus.BAD_REQUEST
        );
    }

    try {
        // Atualiza estoque
        if ("ENTRADA".equals(tipo)) {
            produto.setQuantidade(produto.getQuantidade() + quantidadeMovimentada);
        } else if ("SAIDA".equals(tipo)) {

            if (produto.getQuantidade() < quantidadeMovimentada) {
                return new ResponseEntity<>(
                        Map.of("Erro", "Estoque insuficiente. Saldo atual: " + produto.getQuantidade()),
                        HttpStatus.BAD_REQUEST
                );
            }

            produto.setQuantidade(produto.getQuantidade() - quantidadeMovimentada);

        } else {
            return new ResponseEntity<>(
                    Map.of("Erro", "Tipo inválido. Use 'ENTRADA' ou 'SAIDA'."),
                    HttpStatus.BAD_REQUEST
            );
        }

        produtoRepository.save(produto);

        // REGISTRA A MOVIMENTAÇÃO
        MovimentacaoEstoque mov = new MovimentacaoEstoque();
        mov.setProduto(produto);
        mov.setQuantidade(quantidadeMovimentada);
        mov.setTipo("ENTRADA".equals(tipo) ? TipoMovimentacao.ENTRADA : TipoMovimentacao.SAIDA);
        mov.setDataMovimentacao(LocalDateTime.now());

        MovimentacaoEstoqueRepository.save(mov);

        return new ResponseEntity<>(
                Map.of(
                    "Mensagem", "Movimentação registrada com sucesso",
                    "NovoEstoque", produto.getQuantidade()
                ),
                HttpStatus.OK
        );

    } catch (Exception e) {
        return new ResponseEntity<>(
                Map.of("Erro", "Erro interno ao movimentar estoque: " + e.getMessage()),
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}



// ... (Métodos getProdutos, addProduto, editarProduto e movimentarEstoque permanecem os mesmos) ...

    // NOVO: API para Atualizar Status (LIMPA: usa apenas ProdutoId e Status)
    // Rota: PUT /api/produto/status
    @PostMapping("/produtos/status")
    public ResponseEntity<?> atualizarStatus(@RequestBody AtualizarStatusProdutoRequest request) {
        
        // 1. Obtém os valores de forma clara e correta
        Long produtoId = request.getProdutoId();
        String novoStatus = request.getStatus();

        if (produtoId == null || novoStatus == null || novoStatus.trim().isEmpty()) {
             return new ResponseEntity<>(
                Map.of("Erro", "Os campos 'produtoId' e 'status' são obrigatórios."), 
                HttpStatus.BAD_REQUEST // 400
            );
        }

        // 2. Busca o Produto
        Optional<Produto> produtoOptional = produtoRepository.findById(produtoId);

        if (produtoOptional.isEmpty()) {
            return new ResponseEntity<>(
                Map.of("Erro", "Produto não encontrado com o ID: " + produtoId), 
                HttpStatus.NOT_FOUND // 404
            );
        }

        Produto produto = produtoOptional.get();
        
        try {
            // 3. Define o novo status
            String statusFormatado = novoStatus.toUpperCase();
            
            // Validação simples de status
            if (!statusFormatado.equals("ATIVO") && !statusFormatado.equals("INATIVO")) {
                 return new ResponseEntity<>(
                    Map.of("Erro", "O status deve ser 'ATIVO' ou 'INATIVO'."), 
                    HttpStatus.BAD_REQUEST // 400
                );
            }
            
            produto.setStatus(statusFormatado); 

            // 4. Salva a atualização
            produtoRepository.save(produto);
            
            return new ResponseEntity<>(
                Map.of("Mensagem", "Status do Produto ID " + produtoId + " atualizado para: " + statusFormatado), 
                HttpStatus.OK // 200
            );

        } catch (Exception e) {
            return new ResponseEntity<>(
                Map.of("Erro", "Erro interno ao atualizar status do Produto: " + e.getMessage()), 
                HttpStatus.INTERNAL_SERVER_ERROR // 500
            );
        }
    }
}