package rest;

import model.Game;
import model.GameState;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.web.bind.annotation.RestController
@RequestMapping("/games")
public class RestController {
    private static final String template = "Hello, %s!";

    @Autowired
    private UserDBRepository userRepository;
    @Autowired
    private GameDBRepository gameRepository;
    @Autowired
    private GameStateORMRepository gameStateRepository;

    public RestController() {
        userRepository = new UserDBRepository();
    }

    @RequestMapping("/greeting")
    public String greeting(@RequestParam(value="name", defaultValue="World") String name) {
        return String.format(template, name);
    }

    @CrossOrigin//(origins = "http://localhost:8080")
    @RequestMapping(value="/{gameID}/player/{playerID}", method = RequestMethod.GET)
    public GameState[] getAll(@PathVariable Integer gameID, @PathVariable Integer playerID) {
        List<GameState> result = gameStateRepository.getAll();
        System.out.println("Getting player states in the game... " + result.size());
        return result.stream().filter(p -> p.getGameID() == gameID  && p.getPlayerID() == playerID).toArray(GameState[]::new);
    }
    @RequestMapping(value="/{id}", method= RequestMethod.GET)
    public ResponseEntity<?> getById(@PathVariable Integer id){
        System.out.println("Getting game ... " + id);
        Game game = gameRepository.get(id);
        if (game==null)
            return ResponseEntity.notFound().build();
        else
            return ResponseEntity.ok(game);
    }

    /*
    @CrossOrigin//(origins = "http://localhost:8080")
    @RequestMapping(method = RequestMethod.POST)
    public int create(@RequestBody User user){
        userRepository.save(user);
        user = userRepository.findByName(user.getName());
        return user.getId();
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    public User update(@RequestBody User user, @PathVariable Integer id) {
        user.setId(id);
        userRepository.update(user);
        return user;
    }

    @RequestMapping(value="/{id}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        System.out.println("Deleting user ... "+id);
        userRepository.delete(id);
        return ResponseEntity.ok().build();
    }
    */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(Exception e) {
        e.printStackTrace();
        return "Error: " + e.getMessage();
    }
}
