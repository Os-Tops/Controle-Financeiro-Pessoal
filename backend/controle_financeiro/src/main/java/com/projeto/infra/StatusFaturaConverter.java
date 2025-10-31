package com.projeto.infra;
import com.projeto.domains.enums.StatusFatura;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
@Converter(autoApply = false)
public class StatusFaturaConverter implements AttributeConverter<StatusFatura, Integer> {
    @Override
    public Integer convertToDatabaseColumn(StatusFatura statusFatura) {
        return statusFatura == null ? null : statusFatura.getId();
    }
    @Override
    public StatusFatura convertToEntityAttribute(Integer dbValue) {
        return StatusFatura.toEnum(dbValue);
    }
}