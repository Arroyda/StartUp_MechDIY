# MechDIY — Sistema de Tutoriais de Manutenção Automotiva

MechDIY é uma plataforma que orienta o usuário a realizar manutenções preventivas e simples no próprio veículo. O sistema exibe, para cada carro cadastrado, tutoriais em vídeo e links de produtos recomendados para itens como filtro de ar, bateria e ar-condicionado.

---

## Tecnologias

| Camada | Tecnologia |
|---|---|
| Frontend | React 18 + Vite 5 |
| Backend | Spring Boot 3.2.5 |
| Linguagem | Java 21 |
| Banco de dados | H2 (em memória, desenvolvimento) |
| ORM | Spring Data JPA / Hibernate |
| Documentação | Swagger UI (springdoc-openapi 2.5.0) |
| Build backend | Maven (via Maven Wrapper) |
| Deploy frontend | Netlify |
| Deploy backend | Docker |

---

## Estrutura do projeto

```
MechDIY/
├── frontend/                              # React + Vite
│   ├── src/
│   │   ├── App.jsx                        # Listagem, busca e toggle de tema
│   │   ├── components/
│   │   │   └── CarModal.jsx               # Modal com tutoriais de manutenção
│   │   ├── services/
│   │   │   └── api.js                     # Cliente HTTP para os endpoints
│   │   ├── hooks/
│   │   │   └── useTheme.js                # Modo claro / escuro
│   │   └── main.jsx                       # Entry point React
│   ├── package.json
│   ├── vite.config.js
│   └── .env.production
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
│   ├── src/main/resources/
│   │   ├── application.properties
│   │   └── dados/carros.json             # Fonte de dados dos veículos
│   └── Dockerfile
├── netlify.toml                           # Config de deploy do frontend
└── README.md
```

---

## Como executar

### Pré-requisitos

- Java 21+
- Node.js 18+ e npm
- `JAVA_HOME` configurado

### Backend

```powershell
cd backend

# Configurar o JAVA_HOME (ajuste o caminho conforme sua instalação)
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-21"

# Subir o servidor
.\mvnw.cmd spring-boot:run
```

Servidor disponível em `http://localhost:8080`.

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Interface disponível em `http://localhost:5173`.

### Com Docker (backend)

```bash
cd backend
docker build -t mechdiy-backend .
docker run -p 8080:8080 mechdiy-backend
```

---

## Funcionalidades do frontend

- Listagem de veículos cadastrados com busca em tempo real (debounce 300 ms)
- Filtro por marca, modelo e ano
- Modal com tutoriais em vídeo para cada item de manutenção
- Links diretos para compra dos produtos recomendados
- Alternância entre tema claro e escuro

---

## Endpoints da API

Base URL: `http://localhost:8080/api`

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
      "urlVideo": "https://exemplo.com/videos/filtro.mp4",
      "urlProduto": "https://exemplo.com/produto/filtro"
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
  "urlVideo": "https://exemplo.com/videos/bateria.mp4",
  "urlProduto": "https://exemplo.com/produto/bateria"
}
```

---

## Fonte de dados

Todos os veículos e tutoriais são carregados a partir de `backend/src/main/resources/dados/carros.json` na inicialização. Não há integração com APIs externas.

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
        "bateria": { "..." : "..." },
        "ar_condicionado": { "..." : "..." }
      }
    }
  }
}
```

Para **adicionar um novo carro**, basta inserir uma nova entrada no JSON seguindo o padrão acima. A aplicação carrega os dados automaticamente ao iniciar.

Tipos de manutenção suportados: `filtro`, `bateria`, `ar_condicionado`.

---

## URLs úteis (desenvolvimento)

| URL | Descrição |
|---|---|
| `http://localhost:5173` | Frontend React |
| `http://localhost:8080/swagger-ui.html` | Interface visual para testar os endpoints |
| `http://localhost:8080/v3/api-docs` | Especificação OpenAPI em JSON |
| `http://localhost:8080/h2-console` | Console H2 (JDBC URL: `jdbc:h2:mem:mechdiydb`) |

---

## Banco de dados

O projeto usa **H2** (banco em memória) durante o desenvolvimento. Os dados são recriados a cada inicialização a partir do `carros.json`.

Para migrar para produção com PostgreSQL ou MySQL, altere `application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mechdiy
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
```

E adicione o driver correspondente no `pom.xml`.

---

## CORS

O backend aceita requisições dos seguintes endereços:

- `http://localhost:5173` (Vite / React)
- `http://localhost:3000` (Create React App / Next.js)
