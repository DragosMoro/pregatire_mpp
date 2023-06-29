package org.example;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/exam")
public class RestController {
    private ConfigurationRepository configurationRepository;
    private GameRepository gameRepository;

    private UserRepository userRepository;

    @Autowired
    public RestController( GameRepository gameRepository, UserRepository userRepository, ConfigurationRepository configurationRepository) {
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
        this.configurationRepository = configurationRepository;
    }

    @RequestMapping(value = "/configurations/{id}", method = RequestMethod.POST)
    public ResponseEntity<?> createEntity(@RequestBody Configuration configuration, @PathVariable Integer id){
        System.out.println("Rest - create configuration");
        configuration.setGameId(id);
        configurationRepository.add(configuration);
        return new ResponseEntity<Configuration>(configuration, HttpStatus.OK);
    }

    @RequestMapping(value = "/games/{id}", method = RequestMethod.GET)
    public Collection<ObjectDTO> getAllGames(@PathVariable Integer id)
    {
        Collection<Game> games = (Collection<Game>) gameRepository.getAll();

        Collection<ObjectDTO> dtoObjectConfiguration = games.stream()
                .filter(x -> Objects.equals(x.getUserId(), id) )
                .map(game -> {
                    String config = "";
                    List<Configuration> configurations = configurationRepository.findByGameId(game.getId());
                    for(Configuration c : configurations)
                    {
                        config+=c.getRow()+"x"+c.getColumn()+" ";
                    }
                    String finalConfig = config;
                    return new ObjectDTO( userRepository.findById(game.getUserId()).getUsername(), finalConfig, game.getDuration());
                })
                .toList();
        return dtoObjectConfiguration;
    }
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public User getGamesByUSer(@PathVariable Integer id)
    {
        return userRepository.findById(id);
    }

}
