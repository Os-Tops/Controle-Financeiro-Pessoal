package com.projeto.infra;
import com.projeto.domains.enums.StatusCartao;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
@Converter(autoApply = false)
public class StatusCartaoConverter implements AttributeConverter<StatusCartao, Integer> {
    @Override
    public Integer convertToDatabaseColumn(StatusCartao statusCartao) {
        return statusCartao == null ? null : statusCartao.getId();
    }
    @Override
    public StatusCartao convertToEntityAttribute(Integer dbValue) {
        return StatusCartao.toEnum(dbValue);
    }
}