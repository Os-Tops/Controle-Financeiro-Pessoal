package com.projeto.infra;
import com.projeto.domains.enums.StatusLancamento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
@Converter(autoApply = false)
public class StatusLancamentoConverter implements AttributeConverter<StatusLancamento, Integer> {
    @Override
    public Integer convertToDatabaseColumn(StatusLancamento statusLancamento) {
        return statusLancamento == null ? null : statusLancamento.getId();
    }
    @Override
    public StatusLancamento convertToEntityAttribute(Integer dbValue) {
        return StatusLancamento.toEnum(dbValue);
    }
}