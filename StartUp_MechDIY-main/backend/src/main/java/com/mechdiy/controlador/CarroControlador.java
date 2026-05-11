package com.mechdiy.controlador;

import com.mechdiy.dto.CarroDetalheDTO;
import com.mechdiy.dto.CarroResumoDTO;
import com.mechdiy.dto.ItemManutencaoDTO;
import com.mechdiy.servico.CarroServico;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controlador REST para veículos e seus itens de manutenção.
 * Todos os dados vêm do JSON carregado na inicialização — não há chamadas externas.
 *
 * URL base: /api/carros
 *
 * Endpoints:
 *   GET /api/carros                              → lista todos os carros
 *   GET /api/carros?marca=Toyota                 → filtra por marca
 *   GET /api/carros?marca=Toyota&modelo=Corolla  → filtra por marca e modelo
 *   GET /api/carros?ano=2022                     → filtra por ano
 *   GET /api/carros/{id}                         → detalhe com itens de manutenção
 *   GET /api/carros/{id}/manutencao/{tipo}       → item específico (filtro, bateria, etc.)
 *   GET /api/carros/buscar?termo=civic           → busca livre
 */
@RestController
@RequestMapping("/api/carros")
public class CarroControlador {

    private final CarroServico carroServico;

    public CarroControlador(CarroServico carroServico) {
        this.carroServico = carroServico;
    }

    /**
     * Lista veículos com filtros opcionais por marca, modelo e/ou ano.
     *
     * GET /api/carros
     * GET /api/carros?marca=Toyota
     * GET /api/carros?marca=Honda&modelo=Civic
     * GET /api/carros?ano=2023
     *
     * @param marca  filtra pela marca (opcional, case-insensitive)
     * @param modelo filtra pelo modelo (opcional, case-insensitive)
     * @param ano    filtra pelo ano (opcional)
     */
    @GetMapping
    public ResponseEntity<List<CarroResumoDTO>> listarCarros(
            @RequestParam(required = false) String marca,
            @RequestParam(required = false) String modelo,
            @RequestParam(required = false) Integer ano) {

        return ResponseEntity.ok(carroServico.buscarComFiltros(marca, modelo, ano));
    }

    /**
     * Retorna os detalhes completos de um veículo, incluindo todos os itens de manutenção
     * com descrição, URL do vídeo e link do produto.
     *
     * GET /api/carros/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<CarroDetalheDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(carroServico.buscarPorId(id));
    }

    /**
     * Retorna um item de manutenção específico de um veículo.
     *
     * GET /api/carros/{id}/manutencao/filtro
     * GET /api/carros/{id}/manutencao/bateria
     * GET /api/carros/{id}/manutencao/ar_condicionado
     *
     * @param id   ID do carro
     * @param tipo tipo do item conforme o JSON (filtro, bateria, ar_condicionado, etc.)
     */
    @GetMapping("/{id}/manutencao/{tipo}")
    public ResponseEntity<ItemManutencaoDTO> buscarItemManutencao(
            @PathVariable Long id,
            @PathVariable String tipo) {

        return ResponseEntity.ok(carroServico.buscarItemPorTipo(id, tipo));
    }

    /**
     * Busca veículos por termo livre em marca ou modelo.
     *
     * GET /api/carros/buscar?termo=toyota
     * GET /api/carros/buscar?termo=civic
     *
     * @param termo palavra-chave de busca (case-insensitive)
     */
    @GetMapping("/buscar")
    public ResponseEntity<List<CarroResumoDTO>> buscar(
            @RequestParam("termo") String termo) {

        return ResponseEntity.ok(carroServico.buscar(termo));
    }
}
