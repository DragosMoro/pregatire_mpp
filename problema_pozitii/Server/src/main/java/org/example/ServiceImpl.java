package org.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceImpl implements IService {
    private final IGameTableRepositry gameTableRepository;
    private final IGameRoundRepository gameRoundRepository;
    private final IUserRepository userRepository;

    private final IGameRepository gameRepository;

    private final Map<Integer, IObserver> loggedUsers;

    private int numberOfPlayers = 0;
    private final Object lock = new Object();

    public ServiceImpl(IGameTableRepositry gameTableRepository, IGameRoundRepository gameRoundRepository, IUserRepository userRepository, IGameRepository gameRepository) {
        this.gameTableRepository = gameTableRepository;
        this.gameRoundRepository = gameRoundRepository;
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.loggedUsers = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized User login(User user, IObserver player) throws ServicesException {
        User userToLogin = userRepository.findByUsername(user.getUsername());
        if (userToLogin != null) {
            if (userToLogin.getPassword().equals(user.getPassword())) {
                if (loggedUsers.get(userToLogin.getId()) != null) {
                    throw new ServicesException("User already logged in.");
                }
                loggedUsers.put(userToLogin.getId(), player);
            } else {
                throw new ServicesException("Wrong password.");
            }
        } else {
            throw new ServicesException("User does not exist.");
        }
        return userToLogin;
    }



    @Override
    public synchronized void logout(User user, IObserver player) throws ServicesException {
        IObserver localPlayer = loggedUsers.remove(user.getId());
        if (localPlayer == null) {
            throw new ServicesException("User " + user.getId() + " is not logged in.");
        }

    }

    @Override
    public synchronized void startGame(User user, IObserver player) throws ServicesException {
        IObserver localPlayer = loggedUsers.get(user.getId());
        if (localPlayer == null) {
            throw new ServicesException("User " + user.getId() + " is not logged in.");
        }
        Game game = new Game();
        game.setRoundNumber(0);
        GameRound gameRound = new GameRound();
        gameRound.setUser(user);
        gameRound.setGame(game);
        gameRound.setMoney(50);
        gameRound.setDiceNumber(0);
        gameRound.setPosition(0);
        gameRoundRepository.save(gameRound);
        generateGameBoardForPlayer(user, game);


        synchronized (lock) {
            numberOfPlayers++;

            while (numberOfPlayers < 3) {
                try {

                    lock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            lock.notifyAll();
            numberOfPlayers = 0;
            game.setRoundNumber(0);
            gameRepository.save(game);

        }

    }


private void generateGameBoardForPlayer(User user, Game game){
    List<GameRound> newGameRounds = new ArrayList<>();
    List<GameRound> gameRounds = (ArrayList<GameRound>) gameRoundRepository.findAll();
    boolean found = false;
    for(GameRound gameRound : gameRounds){
        if(Objects.equals(gameRound.getGame().getId(), game.getId())){
            found = true;
            break;
        }
    }

    if(!found){
        for(GameRound gameRound : gameRounds){
            if(gameRound.getGame() == null && gameRound.getUser() == null){
                GameRound gameRoundToAdd = new GameRound();
                gameRoundToAdd.setUser(user);
                gameRoundToAdd.setGame(game);
                gameRoundToAdd.setMoney(gameRound.getMoney());
                gameRoundToAdd.setDiceNumber(gameRound.getDiceNumber());
                gameRoundToAdd.setPosition(gameRound.getPosition());
                newGameRounds.add(gameRoundToAdd);
                gameRoundRepository.save(gameRoundToAdd);


            }

        }
    }

    Collections.shuffle(newGameRounds);


    List<GameRound> selectedGameRounds = newGameRounds.subList(0, Math.min(newGameRounds.size(), 5));


    for (GameRound gameRound : selectedGameRounds) {
        gameRound.setUser(user);
        gameRoundRepository.update(gameRound);
    }

}
}