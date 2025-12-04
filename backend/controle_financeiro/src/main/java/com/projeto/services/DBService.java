package com.projeto.services;

import com.projeto.domains.Transferencia;
import com.projeto.domains.Recebimento;
import com.projeto.domains.Pagamento;
import com.projeto.domains.MovimentoConta;
import com.projeto.domains.Lancamento;
import com.projeto.domains.FaturaCartao;
import com.projeto.domains.Entidade;
import com.projeto.domains.ContaBancaria;
import com.projeto.domains.CentroCusto;
import com.projeto.domains.CartaoCredito;
import com.projeto.domains.Usuario;
import com.projeto.domains.enums.*;
import com.projeto.repositories.TransferenciaRepository;
import com.projeto.repositories.RecebimentoRepository;
import com.projeto.repositories.PagamentoRepository;
import com.projeto.repositories.MovimentoContaRepository;
import com.projeto.repositories.LancamentoRepository;
import com.projeto.repositories.FaturaCartaoRepository;
import com.projeto.repositories.EntidadeRepository;
import com.projeto.repositories.ContaBancariaRepository;
import com.projeto.repositories.CentroCustoRepository;
import com.projeto.repositories.CartaoCreditoRepository;
import com.projeto.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
@Transactional
public class DBService {

    @Autowired
    private TransferenciaRepository transferenciaRepo;

    @Autowired
    private RecebimentoRepository recebimentoRepo;

    @Autowired
    private PagamentoRepository pagamentoRepo;

    @Autowired
    private MovimentoContaRepository movimentoContaRepo;

    @Autowired
    private LancamentoRepository lancamentoRepo;

    @Autowired
    private FaturaCartaoRepository faturaCartaoRepo;

    @Autowired
    private EntidadeRepository entidadeRepo;

    @Autowired
    private ContaBancariaRepository contaBancariaRepo;

    @Autowired
    private CentroCustoRepository centroCustoRepo;

    @Autowired
    private CartaoCreditoRepository cartaoCreditoRepo;

    @Autowired
    private UsuarioRepository usuarioRepo;

    public void initDB() {

        try {
            // Criar usuários
            Usuario usuario01 = new Usuario(null, "João", "joaozinho@gameplay.com", LocalDate.of(2025, 10, 25));
            Usuario usuario02 = new Usuario(null, "Ana", "aninha@bol.com", LocalDate.of(2022, 8, 14));
            usuarioRepo.save(usuario01);
            usuarioRepo.save(usuario02);

            // Criar contas bancárias
            ContaBancaria conta1 = new ContaBancaria(null, "Banco A", 123, 456789, new BigDecimal("5000.00"), "Conta Corrente", LocalDate.of(2025, 1, 1), Status.ATIVA, usuario01);
            ContaBancaria conta2 = new ContaBancaria(null, "Banco B", 321, 987654, new BigDecimal("10000.00"), "Conta Poupança", LocalDate.of(2025, 2, 1), Status.INATIVA, usuario02);
            contaBancariaRepo.save(conta1);
            contaBancariaRepo.save(conta2);

            // Criar centro de custo
            CentroCusto centroCusto1 = new CentroCusto(null, "Marketing", 1001, usuario01, Status.ATIVA);
            CentroCusto centroCusto2 = new CentroCusto(null, "TI", 1002, usuario02, Status.INATIVA);
            centroCustoRepo.save(centroCusto1);
            centroCustoRepo.save(centroCusto2);

            // Criar transferências
            Transferencia transferencia1 = new Transferencia(null, conta1, conta2, LocalDate.of(2025, 11, 6), new BigDecimal("100.50"), "Transferência de teste");
            Transferencia transferencia2 = new Transferencia(null, conta2, conta1, LocalDate.of(2025, 11, 7), new BigDecimal("250.75"), "Outra transferência");
            transferenciaRepo.save(transferencia1);
            transferenciaRepo.save(transferencia2);

            // Criar cartões de crédito
            CartaoCredito cartaoCredito1 = new CartaoCredito(null, "Visa", "Banco A", "Cartão Ouro", LocalDate.of(2025, 1, 10), LocalDate.of(2025, 2, 10), StatusCartao.DESBLOQUEADO, usuario01);
            CartaoCredito cartaoCredito2 = new CartaoCredito(null, "Mastercard", "Banco B", "Cartão Platina", LocalDate.of(2025, 2, 10), LocalDate.of(2025, 3, 10), StatusCartao.BLOQUEADO, usuario02);
            cartaoCreditoRepo.save(cartaoCredito1);
            cartaoCreditoRepo.save(cartaoCredito2);

            // Criar faturas
            FaturaCartao faturaCartao1 = new FaturaCartao(null, LocalDate.of(2025, 10, 1), LocalDate.of(2025, 10, 20), LocalDate.of(2025, 11, 10), new BigDecimal("1200.00"), StatusFatura.ABERTA, cartaoCredito1);
            FaturaCartao faturaCartao2 = new FaturaCartao(null, LocalDate.of(2025, 11, 1), LocalDate.of(2025, 11, 20), LocalDate.of(2025, 12, 10), new BigDecimal("1500.00"), StatusFatura.PAGA, cartaoCredito2);
            faturaCartaoRepo.save(faturaCartao1);
            faturaCartaoRepo.save(faturaCartao2);

            // Criar entidades
            Entidade entidade1 = new Entidade(null, "Entidade A", "123456789", usuario01);
            Entidade entidade2 = new Entidade(null, "Entidade B", "987654321", usuario02);
            entidadeRepo.save(entidade1);
            entidadeRepo.save(entidade2);

            // Criar lançamentos
            Lancamento lancamento1 = new Lancamento(null, "Pagamento de contas", new BigDecimal("450.00"), LocalDate.of(2025, 11, 6), LocalDate.of(2025, 11, 10), new BigDecimal("450.00"), StatusLancamento.BAIXADO, MeioPagamento.CARTAO, TipoLancamento.PAGAR, usuario01, conta1, centroCusto1, entidade1, cartaoCredito1);
            Lancamento lancamento2 = new Lancamento(null, "Compra de materiais", new BigDecimal("600.00"), LocalDate.of(2025, 11, 7), LocalDate.of(2025, 11, 15), new BigDecimal("600.00"), StatusLancamento.PENDENTE, MeioPagamento.CONTA, TipoLancamento.RECEBER, usuario02, conta2, centroCusto2, entidade2, cartaoCredito2);
            lancamentoRepo.save(lancamento1);
            lancamentoRepo.save(lancamento2);

            // Criar recebimentos
            Recebimento recebimento1 = new Recebimento(null, LocalDate.of(2025, 11, 6), new BigDecimal("200.00"), "Recebimento de pagamento", conta1, lancamento1);
            Recebimento recebimento2 = new Recebimento(null, LocalDate.of(2025, 11, 7), new BigDecimal("500.00"), "Recebimento de serviço", conta2, lancamento2);
            recebimentoRepo.save(recebimento1);
            recebimentoRepo.save(recebimento2);

            // Criar pagamentos
            Pagamento pagamento1 = new Pagamento(null, LocalDate.of(2025, 11, 6), new BigDecimal("150.00"), "Pagamento de fatura", conta1, lancamento1);
            Pagamento pagamento2 = new Pagamento(null, LocalDate.of(2025, 11, 7), new BigDecimal("350.00"), "Pagamento de fornecedor", conta2, lancamento2);
            pagamentoRepo.save(pagamento1);
            pagamentoRepo.save(pagamento2);

            // Criar movimentos de conta
            MovimentoConta movimento1 = new MovimentoConta(null, LocalDate.of(2025, 11, 6), new BigDecimal("300.00"), "Movimento bancário", TipoTransacao.CREDITO, conta1);
            MovimentoConta movimento2 = new MovimentoConta(null, LocalDate.of(2025, 11, 7), new BigDecimal("400.00"), "Movimento de débito", TipoTransacao.DEBITO, conta2);
            movimentoContaRepo.save(movimento1);
            movimentoContaRepo.save(movimento2);

        } catch (Exception e) {
            // Logar o erro ou lançar uma exceção customizada
            System.err.println("Erro ao inicializar o banco de dados: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
