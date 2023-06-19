package repository;

import model.Game;

public interface GameRepository extends Repository<Integer, Game> {
    Game getLast();
}
