package com.projeto.infra;
import com.projeto.domains.enums.MeioPagamento;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
@Converter(autoApply = false)
public class MeioPagamentoConverter implements AttributeConverter<MeioPagamento, Integer> {
    @Override
    public Integer convertToDatabaseColumn(MeioPagamento meioPagamento) {
        return meioPagamento == null ? null : meioPagamento.getId();
    }
    @Override
    public MeioPagamento convertToEntityAttribute(Integer dbValue) {
        return MeioPagamento.toEnum(dbValue);
    }
}