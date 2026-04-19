package com.mechdiy.servico;

import com.mechdiy.dto.CarroDetalheDTO;
import com.mechdiy.dto.CarroResumoDTO;
import com.mechdiy.dto.ItemManutencaoDTO;
import com.mechdiy.modelo.Carro;
import com.mechdiy.modelo.ItemManutencao;
import com.mechdiy.repositorio.CarroRepositorio;
import com.mechdiy.repositorio.ItemManutencaoRepositorio;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Serviço com a lógica de negócio dos veículos e seus itens de manutenção.
 * Responsável por buscar, filtrar e converter os dados para DTOs
 * antes de enviá-los ao controlador.
 */
@Service
@Transactional(readOnly = true)
public class CarroServico {

    private final CarroRepositorio carroRepositorio;
    private final ItemManutencaoRepositorio itemRepositorio;

    public CarroServico(CarroRepositorio carroRepositorio,
                        ItemManutencaoRepositorio itemRepositorio) {
        this.carroRepositorio = carroRepositorio;
        this.itemRepositorio = itemRepositorio;
    }

    /**
     * Retorna todos os veículos cadastrados como resumo (sem itens de manutenção).
     * Usado na tela inicial de seleção de veículo.
     */
    public List<CarroResumoDTO> buscarTodos() {
        return carroRepositorio.findAll()
                .stream()
                .map(this::converterParaResumo)
                .collect(Collectors.toList());
    }

    /**
     * Retorna os detalhes completos de um veículo, incluindo todos os itens de manutenção.
     *
     * @param id ID do carro
     * @throws RuntimeException se não encontrado (convertido em HTTP 404 pelo TratadorExcecoes)
     */
    public CarroDetalheDTO buscarPorId(Long id) {
        Carro carro = carroRepositorio.findById(id)
                .orElseThrow(() -> new RuntimeException("Carro não encontrado com ID: " + id));
        return converterParaDetalhe(carro);
    }

    /**
     * Filtra veículos com parâmetros opcionais de marca, modelo e ano.
     * Qualquer parâmetro nulo é ignorado no filtro.
     *
     * @param marca  nome da marca (opcional)
     * @param modelo nome do modelo (opcional)
     * @param ano    ano do veículo (opcional)
     */
    public List<CarroResumoDTO> buscarComFiltros(String marca, String modelo, Integer ano) {
        List<Carro> carros;

        if (marca != null && modelo != null) {
            carros = carroRepositorio.buscarPorMarcaEModelo(marca, modelo);
        } else if (marca != null) {
            carros = carroRepositorio.buscarPorMarca(marca);
        } else if (modelo != null) {
            carros = carroRepositorio.buscarPorModelo(modelo);
        } else if (ano != null) {
            carros = carroRepositorio.findByAno(ano);
        } else {
            carros = carroRepositorio.findAll();
        }

        return carros.stream()
                .map(this::converterParaResumo)
                .collect(Collectors.toList());
    }

    /**
     * Busca veículos por termo livre em marca ou modelo (case-insensitive).
     *
     * @param termo palavra-chave (ex.: "Toyota", "Civic", "Honda")
     */
    public List<CarroResumoDTO> buscar(String termo) {
        if (termo == null || termo.isBlank()) {
            return buscarTodos();
        }
        return carroRepositorio.buscarPorTermo(termo.trim())
                .stream()
                .map(this::converterParaResumo)
                .collect(Collectors.toList());
    }

    /**
     * Retorna um item de manutenção específico de um veículo pelo tipo.
     *
     * @param carroId ID do carro
     * @param tipo    tipo do item (ex.: "filtro", "bateria", "ar_condicionado")
     * @throws RuntimeException se o item não for encontrado
     */
    public ItemManutencaoDTO buscarItemPorTipo(Long carroId, String tipo) {
        ItemManutencao item = itemRepositorio.findByCarroIdAndTipo(carroId, tipo)
                .orElseThrow(() -> new RuntimeException(
                        "Item '" + tipo + "' não encontrado para o carro ID: " + carroId));
        return converterItemParaDTO(item);
    }

    // -------------------------
    // Métodos de conversão privados
    // -------------------------

    /** Converte Carro → CarroResumoDTO (sem itens de manutenção) */
    private CarroResumoDTO converterParaResumo(Carro carro) {
        return new CarroResumoDTO(
                carro.getId(),
                carro.getMarca(),
                carro.getModelo(),
                carro.getAno(),
                carro.getVersao(),
                carro.getItensManutencao().size()
        );
    }

    /** Converte Carro → CarroDetalheDTO (com todos os itens de manutenção) */
    private CarroDetalheDTO converterParaDetalhe(Carro carro) {
        List<ItemManutencaoDTO> itens = carro.getItensManutencao()
                .stream()
                .map(this::converterItemParaDTO)
                .collect(Collectors.toList());

        return new CarroDetalheDTO(
                carro.getId(),
                carro.getMarca(),
                carro.getModelo(),
                carro.getAno(),
                carro.getVersao(),
                itens
        );
    }

    /** Converte ItemManutencao → ItemManutencaoDTO */
    private ItemManutencaoDTO converterItemParaDTO(ItemManutencao item) {
        return new ItemManutencaoDTO(
                item.getId(),
                item.getTipo(),
                item.getDescricao(),
                item.getUrlVideo(),
                item.getUrlProduto()
        );
    }
}
