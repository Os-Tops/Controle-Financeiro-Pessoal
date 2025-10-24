# Sistema de Controle Financeiro Pessoal

## 1. Visão Geral

O objetivo deste sistema é permitir que o usuário realize o **controle completo das suas finanças pessoais**, incluindo o cadastro de **contas bancárias e cartões de crédito**, o **lançamento de contas a pagar e a receber**, o **registro de pagamentos e recebimentos**, e a **atualização automática da movimentação bancária**.  

O sistema deve permitir ainda **consultas de extratos** em qualquer período, **classificação de despesas e receitas por centro de custo**, **vinculação a entidades geradoras (como lojas ou empregadores)**, e **transferência de valores entre contas**.  

No caso de cartões de crédito, o sistema deve gerenciar o **fechamento e pagamento de faturas**.

---

## 2. Estrutura Geral do Domínio

O domínio é composto por módulos principais:

- **Usuário** – identifica o dono dos dados financeiros;
- **Contas Bancárias** – controla saldo e movimentações;
- **Cartões de Crédito** – gerencia limite, fechamento e pagamento de faturas;
- **Lançamentos Financeiros** – representa contas a pagar e a receber;
- **Pagamentos e Recebimentos** – representam as baixas dos lançamentos;
- **Movimentação de Contas** – registra débitos e créditos bancários;
- **Centros de Custo e Entidades** – classificadores para controle gerencial;
- **Transferências** – movimentação entre contas;
- **Relatórios e Consultas** – extratos e análises por período.

---

## 3. Enumerações do Domínio

| Enumeração | Valores Possíveis | Descrição |
|-------------|-------------------|------------|
| `TipoLancamento` | PAGAR, RECEBER | Define se o lançamento é despesa ou receita |
| `StatusLancamento` | PENDENTE, BAIXADO, PARCIAL, CANCELADO | Situação atual do título |
| `MeioPagamento` | CONTA, CARTAO, DINHEIRO, PIX | Meio de quitação do título |
| `TipoTransacao` | CREDITO, DEBITO, TRANSFERENCIA | Tipo de movimentação bancária |
| `StatusFatura` | ABERTA, FECHADA, PAGA | Situação da fatura do cartão de crédito |

---

## 4. Tabela de Relacionamentos entre Classes

| Origem → Alvo | Cardinalidade | Tipo de Relação | Dono (FK) | Regra de Ciclo de Vida / Deleção | Invariantes Importantes |
|---|---:|---|---|---|---|
| **Usuario → ContaBancaria** | 1 → 0..* | Agregação | `ContaBancaria.usuario_id` | Soft delete ou bloqueio; preservar histórico | Cada conta pertence a um único usuário |
| **Usuario → CartaoCredito** | 1 → 0..* | Agregação | `CartaoCredito.usuario_id` | Mesmo comportamento de conta | O cartão pertence a apenas um usuário |
| **Usuario → Entidade** | 1 → 0..* | Agregação | `Entidade.usuario_id` | Soft delete; preservar histórico | Nome + documento únicos por usuário |
| **Usuario → CentroCusto** | 1 → 0..* | Agregação | `CentroCusto.usuario_id` | Inativar quando houver vínculos | Apenas centros do mesmo usuário podem ser usados |
| **Usuario → Lancamento** | 1 → 0..* | Agregação | `Lancamento.usuario_id` | Cancelar ao invés de deletar | Mesmo usuário em todos os vínculos |
| **ContaBancaria → MovimentoConta** | 1 → 0..* | Composição | `MovimentoConta.conta_id` | Não remover se houver histórico | Movimentos definem o saldo da conta |
| **Lancamento → Pagamento** | 1 → 0..* | Composição | `Pagamento.lancamento_id` | Estornar e recalcular status se excluído | Válido apenas se tipo = PAGAR |
| **Lancamento → Recebimento** | 1 → 0..* | Composição | `Recebimento.lancamento_id` | Estornar e recalcular status se excluído | Válido apenas se tipo = RECEBER |
| **Pagamento → ContaBancaria (origem)** | N → 1 | Associação | `Pagamento.conta_origem_id` | Conta deve estar ativa na data | Gera MovimentoConta DEBITO |
| **Recebimento → ContaBancaria (destino)** | N → 1 | Associação | `Recebimento.conta_destino_id` | Conta deve estar ativa | Gera MovimentoConta CREDITO |
| **Transferencia → ContaBancaria (origem/destino)** | 1 → 1 / 1 → 1 | Associação dupla | `conta_origem_id` / `conta_destino_id` | Transação atômica | origem ≠ destino; valor > 0 |
| **Entidade → Lancamento** | 1 → 0..* | Associação | `Lancamento.entidade_id` | Inativar em vez de excluir | Mesmo usuário nos dois lados |
| **CentroCusto → Lancamento** | 1 → 0..* | Associação | `Lancamento.centro_custo_id` | Igual acima | Opcional ou obrigatório conforme regra |
| **CartaoCredito → FaturaCartao** | 1 → 0..* | Composição | `Fatura.cartao_id` | Fatura (cartão, competência) única | Fechamento bloqueia inclusão |
| **Lancamento → CartaoCredito** | 0..* → 0..1 | Associação | `Lancamento.cartao_id` | Permitido apenas se meio = CARTAO | Fatura precisa estar ABERTA |
| **Lancamento → ContaBancaria** | 0..* → 0..1 | Associação | `Lancamento.conta_id` | Permitido se meio = CONTA/PIX/DINHEIRO | Conta deve estar ativa |

> **Composição:** o objeto filho não pode existir sem o pai.  
> **Agregação:** relação forte de pertencimento, mas com ciclo de vida independente.  
> **Associação:** vínculo lógico entre objetos de agregados diferentes.

---

## 5. Classes e Descrições

### 5.1 Usuario
- **Atributos:** id, nome, email, criadoEm  
- **Descrição:** Representa o dono dos registros financeiros.

### 5.2 ContaBancaria
- **Atributos:** id, usuario, instituição, agência, número, apelido, saldoInicial, dataSaldoInicial, ativa  
- **Descrição:** Representa uma conta bancária vinculada ao usuário.

### 5.3 CartaoCredito
- **Atributos:** id, usuario, bandeira, emissor, apelido, fechamentoFaturaDia, vencimentoFaturaDia, ativo  
- **Descrição:** Cartão de crédito utilizado para lançamentos via fatura.

### 5.4 FaturaCartao
- **Atributos:** id, cartao, competencia, dataFechamento, dataVencimento, valorTotal, status  
- **Descrição:** Fatura mensal vinculada a um cartão, com lançamentos agregados.

### 5.5 Entidade
- **Atributos:** id, usuario, nome, documento, tipo  
- **Descrição:** Identifica a origem ou destino do evento financeiro (loja, empresa, pessoa).

### 5.6 CentroCusto
- **Atributos:** id, usuario, nome, código, ativo  
- **Descrição:** Agrupa lançamentos para controle gerencial.

### 5.7 Lancamento
- **Atributos:** id, usuario, tipo, descricao, entidade, centroCusto, valor, dataCompetencia, dataVencimento, meioPagamento, contaBancaria, cartaoCredito, status, valorBaixado  
- **Descrição:** Representa um título financeiro a pagar ou a receber.

### 5.8 Pagamento
- **Atributos:** id, lancamento, dataPagamento, valorPago, contaOrigem, observacao  
- **Descrição:** Registra a quitação (total ou parcial) de um lançamento do tipo “a pagar”.

### 5.9 Recebimento
- **Atributos:** id, lancamento, dataRecebimento, valorRecebido, contaDestino, observacao  
- **Descrição:** Registra o recebimento de valores associados a lançamentos “a receber”.

### 5.10 MovimentoConta
- **Atributos:** id, conta, dataMovimento, tipo, valor, histórico, referenciaId, referenciaTipo  
- **Descrição:** Representa uma movimentação financeira que afeta o saldo da conta.

### 5.11 Transferencia
- **Atributos:** id, usuario, contaOrigem, contaDestino, data, valor, observacao  
- **Descrição:** Transferência de valores entre contas bancárias do mesmo usuário.

---

## 6. Regras de Negócio

1. **Baixas de lançamentos**
   - Atualizar valor baixado e status.
   - Criar automaticamente `MovimentoConta` associado.
2. **Movimentações de conta**
   - Pagamento → débito.
   - Recebimento → crédito.
   - Transferência → débito + crédito (transação única).
3. **Cartões de crédito**
   - Lançamentos entram na fatura vigente.
   - Fechamento impede novos lançamentos.
   - Pagamento gera débito na conta bancária.
4. **Extrato**
   - Consulta por período, com saldo inicial e final.
5. **Validações**
   - Proibir transferências com mesma conta.
   - Contas inativas não podem movimentar.
6. **Auditoria**
   - Todos os registros possuem `criadoEm` e `atualizadoEm`.

---

## 7. Estrutura de API (RESTful)

### 7.1 Contas Bancárias
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| POST | `/api/v1/contas` | Cria nova conta bancária |
| GET | `/api/v1/contas` | Lista todas as contas do usuário |
| GET | `/api/v1/contas/{id}` | Detalha informações da conta (instituição, saldo inicial, status, etc.) |
| PUT | `/api/v1/contas/{id}` | Atualiza dados cadastrais da conta |
| DELETE | `/api/v1/contas/{id}` | Inativa a conta, preservando o histórico de movimentos |
| GET | `/api/v1/contas/{id}/extrato?inicio=YYYY-MM-DD&fim=YYYY-MM-DD&modo=contabil|projetado` | Gera o extrato da conta para o período informado, **baseado nos registros de `MovimentoConta`** |

#### 🧮 Fonte dos dados do extrato
O extrato é **calculado dinamicamente** a partir de:
- **Saldo inicial:** `ContaBancaria.saldoInicial` ajustado até o dia anterior ao período (`inicio`) somando todos os movimentos prévios (`MovimentoConta`).
- **Movimentos do período:** registros em `MovimentoConta` com `conta_id` correspondente e `dataMovimento` entre `inicio` e `fim`.
- **Saldo final:** saldo inicial + soma dos movimentos do período (créditos positivos e débitos negativos).

#### 🧾 Exemplo de resposta
```json
{
  "contaId": 1,
  "periodo": { "inicio": "2025-10-01", "fim": "2025-10-31" },
  "saldoInicialPeriodo": 2530.75,
  "movimentos": [
    {
      "data": "2025-10-03",
      "tipo": "DEBITO",
      "valor": 120.50,
      "historico": "Pagamento Conta de Luz",
      "referencia": { "tipo": "PAGAMENTO", "id": 987 }
    },
    {
      "data": "2025-10-05",
      "tipo": "CREDITO",
      "valor": 3500.00,
      "historico": "Recebimento salário",
      "referencia": { "tipo": "RECEBIMENTO", "id": 654 }
    }
  ],
  "saldoFinalPeriodo": 5910.25
}
```

#### ⚙️ Observações
- Os movimentos são provenientes de **pagamentos**, **recebimentos**, **transferências** e **pagamentos de fatura de cartão**.  
- O endpoint não lê dados diretamente de `ContaBancaria`, mas da entidade **`MovimentoConta`**, garantindo rastreabilidade e conciliação contábil.  
- É possível estender o endpoint com o parâmetro `modo=projetado` para incluir lançamentos futuros ainda não baixados.

#### 📈 Extrato Projetado
O **extrato projetado** é uma extensão do extrato contábil tradicional. Ele permite visualizar não apenas os movimentos já efetivados na conta (`MovimentoConta`), mas também **lançamentos futuros previstos**, provenientes de títulos **a pagar** e **a receber** que ainda não foram baixados.

##### Finalidade
- Antecipar **fluxo de caixa futuro**;
- Facilitar **planejamento financeiro** e **decisões de investimento ou pagamento**;
- Identificar períodos com **saldo projetado negativo**.

##### Fonte dos dados
- `MovimentoConta`: registros contábeis efetivos (créditos e débitos confirmados);
- `Lancamento`: registros pendentes (`status = PENDENTE`) cuja `dataVencimento` está dentro do intervalo consultado.

##### Regras
- Lançamentos com **meioPagamento = CARTAO** são ignorados, pois já integram faturas futuras;
- O saldo inicial considera apenas o saldo contábil até o início do período;
- O saldo final projetado = saldo inicial + soma(movimentos efetivos + lançamentos projetados);
- O parâmetro `modo=projetado` **não altera o extrato contábil armazenado** — é apenas uma **visualização preditiva**.

##### Exemplo de chamada
```
GET /api/v1/contas/12/extrato?inicio=2025-10-01&fim=2025-10-31&modo=projetado
```

##### Exemplo de resposta simplificada
```json
{
  "contaId": 12,
  "modo": "projetado",
  "saldoInicialPeriodo": 2530.75,
  "saldoFinalContabil": 5910.25,
  "saldoFinalProjetado": 4720.25,
  "movimentosEfetivos": 18,
  "lancamentosPendentes": 5
}
```



### 7.2 Cartões e Faturas
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| POST | `/api/v1/cartoes` | Cadastra cartão |
| GET | `/api/v1/cartoes` | Lista cartões |
| GET | `/api/v1/cartoes/{id}/faturas` | Lista faturas |
| POST | `/api/v1/cartoes/{id}/faturas/fechamento` | Fecha fatura |
| POST | `/api/v1/cartoes/{id}/faturas/{faturaId}/pagar` | Paga fatura |

### 7.3 Centros de Custo
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| POST | `/api/v1/centros-custo` | Cria novo |
| GET | `/api/v1/centros-custo?ativo=true` | Lista ativos |
| PUT | `/api/v1/centros-custo/{id}` | Atualiza |
| DELETE | `/api/v1/centros-custo/{id}` | Inativa |

### 7.4 Entidades
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| POST | `/api/v1/entidades` | Cria entidade |
| GET | `/api/v1/entidades?nome=` | Busca por nome |
| PUT | `/api/v1/entidades/{id}` | Atualiza |
| DELETE | `/api/v1/entidades/{id}` | Exclui/Inativa |

### 7.5 Lançamentos
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| POST | `/api/v1/lancamentos` | Cria lançamento |
| GET | `/api/v1/lancamentos?tipo&status&inicio&fim` | Filtra lançamentos |
| GET | `/api/v1/lancamentos/{id}` | Detalha lançamento |
| PUT | `/api/v1/lancamentos/{id}` | Atualiza |
| POST | `/api/v1/lancamentos/{id}/cancelar` | Cancela título |

### 7.6 Pagamentos e Recebimentos
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| POST | `/api/v1/lancamentos/{id}/pagamentos` | Registra pagamento |
| GET | `/api/v1/lancamentos/{id}/pagamentos` | Lista pagamentos |
| POST | `/api/v1/lancamentos/{id}/recebimentos` | Registra recebimento |
| GET | `/api/v1/lancamentos/{id}/recebimentos` | Lista recebimentos |

### 7.7 Transferências
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| POST | `/api/v1/transferencias` | Registra transferência |
| GET | `/api/v1/transferencias?inicio&fim&contaId` | Lista transferências |

### 7.8 Relatórios
| Método | Endpoint | Descrição |
|---------|-----------|-----------|
| GET | `/api/v1/relatorios/extrato?contaId&inicio&fim` | Gera extrato |
| GET | `/api/v1/relatorios/contas-a-pagar` | Contas a pagar |
| GET | `/api/v1/relatorios/contas-a-receber` | Contas a receber |
| GET | `/api/v1/relatorios/posicao-geral` | Consolidação geral |

---

## 8. Considerações Técnicas

- **Framework:** Spring Boot (Web, Data JPA, Validation, Security)
- **Banco:** PostgreSQL
- **Transações:** `@Transactional` em operações críticas
- **Paginação:** `Pageable` nas listagens
- **Segurança:** Escopo por `usuarioId`
- **Auditoria:** `@PrePersist`, `@PreUpdate`
- **Cascades:** Somente em composições (`Conta→Movimentos`, `Lancamento→Pagamentos/Recebimentos`, `Cartao→Faturas`)

## 💡 Dicas de Implementação (Integridade e JPA)

### 🔐 FKs coerentes por usuário
- Valide na camada **Service** para garantir integridade:
  ```java
  assert conta.getUsuario().getId().equals(lancamento.getUsuario().getId());
  ```

---

### ⚙️ Índices e Unicidades
- **fatura** (`cartao_id`, `competencia`) → `UNIQUE`
- **movimento** (`conta_id`, `data_movimento`) → índice para consultas de extrato
- **lancamento** (`usuario_id`, `data_vencimento`) → índice para filtros por período

---

### 🧩 Cascades
Utilize `@OneToMany(mappedBy = ..., cascade = CascadeType.ALL, orphanRemoval = true)` **somente** nos relacionamentos de **composição**:
- `Lancamento → Pagamentos / Recebimentos`
- `ContaBancaria → Movimentos`
- `CartaoCredito → Faturas`

---

### 🔄 Transações
As operações de:
- **Pagamento**
- **Recebimento**
- **Transferência**

devem envolver a criação/remoção dos movimentos e a atualização de status dentro da **mesma transação**, utilizando:

```java
@Transactional
public void processarPagamento(PagamentoDTO dto) { ... }
```

---

### ⏱️ Consistência Temporal
- Bloquear lançamentos em **faturas com status FECHADA**.  
- Impedir movimentações em **contas inativas** na data do evento.
