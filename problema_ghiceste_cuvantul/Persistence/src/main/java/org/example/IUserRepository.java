package org.example;

public interface IUserRepository extends IRepository<Integer, User> {
    User findByUsername(String username);
}
