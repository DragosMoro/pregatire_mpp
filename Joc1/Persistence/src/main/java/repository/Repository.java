package repository;

import model.Entity;

import java.util.List;

public interface Repository<ID, E extends Entity<ID>> {
    public List<E> getAll();
    public E get(ID id);
    public void save(E entity);
    public void delete(ID id);
    public void update(E entity);
}
