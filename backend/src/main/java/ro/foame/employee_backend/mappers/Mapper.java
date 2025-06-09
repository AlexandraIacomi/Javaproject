package ro.foame.employee_backend.mappers;

public interface Mapper<A, B> {
    B mapTo(A a);
    A mapFrom(B b);
    default void mapFromDtoToEntity(B dto, A entity) {}
}