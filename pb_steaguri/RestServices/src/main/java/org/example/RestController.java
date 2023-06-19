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

    @RequestMapping(value = "/configurations", method = RequestMethod.POST)
    public ResponseEntity<?> createEntity(@RequestBody Configuration configuration){
        System.out.println("Rest - create configuration");
        configurationRepository.add(configuration);
        return new ResponseEntity<Configuration>(configuration, HttpStatus.OK);
    }

    @RequestMapping(value = "/games/{id}", method = RequestMethod.GET)
    public Collection<ObjectDTO2> getAllGames(@PathVariable Integer id)
    {
        Collection<Game> games = (Collection<Game>) gameRepository.getAll();
        //int tries, String message, StringBuffer propsedPositions, String username
        return games.stream()
                .filter(x -> Objects.equals(x.getUserId(), id) )
                //String username, int score, int guessedPositions
                .map(game -> {
                    return new ObjectDTO2(userRepository.findById(game.getUserId()).getUsername(), game.getScore(), game.getGuessedPositions());
                })
                .toList();
    }
    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    public User getUser(@PathVariable Integer id)
    {

        return userRepository.findById(id);
    }

}
