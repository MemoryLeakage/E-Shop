package com.eshop.models.constants;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class AvailabilityStateConverter implements AttributeConverter<ProductAvailabilityState,String> {
    @Override
    public String convertToDatabaseColumn(ProductAvailabilityState attribute) {
        return attribute.name();
    }

    @Override
    public ProductAvailabilityState convertToEntityAttribute(String dbData) {
        return ProductAvailabilityState.valueOf(dbData);
    }
}
