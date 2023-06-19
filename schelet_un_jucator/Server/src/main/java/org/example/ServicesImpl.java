package org.example;


import java.util.Collection;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ServicesImpl implements IServices {
    UserRepository userRepository;
    GameRepository gameRepository;
//    ConfigurationRepository configurationRepository;
    private Map<Integer, IObserver> logged;


    public ServicesImpl(UserRepository userRepository, GameRepository gameRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
//        this.configurationRepository = configurationRepository;
        logged = new ConcurrentHashMap<Integer, IObserver>();
    }

    @Override
    public User login(User user, IObserver client) throws ServicesException {
        User loginUser = userRepository.findByUsername(user.getUsername());

        if(loginUser != null){
            if(logged.get(loginUser.getId()) != null)
                throw new ServicesException("User already logged in");
            logged.put(loginUser.getId(), client);
            return loginUser;
        }else
            throw new ServicesException("Authentication failed!");
    }

    @Override
    public void logout(User user, IObserver client) throws ServicesException {
        User logoutUser = userRepository.findByUsername(user.getUsername());
        IObserver localClient = logged.remove(logoutUser.getId());
        if(localClient == null)
            throw new ServicesException("User " + logoutUser.getUsername() + " is not logged in");
    }

    @Override
    public Game play(Game game) throws ServicesException {
//        Game playedGame = playGame(game);
//        if(playedGame.getOver())
//        {
//            gameRepository.add(playedGame);
//            notifyUsers(playedGame);
//        }
//
//        if(playedGame.getTries() >= 4){
//            game.setOver(true);
//            game.setTries(10);
//            gameRepository.add(playedGame);
//            notifyUsers(playedGame);
//        }
//
//        return playedGame;
        return null;
    }

//
//
//    public Game playGame(Game game) throws ServicesException {
//        if(game.getOver()){
//            throw new ServicesException("Game is over! You won !");
//        }
//
//        if(game.getTries() == 4){
//            throw new ServicesException("The maximum number of moves has been reached !");
//
//        }
//
//        int correctRow = configurationRepository.findById(game.getConfigurationId()).getRow();
//        int correctCol = configurationRepository.findById(game.getConfigurationId()).getColumn();
//        //get the first pair with the format row x column by splitting the stringBuffer by " "
//        String[] pair = game.getProposedPositions().split(" ");
//        String combined =String.valueOf(pair[pair.length-1]);
//        String[] rowCol = combined.split("x");
//        int row = Integer.parseInt(rowCol[0]);
//        int col = Integer.parseInt(rowCol[1]);
//
//
//        if(Objects.equals(row, correctRow) && Objects.equals(col, correctCol)) {
//            game.setOver(true);
//        }
//
//        game.setTries(game.getTries() + 1);
//
//        return game;
//    }
//


//    @Override
//    public Collection<Configuration> getAllConf() throws ServicesException {
//        return (Collection<Configuration>) configurationRepository.getAll();
//    }
//
//    @Override
//    public Configuration getConfById(Integer id) throws ServicesException {
//        return configurationRepository.findById(id);
//    }

    @Override
    public User getUserById(Integer data) throws ServicesException {
        return userRepository.findById(data);
    }

    private final int defaultThreadsNo=5;
    private void notifyUsers(Game game){
        ExecutorService executor = Executors.newFixedThreadPool(defaultThreadsNo);
        for(Map.Entry<Integer, IObserver> elem : logged.entrySet()){
            Integer id = elem.getKey();
            IObserver client = elem.getValue();
            executor.execute(() -> {
                try {
                    System.out.println("Notifying user with id: [" + id + "] that a game was ended");
                    client.endGame((Collection<Game>) gameRepository.getAll());
                } catch (ServicesException e) {
                    System.err.println("Error notifying friend " + e);
                }
            });
        }
        executor.shutdown();
    }

}
