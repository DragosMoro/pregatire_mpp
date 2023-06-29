package org.example;

import java.util.List;

public interface IConfiguratioRepository<ID, E extends Entity<ID>> extends IRepository<ID, E>   {
    List<Configuration> findByGameId(Integer id);
}
