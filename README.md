# FullStack_PWEB
Projeto valendo 50% da disciplina Programação Web, com o objetivo de desenvolver, individualmente, um sistema baseando-se em um sistema legado.

**Estudante**: Yago França Santos — RA: 42386  
**Instituição**: ESEG — Faculdade do Grupo Etapa (Engenharia de Computação — 2S2026)  
**Professor**: Programação Web / Projeto Integrador Full Stack  

---

## 📌 Visão Geral do Projeto

Modernização e reengenharia de um sistema legado de Recursos Humanos baseado originalmente em dados tabulares de planilha (`17_recursos_humanos.xlsx`). O projeto substitui o modelo desnormalizado anterior por uma arquitetura corporativa multicamadas, orientada a objetos e baseada no ecossistema **Spring Boot**, com persistência relacional via **Jakarta Persistence (JPA)** e **Hibernate**, banco de dados **H2**, regras de negócio transacionais, controladores RESTful e interface web responsiva adaptada com **Bootstrap** e a paleta do **Color Hunt**.

---

## 🗂️ Estrutura das Entregas

```
FullStack_PWEB/
│
├── 1Entrega/                       # 1ª Entrega (25/10/2026 - Fases 01 a 04)
│   ├── Matriz de rastreabilidade — 1ª entrega.docx
│   ├── vLegadoOficial.asta         # Modelo Astah do sistema legado
│   └── ReduzidoOficial1Entrega.asta# Modelo Astah evoluído e normalizado
│
├── 2Entrega/                       # 2ª Entrega (06/11/2026 - Fases 05 a 08)
│   ├── Matriz de rastreabilidade — 2ª entrega.docx
│   └── Relatório Técnico — 2ª entrega.docx
│
├── src/main/java/com/ProjetoP2/Yago_42386/
│   ├── model/                      # 10 Entidades JPA mapeadas (Fases 05/06)
│   ├── repository/                 # Repositórios Spring Data JPA (Fase 06)
│   ├── service/                    # Regras de Negócio e Transição de Status (Fase 07)
│   └── controller/                 # Endpoints RESTful e Tratamento de Erros (Fase 08)
│
├── src/main/resources/
│   ├── application.properties      # Configuração H2, Schema APP, DDL e Logs
│   ├── data.sql                    # 900 registros reais importados da planilha XLSX
│   ├── META-INF/persistence.xml    # Especificação Jakarta Persistence 3.0
│   ├── static/css/style.css        # CSS corporativo com paleta Color Hunt e badges dinâmicos
│   └── templates/index.html        # Interface Web responsiva integrada
│
├── database/                       # Banco de dados H2 persistente em disco (RH.mv.db)
├── pom.xml                         # Dependências do projeto Maven
├── rodar.bat                       # Script automatizado de compilação e execução
└── paleta_colorhunt.png            # Paleta de cores oficial utilizada na estilização
```

---

## 🎨 Paleta de Cores (Color Hunt)

A estilização visual da aplicação obedece rigorosamente à paleta corporativa extraída de `paleta_colorhunt.png`:

| Cor | Hexadecimal | Aplicação no Sistema |
| :--- | :---: | :--- |
| **Dark Forest** | `#092328` | Navbar, cabeçalhos de cards e títulos principais |
| **Deep Pine** | `#12544F` | Botões primários, bordas ativas e estados de foco |
| **Forest Green** | `#2A835F` | Botões de novo registro e ações de sucesso |
| **Soft Sage** | `#8BBB92` | Sublinhados de abas, hover de tabelas e rolagem |

### Badges e Indicadores de Status do Colaborador:
* **`Ativo`**: Fundo `#d1fae5`, texto `#15803d`, indicador pontual `●` verde (`#16a34a`).
* **`Afastado`**: Fundo `#fef3c7`, texto `#b45309`, indicador pontual `●` âmbar (`#d97706`).
* **`Férias`**: Fundo `#e0f2fe`, texto `#0369a1`, indicador pontual `●` azul (`#0284c7`).
* **`Desligado`**: Fundo `#fee2e2`, texto `#b91c1c`, indicador pontual `●` vermelho (`#dc2626`).

---

## ⚙️ Regras de Negócio Principais (Fase 07)

1. **Validação de Transição de Status do Colaborador (Itens 2.3 e 7 do Edital)**:
   * Status admissíveis: `Ativo`, `Afastado`, `Férias` e `Desligado`.
   * **Máquina de Estados**: O status **`Desligado`** é considerado um estado **terminal** (rescisão homologada). Qualquer tentativa de alterar um colaborador já desligado para outro status é bloqueada pelo `colaboradorService`, disparando `IllegalArgumentException` e retornando **HTTP 400 Bad Request**.
2. **Cálculo Automatizado de Folha de Pagamento**:
   * O valor de `salario_liquido` é sempre calculado e verificado pelo backend (`total_vencimentos - total_descontos`), evitando discrepâncias contábeis no banco de dados.
3. **Resolução de Integridade Referencial**:
   * O vínculo de chaves estrangeiras entre Colaborador, Cargo, Unidade e Departamento é tratado na camada de serviço.

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* **Java**: JDK 17 ou JDK 21 instalado e configurado no `JAVA_HOME`.
* **Maven**: Maven 3.8+ instalado (ou execução via script automático).

### 1. Inicialização Rápida no Windows
Na pasta raiz do projeto, execute:
```cmd
.\rodar.bat
```
O script compila o projeto e inicia o servidor Spring Boot na porta **8080**.

### 2. Acesso à Aplicação
* **Interface Web do Sistema**: [http://localhost:8080/](http://localhost:8080/)
* **Console do Banco H2**: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
  * *JDBC URL*: `jdbc:h2:file:./database/RH`
  * *User*: `sa`
  * *Password*: *(em branco)*

---

## 📋 Endpoints REST Disponíveis (CRUD Completo)

| Recurso | Rota Base | Métodos HTTP |
| :--- | :--- | :--- |
| **Colaboradores** | `/api/colaboradores` | `GET`, `POST`, `PUT`, `DELETE` |
| **Departamentos** | `/api/departamentos` | `GET`, `POST`, `PUT`, `DELETE` |
| **Cargos** | `/api/cargos` | `GET`, `POST`, `PUT`, `DELETE` |
| **Unidades** | `/api/unidades` | `GET`, `POST`, `PUT`, `DELETE` |
| **Benefícios** | `/api/beneficios` | `GET`, `POST`, `PUT`, `DELETE` |
| **Folhas de Pagamento** | `/api/folhas-pagamento` | `GET`, `POST`, `PUT`, `DELETE` |
| **Perfis de Acesso** | `/api/perfis` | `GET`, `POST`, `PUT`, `DELETE` |
| **Usuários** | `/api/usuarios` | `GET`, `POST`, `PUT`, `DELETE` |
| **Colaborador x Benefício** | `/api/colaboradores-beneficios` | `GET`, `POST`, `DELETE` |
| **Usuário x Perfil** | `/api/usuarios-perfis` | `GET`, `POST`, `DELETE` |

