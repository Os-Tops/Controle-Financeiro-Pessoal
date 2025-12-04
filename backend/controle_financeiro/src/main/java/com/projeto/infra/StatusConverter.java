package com.projeto.infra;
import com.projeto.domains.enums.Status;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
@Converter(autoApply = false)
public class StatusConverter implements AttributeConverter<Status, Integer> {
    @Override
    public Integer convertToDatabaseColumn(Status status) {
        return status == null ? null : status.getId();
    }
    @Override
    public Status convertToEntityAttribute(Integer dbValue) {
        return Status.toEnum(dbValue);
    }
}