package server;
import model.Game;
import model.GameState;
import repository.GameStateRepository;
import service.IObserver;
import service.IService;
import service.MyException;
import model.User;
import repository.GameRepository;
import repository.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class ServicesImplementation implements IService {
    private final Map<Integer, IObserver> players, loggedUsers;
    public Game game = null;
    private UserRepository userRepository;

    private GameStateRepository gameStateRepo;

    private GameRepository gameRepo;

    public ServicesImplementation(UserRepository userRepo, GameStateRepository gameStateRepo, GameRepository gameRepo) {
        this.userRepository = userRepo;
        this.gameStateRepo = gameStateRepo;
        this.gameRepo = gameRepo;
        players = new ConcurrentHashMap<>();
        loggedUsers = new ConcurrentHashMap<>();
    }

    public Game getGame() {
        return game;
    }

    @Override
    public void sendNumber(int number) throws MyException {
        for (IObserver player: players.values()) {
            player.playerUpdate(number);
        }
    }

    @Override
    public void saveGameState(GameState state) throws MyException {
        gameStateRepo.save(state);
    }

    @Override
    public List<User> getAllUsers() throws MyException {
        return userRepository.getAll();
    }

    @Override
    public List<User> getActiveUsers() throws MyException {
        return null;
    }

    @Override
    public User getPlayer(int id) throws MyException {
        return userRepository.get(id);
    }

    @Override
    public Boolean addPlayer(User player, IObserver playerObserver) {
        if (game != null && game.getPlayer3() != -1) {
            return false;
        }
        if (game == null) {
            game = new Game();
            game.setPlayer1(player.getId());
            game.setScore1(50);
        } else if (game.getPlayer2() == -1) {
            game.setPlayer2(player.getId());
            game.setScore2(50);
        } else {
            game.setPlayer3(player.getId());
            game.setScore3(50);
        }
        players.put(player.getId(), playerObserver);
        if (players.size() == 3) {
            startGame();
        }
        return true;
    }

    public void startGame() {
        // generate an array with 5 random numbers
        int[] numbers = new int[5];
        for (int i = 0; i < 5; i++) {
            numbers[i] = ((int) (Math.random() * 20)) * 2;
        }
        game.setPositions(numbers);
        gameRepo.save(game);
        game = gameRepo.getLast();
        System.out.println("Game started: " + game.getId());
        for (IObserver player: players.values()) {
            System.out.println(player);
            player.refreshAll(game);
        }
    }

    @Override
    public User login(User user, IObserver userObserver) throws MyException {
        User autentificatedUser = userRepository.findByName(user.getName());
        if (autentificatedUser != null && autentificatedUser.getPassword().equals(user.getPassword())) {
            if (loggedUsers.get(autentificatedUser.getId()) != null)
                throw new MyException("User already logged in.");
            loggedUsers.put(autentificatedUser.getId(), userObserver);
        } else {
            throw new MyException("Authentication failed.");
        }
        return autentificatedUser;
    }

    @Override
    public void logout(User user, IObserver userObserver) throws MyException {
        IObserver localUserObserver = loggedUsers.remove(user.getId());
        if (localUserObserver == null)
            throw new MyException("User " + user.getName() + " is not logged in.");
    }

    @Override
    public void gameFinished(Game game) throws MyException {
        System.out.println(game.getId() + this.game.getId());
        game.setId(this.game.getId());
        gameRepo.update(game);
        players.clear();
        this.game = null;
    }
}
