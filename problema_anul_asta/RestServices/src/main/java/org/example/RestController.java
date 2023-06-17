package org.example;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Objects;

@CrossOrigin
@org.springframework.web.bind.annotation.RestController
@RequestMapping("/exam")
public class RestController {
    private ConfigurationRepository configurationRepository;
    private GameRepository gameRepository;

    private UserRepository userRepository;

    @Autowired
    public RestController(ConfigurationRepository configurationRepository,GameRepository g, UserRepository userRepository1) {
        this.configurationRepository = configurationRepository;
        this.gameRepository = g;
        this.userRepository = userRepository1;
    }

    @RequestMapping(value = "/configurations", method = RequestMethod.POST)
    public ResponseEntity<?> createEntity(@RequestBody Configuration configuration){
        System.out.println("Rest - create configuration");
        configurationRepository.add(configuration);
        return new ResponseEntity<Configuration>(configuration, HttpStatus.OK);
    }

    @RequestMapping(value = "/games/{id}", method = RequestMethod.GET)
    public Collection<ObjectDTO> getAllGames(@PathVariable Integer id)
    {
        Collection<Game> games = (Collection<Game>) gameRepository.getAll();
        //int tries, String message, StringBuffer propsedPositions, String username
        Collection<ObjectDTO> dtoObjectConfiguration = games.stream()
                .filter(x -> Objects.equals(x.getUserId(), id) )
                .map(game -> {
                    return new ObjectDTO(game.getTries(), configurationRepository.findById(game.getConfigurationId()).getMessage(), game.getProposedPositions(), userRepository.findById(game.getUserId()).getUsername());
                })
                .toList();
        return dtoObjectConfiguration;
    }

}
