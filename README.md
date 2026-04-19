# MechDIY — Sistema de Tutoriais de Manutenção Automotiva

> Projeto universitário. O frontend ainda será implementado.

MechDIY é uma plataforma que orienta o usuário a realizar manutenções preventivas e simples no próprio veículo. O sistema exibe, para cada carro cadastrado, tutoriais em vídeo e links de produtos recomendados para itens como filtro, bateria e ar-condicionado.

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Backend | Spring Boot 3.2.5 |
| Linguagem | Java 21 |
| Banco de dados | H2 (em memória, desenvolvimento) |
| ORM | Spring Data JPA / Hibernate |
| Documentação | Swagger UI (springdoc-openapi 2.5.0) |
| Build | Maven (via Maven Wrapper) |
| Frontend | **A implementar** |

---

## Estrutura do projeto

```
MechDIY/
├── backend/
│   ├── src/main/java/com/mechdiy/
│   │   ├── MechDiyAplicacao.java          # Ponto de entrada da aplicação
│   │   ├── configuracao/
│   │   │   ├── ConfiguracaoCors.java      # Libera o frontend para consumir a API
│   │   │   └── InicializadorDados.java    # Carrega o JSON no banco ao iniciar
│   │   ├── controlador/
│   │   │   ├── CarroControlador.java      # Endpoints REST /api/carros
│   │   │   └── TratadorExcecoes.java      # Respostas de erro padronizadas
│   │   ├── dto/
│   │   │   ├── CarroResumoDTO.java        # Dados resumidos (listagem)
│   │   │   ├── CarroDetalheDTO.java       # Dados completos (detalhe do carro)
│   │   │   └── ItemManutencaoDTO.java     # Item individual de manutenção
│   │   ├── modelo/
│   │   │   ├── Carro.java                 # Entidade JPA — tabela carros
│   │   │   └── ItemManutencao.java        # Entidade JPA — tabela itens_manutencao
│   │   ├── repositorio/
│   │   │   ├── CarroRepositorio.java      # Queries de carro (marca, modelo, busca)
│   │   │   └── ItemManutencaoRepositorio.java
│   │   └── servico/
│   │       └── CarroServico.java          # Lógica de negócio e conversão para DTOs
│   └── src/main/resources/
│       ├── application.properties         # Configurações da aplicação
│       └── dados/carros.json             # Fonte de dados dos veículos
└── README.md
```

---

## Fonte de dados

Todos os veículos e tutoriais são carregados a partir do arquivo `src/main/resources/dados/carros.json` na inicialização da aplicação. Não há integração com APIs externas.

### Estrutura do JSON

```json
{
  "carros": {
    "carro_001": {
      "marca": "Toyota",
      "modelo": "Corolla",
      "ano": 2022,
      "versao": "XEi",
      "detalhes_opcoes": {
        "filtro": {
          "descricao": "Descrição do item",
          "arquivo_mp4": "https://url-do-video.com/video.mp4",
          "link": "https://url-do-produto.com"
        },
        "bateria": { ... },
        "ar_condicionado": { ... }
      }
    }
  }
}
```

Para **adicionar um novo carro**, basta inserir uma nova entrada no JSON seguindo o padrão acima. A aplicação carrega os dados automaticamente ao ser iniciada.

Tipos de manutenção atualmente suportados: `filtro`, `bateria`, `ar_condicionado`.

---

## Endpoints da API

Base URL: `http://localhost:8080/api`

### Carros

| Método | Endpoint | Descrição |
|---|---|---|
| `GET` | `/api/carros` | Lista todos os carros cadastrados |
| `GET` | `/api/carros?marca=Toyota` | Filtra por marca |
| `GET` | `/api/carros?marca=Honda&modelo=Civic` | Filtra por marca e modelo |
| `GET` | `/api/carros?ano=2022` | Filtra por ano |
| `GET` | `/api/carros/buscar?termo=civic` | Busca livre em marca ou modelo |
| `GET` | `/api/carros/{id}` | Retorna detalhes completos de um carro |
| `GET` | `/api/carros/{id}/manutencao/{tipo}` | Retorna um item específico de manutenção |

### Exemplos de resposta

**`GET /api/carros`**
```json
[
  {
    "id": 1,
    "marca": "Toyota",
    "modelo": "Corolla",
    "ano": 2022,
    "versao": "XEi",
    "quantidadeItens": 3
  }
]
```

**`GET /api/carros/1`**
```json
{
  "id": 1,
  "marca": "Toyota",
  "modelo": "Corolla",
  "ano": 2022,
  "versao": "XEi",
  "itensManutencao": [
    {
      "id": 1,
      "tipo": "filtro",
      "descricao": "Filtro de ar esportivo de alta performance...",
      "urlVideo": "https://exemplo.com/videos/filtro_kn.mp4",
      "urlProduto": "https://exemplo.com/produto/filtro_kn"
    }
  ]
}
```

**`GET /api/carros/1/manutencao/bateria`**
```json
{
  "id": 2,
  "tipo": "bateria",
  "descricao": "Bateria automotiva Heliar de 60Ah...",
  "urlVideo": "https://exemplo.com/videos/bateria_heliar.mp4",
  "urlProduto": "https://exemplo.com/produto/bateria_heliar"
}
```

---

## Como executar o backend

### Pré-requisitos

- Java 21+ instalado
- `JAVA_HOME` configurado

### No Windows (PowerShell)

```powershell
cd "C:\Users\varru\OneDrive\Ambiente de Trabalho\MechDIY\backend"

# Configurar o JAVA_HOME (substitua pelo caminho da sua instalação)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-25.0.2.10-hotspot"

# Subir o servidor
.\mvnw.cmd spring-boot:run
```

O servidor sobe na porta **8080**.

### URLs úteis após subir

| URL | Descrição |
|---|---|
| `http://localhost:8080/swagger-ui.html` | Interface visual para testar os endpoints |
| `http://localhost:8080/v3/api-docs` | Especificação OpenAPI em JSON |
| `http://localhost:8080/h2-console` | Console do banco H2 (JDBC URL: `jdbc:h2:mem:mechdiydb`) |

---

## CORS

O backend já está configurado para aceitar requisições dos seguintes endereços, que são os padrões de desenvolvimento dos frameworks frontend mais comuns:

- `http://localhost:5173` (Vite / React)
- `http://localhost:3000` (Create React App / Next.js)

---

## Frontend

O frontend **ainda não foi implementado**. Quando desenvolvido, deverá consumir os endpoints listados acima para:

- Exibir a lista de carros disponíveis
- Permitir busca e filtro por marca, modelo e ano
- Exibir os tutoriais em vídeo de cada item de manutenção
- Exibir o link do produto recomendado para cada item

---

## Banco de dados

O projeto usa **H2** (banco em memória) durante o desenvolvimento. Os dados são recriados a cada inicialização a partir do `carros.json`.

Para migrar para produção com PostgreSQL ou MySQL, basta alterar as configurações em `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mechdiy
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

E adicionar o driver correspondente no `pom.xml`.
