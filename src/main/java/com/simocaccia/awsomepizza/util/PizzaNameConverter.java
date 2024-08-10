package com.simocaccia.awsomepizza.util;

import com.simocaccia.awsomepizza.entity.PizzaName;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@Converter
public class PizzaNameConverter implements AttributeConverter<Collection<PizzaName>, String> {
    private static final String SPLIT_CHAR = ",";

    @Override
    public String convertToDatabaseColumn(Collection<PizzaName> items) {
        if (items == null || items.isEmpty()) {
            return "";
        }
        return items.stream()
                .map(PizzaName::toString)
                .collect(Collectors.joining(SPLIT_CHAR));
    }

    @Override
    public Collection<PizzaName> convertToEntityAttribute(String dbData) {
        if (dbData == null || dbData.isEmpty()) {
            return List.of();
        }
        return Stream.of(dbData.split(SPLIT_CHAR))
                .map(PizzaName::valueOf)
                .toList();
    }
}
