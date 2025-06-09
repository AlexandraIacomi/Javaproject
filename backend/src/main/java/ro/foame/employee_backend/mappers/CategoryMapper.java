package ro.foame.employee_backend.mappers;

import ro.foame.employee_backend.dtos.ProductDto;
import ro.foame.employee_backend.entities.Product;

public interface CategoryMapper<A, B> {
    B mapTo(A a);
    A mapFrom(B b);
    void mapFromDtoToEntity(ProductDto dto, Product entity);
}