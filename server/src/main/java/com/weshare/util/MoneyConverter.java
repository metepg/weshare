package com.weshare.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class MoneyConverter implements AttributeConverter<Double, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Double attribute) {
        if (attribute == null) {
            return null;
        }
        return (int) Math.round(attribute * 100);
    }

    @Override
    public Double convertToEntityAttribute(Integer dbData) {
        if (dbData == null) {
            return null;
        }
        return dbData / 100.0;
    }
}
