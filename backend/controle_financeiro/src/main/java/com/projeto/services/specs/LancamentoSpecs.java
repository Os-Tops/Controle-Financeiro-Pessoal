package com.projeto.services.specs;

import com.projeto.domains.Lancamento;
import com.projeto.domains.enums.StatusLancamento;
import com.projeto.domains.enums.TipoLancamento;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class LancamentoSpecs {

    public static Specification<Lancamento> comFiltros(TipoLancamento tipo,
                                                       StatusLancamento status,
                                                       LocalDate inicio,
                                                       LocalDate fim) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Filtro por Tipo (RECEITA / DESPESA)
            if (tipo != null) {
                predicates.add(builder.equal(root.get("tipoLancamento"), tipo));
            }

            // Filtro por Status (PENDENTE / PAGO)
            if (status != null) {
                predicates.add(builder.equal(root.get("statusLancamento"), status));
            }

            // Filtro por Data
            if (inicio != null && fim != null) {
                predicates.add(builder.between(root.get("dataVencimento"), inicio, fim));
            } else if (inicio != null) {
                predicates.add(builder.greaterThanOrEqualTo(root.get("dataVencimento"), inicio));
            } else if (fim != null) {
                predicates.add(builder.lessThanOrEqualTo(root.get("dataVencimento"), fim));
            }

            // Combina tudo com AND
            return builder.and(predicates.toArray(new Predicate[0]));
        };
    }
}
