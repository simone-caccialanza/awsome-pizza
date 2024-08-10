package com.simocaccia.awsomepizza.util;

import com.simocaccia.awsomepizza.entity.PizzaName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class PizzaNameConverterTest {

    PizzaNameConverter pizzaNameConverter = new PizzaNameConverter();

    public static Stream<Arguments> convertToEntityAttributeData() {
        return Stream.of(
                Arguments.of("", List.of()),
                Arguments.of("MARGHERITA,ORTOLANA,PROSCIUTTO", List.of(PizzaName.MARGHERITA, PizzaName.ORTOLANA, PizzaName.PROSCIUTTO)),
                Arguments.of("MARGHERITA,MARGHERITA", List.of(PizzaName.MARGHERITA, PizzaName.MARGHERITA))
        );
    }

    public static Stream<Arguments> convertToDatabaseColumnData() {
        return Stream.of(
                Arguments.of(List.of(), ""),
                Arguments.of(List.of(PizzaName.MARGHERITA, PizzaName.ORTOLANA, PizzaName.PROSCIUTTO), "MARGHERITA,ORTOLANA,PROSCIUTTO"),
                Arguments.of(List.of(PizzaName.MARGHERITA, PizzaName.MARGHERITA), "MARGHERITA,MARGHERITA")
        );
    }

    @ParameterizedTest
    @MethodSource("convertToDatabaseColumnData")
    void convertToDatabaseColumn(Collection<PizzaName> items, String expected) {

        String actual = pizzaNameConverter.convertToDatabaseColumn(items);

        assertEquals(expected, actual);
    }


    @ParameterizedTest
    @MethodSource("convertToEntityAttributeData")
    void convertToEntityAttribute(String dbData, Collection<PizzaName> expected) {
        Collection<PizzaName> actual = pizzaNameConverter.convertToEntityAttribute(dbData);

        List<PizzaName> expectedList = List.copyOf(expected);
        List<PizzaName> actualList = List.copyOf(actual);

        assertThat(actualList).containsExactlyInAnyOrderElementsOf(expectedList);
    }
}