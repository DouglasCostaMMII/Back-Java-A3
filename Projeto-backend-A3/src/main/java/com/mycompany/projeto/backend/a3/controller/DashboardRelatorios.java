

package com.mycompany.projeto.backend.a3.controller;

import com.mycompany.projeto.backend.a3.dto.MovimentacaoEstoqueRequest;
import com.mycompany.projeto.backend.a3.dto.ResumoRelatorio;
import com.mycompany.projeto.backend.a3.service.RelatorioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*") 
public class DashboardRelatorios {

    @Autowired
    private RelatorioService relatorioService;

    @GetMapping("/resumo/movimentacoes")
    public ResponseEntity<List<MovimentacaoEstoqueRequest>> getRelatorios() {
        try {
            List<MovimentacaoEstoqueRequest> resultado = relatorioService.gerarRelatorioCompleto();
            return ResponseEntity.ok(resultado);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/resumo")
    public ResponseEntity<ResumoRelatorio> getResumo() {
        try {
            ResumoRelatorio resumo = relatorioService.gerarResumo();
            return ResponseEntity.ok(resumo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}