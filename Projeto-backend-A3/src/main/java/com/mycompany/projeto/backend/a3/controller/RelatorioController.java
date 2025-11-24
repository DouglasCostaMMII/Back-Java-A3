package com.mycompany.projeto.backend.a3.controller;
import com.mycompany.projeto.backend.a3.model.Produto;
import com.mycompany.projeto.backend.a3.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.lang.reflect.Method;
import java.util.*;
import java.util.stream.Collectors;
import com.mycompany.projeto.backend.a3.repository.MovimentacaoEstoqueRepository;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class RelatorioController {

    @Autowired
    private ProdutoRepository produtoRepository;

    // tenta obter unidade via reflection
    private String tentarObterUnidade(Produto p) {
        try {
            Method m = p.getClass().getMethod("getUnidadeMedida");
            Object val = m.invoke(p);
            return val != null ? val.toString() : "N/A";
        } catch (NoSuchMethodException ignored) {
        } catch (Exception e) {
        }

        try {
            Method m2 = p.getClass().getMethod("getUnidade");
            Object val = m2.invoke(p);
            return val != null ? val.toString() : "N/A";
        } catch (NoSuchMethodException ignored) {
        } catch (Exception e) {
        }

        return "N/A";
    }

    // 1 Lista de Preços
    @GetMapping("/relatorios/listaPrecos")
    public ResponseEntity<?> listaPrecos() {
        try {
            List<Produto> produtos = produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));

            List<Map<String, Object>> lista = produtos.stream().map(p -> {
                Map<String, Object> m = new HashMap<>();
                m.put("produtoId", p.getProdutoId());
                m.put("nome", p.getNome());
                m.put("preco", p.getPreco());
                m.put("categoria", p.getCategoria() != null ? p.getCategoria().getNome() : "Sem categoria");
                return m;
            }).collect(Collectors.toList());

            return ResponseEntity.ok(lista);

        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Falha ao gerar Lista de Preços");
            erro.put("detalhes", e.getMessage());
            return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 2 Balanço Físico / Financeiro
    @GetMapping("/relatorios/balanco")
    public ResponseEntity<?> balancoFisicoFinanceiro() {
        try {
            List<Produto> produtos = produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));

            List<Map<String, Object>> items = new ArrayList<>();
            BigDecimal totalEstoque = BigDecimal.ZERO;

            for (Produto p : produtos) {
                BigDecimal preco = BigDecimal.ZERO;
                Integer quantidade = 0;

                // pega preco se não for nulo (suporta Double, BigDecimal, Float)
                if (p.getPreco() != null) {
                    Object precoObj = p.getPreco();
                    if (precoObj instanceof BigDecimal) {
                        preco = (BigDecimal) precoObj;
                    } else if (precoObj instanceof Number) {
                        preco = BigDecimal.valueOf(((Number) precoObj).doubleValue());
                    } else {
                        try {
                            preco = new BigDecimal(precoObj.toString());
                        } catch (Exception ex) {
                            preco = BigDecimal.ZERO;
                        }
                    }
                }

                if (p.getQuantidade() != null) {
                    quantidade = p.getQuantidade();
                }

                BigDecimal valorTotalProduto = preco.multiply(BigDecimal.valueOf(quantidade));
                totalEstoque = totalEstoque.add(valorTotalProduto);

                Map<String, Object> m = new HashMap<>();
                m.put("produtoId", p.getProdutoId());
                m.put("nome", p.getNome());
                m.put("categoria", p.getCategoria() != null ? p.getCategoria().getNome() : "Sem categoria");
                m.put("quantidade", quantidade);
                m.put("precoUnitario", preco);
                m.put("valorTotalDoProduto", valorTotalProduto);
                items.add(m);
            }

            Map<String, Object> resp = new HashMap<>();
            resp.put("itens", items);
            resp.put("valor_total_estoque", totalEstoque);

            return ResponseEntity.ok(resp);

        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Falha ao gerar Balanço Físico/Financeiro");
            erro.put("detalhes", e.getMessage());
            return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 3 Produtos abaixo da quantidade mínima
    @GetMapping("/relatorios/estoqueMinimo")
    public ResponseEntity<?> produtosAbaixoDaMinima() {
        try {
            List<Produto> produtos = produtoRepository.findAll(Sort.by(Sort.Direction.ASC, "nome"));

            List<Map<String, Object>> abaixo = produtos.stream()
                    .filter(p -> p.getQuantidade() != null && p.getQuantidadeMinima() != null
                    && p.getQuantidade() <= p.getQuantidadeMinima())
                    .map(p -> {
                        Map<String, Object> m = new HashMap<>();
                        m.put("produtoId", p.getProdutoId());
                        m.put("nome", p.getNome());
                        m.put("quantidadeMinima", p.getQuantidadeMinima());
                        m.put("quantidade", p.getQuantidade());
                        m.put("categoria", p.getCategoria() != null ? p.getCategoria().getNome() : "Sem categoria");
                        return m;
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(abaixo);

        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Falha ao gerar relatório de Produtos abaixo da mínima");
            erro.put("detalhes", e.getMessage());
            return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 4 Quantidade de Produtos por Categoria
    @GetMapping("/relatorios/quantidadeCategoria")
    public ResponseEntity<?> getQuantidadeProdutosPorCategoria() {
        try {
            // Consulta o banco de dados via repositório (consulta customizada)
            List<Object[]> resultados = produtoRepository.contarProdutosPorCategoria();

            // Monta a resposta JSON
            List<Map<String, Object>> resposta = new ArrayList<>();
            for (Object[] linha : resultados) {
                Map<String, Object> item = new HashMap<>();
                item.put("categoria", linha[0]);
                item.put("quantidadeProdutosDistintos", linha[1]);
                resposta.add(item);
            }

            return ResponseEntity.ok(resposta);

        } catch (Exception e) {
            Map<String, Object> erro = new HashMap<>();
            erro.put("erro", "Falha ao gerar relatório de quantidade de produtos por categoria");
            erro.put("detalhes", e.getMessage());
            return new ResponseEntity<>(erro, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // 5 Produto com mais entrada e saída
   @Autowired
private MovimentacaoEstoqueRepository movimentacaoEstoqueRepository;

@GetMapping("/relatorios/produtoMaisMovimentado")
public ResponseEntity<Map<String, Object>> produtoMaisMovimentado() {

    List<Object[]> resultados = movimentacaoEstoqueRepository.buscarResumoMovimentacoes();

    if (resultados.isEmpty()) {
        return ResponseEntity.ok(Map.of("mensagem", "Nenhum dado encontrado."));
    }

    Object[] produtoMaisEntrada = resultados.stream()
            .max((a, b) -> Long.compare(
                    ((Number) a[1]).longValue(),
                    ((Number) b[1]).longValue()
            )).orElse(null);

    Object[] produtoMaisSaida = resultados.stream()
            .max((a, b) -> Long.compare(
                    ((Number) a[2]).longValue(),
                    ((Number) b[2]).longValue()
            )).orElse(null);

    return ResponseEntity.ok(
            Map.of(
                "produtoMaisEntrada", Map.of(
                        "nome", produtoMaisEntrada[0],
                        "totalEntradas", produtoMaisEntrada[1]
                ),
                "produtoMaisSaida", Map.of(
                        "nome", produtoMaisSaida[0],
                        "totalSaidas", produtoMaisSaida[2]
                )
            )
    );
}
}