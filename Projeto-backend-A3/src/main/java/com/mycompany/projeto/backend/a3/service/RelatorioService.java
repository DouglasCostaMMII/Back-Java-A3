package com.mycompany.projeto.backend.a3.service;

import com.mycompany.projeto.backend.a3.model.EntradaMov;
import com.mycompany.projeto.backend.a3.model.SaidaMov;
import com.mycompany.projeto.backend.a3.dto.MovimentacaoEstoqueRequest;
import com.mycompany.projeto.backend.a3.dto.ResumoRelatorio;
import com.mycompany.projeto.backend.a3.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Comparator;

@Service
public class RelatorioService {

    @Autowired private EntradaRepository entradaRepo;
    @Autowired private SaidaRepository saidaRepo;
    @Autowired private ProdutoRepository produtoRepo;


    public List<MovimentacaoEstoqueRequest> gerarRelatorioCompleto() {
        List<MovimentacaoEstoqueRequest> relatorio = new ArrayList<>();

        // Busca Entradas e converte para DTO
        List<EntradaMov> entradas = entradaRepo.findAllComDetalhes();
        for (EntradaMov e : entradas) {
            MovimentacaoEstoqueRequest dto = new MovimentacaoEstoqueRequest();
            dto.setProdutoId(e.getId());
            dto.setQuantidade(e.getQuantidade());
            dto.setData(e.getDataHora());
            dto.setNomeProduto(e.getProduto().getNome());
            dto.setNomeCategoria(e.getCategoria().getNome());
            dto.setTipo("ENTRADA");
            relatorio.add(dto);
        }

        // Busca Saídas e converte para DTO 
        List<SaidaMov> saidas = saidaRepo.findAllComDetalhes();
        for (SaidaMov s : saidas) {
            MovimentacaoEstoqueRequest dto = new MovimentacaoEstoqueRequest();
            dto.setProdutoId(s.getId());
            dto.setQuantidade(s.getQuantidade());
            dto.setData(s.getDataHora()); 
            dto.setNomeProduto(s.getProduto().getNome());
            dto.setNomeCategoria(s.getCategoria().getNome());
            dto.setTipo("SAIDA");
            relatorio.add(dto);
        }

        relatorio.sort(Comparator.comparing(MovimentacaoEstoqueRequest::getData)
            .reversed());

        return relatorio;
    }

    public ResumoRelatorio gerarResumo() {
        long totalProdutos = produtoRepo.count();
        long estoqueBaixo = produtoRepo.countByQuantidadeLessThan(10); // Exemplo: menos de 10 itens

        return new ResumoRelatorio(totalProdutos, estoqueBaixo);
    }
}