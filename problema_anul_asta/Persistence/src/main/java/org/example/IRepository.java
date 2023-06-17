package org.example;

public interface IRepository <ID, E extends Entity<ID>>{
    Iterable<E> getAll();
    E findById(ID id);
    void add(E entity);
    void delete(ID id);
    void update(E entity);

}
