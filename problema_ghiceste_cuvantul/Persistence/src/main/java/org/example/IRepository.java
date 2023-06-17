package org.example;

public interface IRepository<ID, E extends Entity<ID>> {
void add(E entity);
void delete(ID id);

void update(E entity);
E findByID(ID id);
Iterable<E> getAll();


}
