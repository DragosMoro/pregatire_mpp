package repository;

import model.User;

public interface UserRepository extends Repository<Integer, User> {
    User findByName(String name);
}
