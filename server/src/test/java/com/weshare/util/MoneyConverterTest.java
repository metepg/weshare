package com.weshare.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MoneyConverterTest {

    private final MoneyConverter converter = new MoneyConverter();

    @Test
    void convertToDatabaseColumn() {
        assertNull(converter.convertToDatabaseColumn(null));
        assertEquals(123, converter.convertToDatabaseColumn(1.23));
        assertEquals(0, converter.convertToDatabaseColumn(0.004));
    }

    @Test
    void convertToEntityAttribute() {
        assertNull(converter.convertToEntityAttribute(null));
        assertEquals(1.23, converter.convertToEntityAttribute(123));
        assertEquals(0.0, converter.convertToEntityAttribute(0));
    }
}
