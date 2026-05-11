package com.mechdiy.configuracao;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mechdiy.modelo.Carro;
import com.mechdiy.modelo.ItemManutencao;
import com.mechdiy.repositorio.CarroRepositorio;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * Lê o arquivo dados/carros.json e popula o banco H2 na inicialização.
 *
 * Fluxo:
 *   1. Lê o arquivo JSON de src/main/resources/dados/carros.json
 *   2. Itera sobre cada "carro_XXX" no objeto "carros"
 *   3. Para cada carro, cria a entidade Carro e seus ItemManutencao
 *   4. Salva tudo no banco via CarroRepositorio (cascade propaga os itens)
 *
 * ObjectMapper é a classe Jackson que converte JSON em objetos Java.
 * ClassPathResource localiza o arquivo dentro de src/main/resources.
 */
@Component
public class InicializadorDados implements CommandLineRunner {

    private final CarroRepositorio carroRepositorio;

    /**
     * ObjectMapper é thread-safe e reutilizável — instanciado uma vez aqui.
     * É a classe principal do Jackson para ler/escrever JSON.
     */
    private final ObjectMapper objectMapper = new ObjectMapper();

    public InicializadorDados(CarroRepositorio carroRepositorio) {
        this.carroRepositorio = carroRepositorio;
    }

    @Override
    public void run(String... args) throws Exception {
        // Evita reinserir dados se o banco já foi populado
        if (carroRepositorio.count() > 0) {
            return;
        }

        System.out.println("=== MechDIY: Carregando dados do JSON ===");

        // Localiza o arquivo JSON dentro de src/main/resources
        ClassPathResource recurso = new ClassPathResource("dados/carros.json");
        InputStream fluxo = recurso.getInputStream();

        // Faz o parse do JSON — raiz tem um objeto "carros"
        JsonNode raiz = objectMapper.readTree(fluxo);
        JsonNode objetoCarros = raiz.get("carros");

        if (objetoCarros == null) {
            System.err.println("=== MechDIY: Chave 'carros' não encontrada no JSON ===");
            return;
        }

        int totalCarros = 0;

        // Itera sobre cada chave do objeto "carros" (carro_001, carro_002, ...)
        Iterator<Map.Entry<String, JsonNode>> entradas = objetoCarros.fields();
        while (entradas.hasNext()) {
            Map.Entry<String, JsonNode> entrada = entradas.next();

            String chaveJson = entrada.getKey();      // ex.: "carro_001"
            JsonNode dadosCarro = entrada.getValue(); // objeto com marca, modelo, etc.

            // Verifica se este carro já existe no banco (evita duplicata em reruns)
            if (carroRepositorio.findByChave(chaveJson).isPresent()) {
                continue;
            }

            // Lê os campos básicos do carro
            String marca  = dadosCarro.get("marca").asText();
            String modelo = dadosCarro.get("modelo").asText();
            int    ano    = dadosCarro.get("ano").asInt();
            String versao = dadosCarro.has("versao") ? dadosCarro.get("versao").asText() : null;

            // Cria a entidade Carro
            Carro carro = new Carro(chaveJson, marca, modelo, ano, versao);

            // Lê os itens de manutenção de "detalhes_opcoes"
            JsonNode detalhesOpcoes = dadosCarro.get("detalhes_opcoes");
            if (detalhesOpcoes != null) {
                // Itera sobre cada tipo de manutenção (filtro, bateria, ar_condicionado, ...)
                Iterator<Map.Entry<String, JsonNode>> opcoes = detalhesOpcoes.fields();
                while (opcoes.hasNext()) {
                    Map.Entry<String, JsonNode> opcao = opcoes.next();

                    String     tipo       = opcao.getKey();   // ex.: "filtro"
                    JsonNode   dadosItem  = opcao.getValue(); // objeto com descricao, arquivo_mp4, link

                    String descricao  = dadosItem.has("descricao")    ? dadosItem.get("descricao").asText()    : "";
                    String urlVideo   = dadosItem.has("arquivo_mp4")  ? dadosItem.get("arquivo_mp4").asText()  : null;
                    String urlProduto = dadosItem.has("link")         ? dadosItem.get("link").asText()         : null;

                    // Cria o item e associa ao carro
                    ItemManutencao item = new ItemManutencao(carro, tipo, descricao, urlVideo, urlProduto);
                    carro.getItensManutencao().add(item);
                }
            }

            // Salva o carro — o cascade ALL propaga o save para os itens de manutenção
            carroRepositorio.save(carro);
            totalCarros++;
        }

        System.out.println("=== MechDIY: " + totalCarros + " carros carregados do JSON ===");
    }
}
