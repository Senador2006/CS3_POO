# Motiva — Persistência Oracle (Sprint 3)

Evolução do sistema da **Sprint 2** com JDBC: as regras de priorização continuam as mesmas, mas agora equipes, trechos, intervenções e o histórico de relatórios ficam no Oracle da FIAP.

## Objetivo da Sprint

- Mapear as classes da Sprint 1/2 para tabelas Oracle
- Implementar DAOs com o padrão CRUD (`inserir`, `buscarPorId`, `listarTodas`, `atualizar`, `deletar`)
- Persistir o relatório gerado pelo `GeradorRelatorio`

## Requisitos

- **JDK 17** ou superior (os scripts encontram sozinhos o JDK do IntelliJ em `%USERPROFILE%\.jdks`, mesmo sem `javac` no PATH)
- Driver **ojdbc17.jar** (já está em `lib/`)
- Acesso ao Oracle da FIAP (`oracle.fiap.com.br:1521:ORCL`) — laboratório ou VPN

## Estrutura

```
CS3_POO/
├── seu-script-criacao.sql      → tabelas e sequences
├── seu-script-dados.sql        → dados de teste (CatalogoRodovias)
├── lib/ojdbc17.jar
├── src/
│   ├── db/                     → ConexaoBD (singleton)
│   ├── dao/                    → DAOs + SQL em constantes
│   ├── model/                  → records persistidos
│   ├── service/GeradorRelatorio.java
│   ├── main/Main.java          → demonstração CRUD + relatório
│   └── br/com/motiva/          → domínio da Sprint 2
└── test/                       → testes da Sprint 2
```

### De classes para tabelas

| Classe (Sprint 1/2) | Tabela |
|---|---|
| `EquipeManutencao` (nova entidade operacional) | `T_MOTIVA_EQUIPE_MANUTENCAO` |
| `TrechoRodovia` / `TrechoRodoviaMonitorado` | `T_MOTIVA_TRECHO_RODOVIA` |
| `IntervencaoOperacional` (Rocada/Pulverização) | `T_MOTIVA_INTERVENCAO` |
| `RelatorioPrioridade` | `T_MOTIVA_RELATORIO_PRIORIDADE` |

## 1. Banco de dados

No SQL Developer / SQL*Plus do laboratório, conecte com o RM do grupo e execute **nesta ordem**:

1. `seu-script-criacao.sql`
2. `seu-script-dados.sql`

O script de criação é reexecutável: apaga tabelas/sequences se já existirem.

O `Main` também executa esses scripts automaticamente na primeira conexão, se as tabelas ainda não existirem.

## 2. Credenciais

`ConexaoBD` já aponta para o Oracle da FIAP. Se o RM/senha do grupo for outro, altere as constantes ou use variáveis de ambiente:

```bat
set MOTIVA_ORACLE_USER=RMXXXXXX
set MOTIVA_ORACLE_PASSWORD=sua_senha
```

URL padrão: `jdbc:oracle:thin:@oracle.fiap.com.br:1521:ORCL`

## 3. Compilar e executar

No PowerShell (use o prefixo `.\`):

```powershell
.\compilar.ps1
.\executar.ps1
```

Ou um comando só:

```powershell
.\rodar.ps1
```

Os `.bat` chamam esses mesmos scripts.

Manual:

```bat
dir /s /b src\*.java test\*.java > sources.txt
javac -encoding UTF-8 -cp lib\ojdbc17.jar -d bin @sources.txt
java -cp bin;lib\ojdbc17.jar main.Main
java -cp bin;lib\ojdbc17.jar br.com.motiva.TestSuite
```

Menu interativo da Sprint 2 (sem banco):

```bat
java -cp bin;lib\ojdbc17.jar br.com.motiva.Main
java -cp bin;lib\ojdbc17.jar br.com.motiva.Main --demo
```

### IntelliJ / Cursor

1. Abra a pasta `CS3_POO`
2. Marque `src` como Source Root e `test` como Test Root
3. Adicione `lib/ojdbc17.jar` ao classpath
4. Execute `main.Main`

## O que o Main demonstra

1. Conexão singleton (`ConexaoBD.getInstancia().conectar()`)
2. CRUD completo de `EquipeManutencao`
3. CRUD de `TrechoRodovia`
4. CRUD de `IntervencaoOperacional`
5. Relatório no console (motor da Sprint 2) **e** gravação em `T_MOTIVA_RELATORIO_PRIORIDADE`
6. Listagem do histórico
7. Encerramento da conexão

## Classificação persistida

O console continua usando CRÍTICO / ALTO / MODERADO da Sprint 2. No banco, o histórico agrega:

| Faixa | Coluna |
|---|---|
| Score ≥ 80 | `QT_URGENTE` |
| Score ≥ 60 (CRÍTICO) | `QT_CRITICO` |
| Demais trechos acima de 1,20 m | `QT_ATENCAO` |
| Trechos abaixo do limite | `QT_NORMAL` |

## Problemas comuns

| Problema | Solução |
|---|---|
| Driver not found | `lib/ojdbc17.jar` precisa estar no classpath |
| ORA-01017 | Confira RM/senha em `ConexaoBD` |
| ORA-00942 | Rode `seu-script-criacao.sql` antes do `Main` |
| ORA-02292 | Apague a intervenção (filha) antes do trecho/equipe |
| Connection closed | Chame `conectar()` antes das operações |

## Autor

Projeto acadêmico — FIAP, 2º ano, POO — Challenge Sprint 3.
