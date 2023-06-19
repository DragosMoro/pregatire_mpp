package org.example;

public interface IUserRepository<ID, E extends Entity<ID>> extends IRepository<ID, E> {

    User getUserByUsername(String username);
}
