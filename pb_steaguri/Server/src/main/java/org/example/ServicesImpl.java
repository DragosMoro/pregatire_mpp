package org.example;


import javax.persistence.criteria.CriteriaBuilder;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.abs;

public class ServicesImpl implements IServices {
    UserRepository userRepository;
    GameRepository gameRepository;
    ConfigurationRepository configurationRepository;
    private Map<Integer, IObserver> logged;


    public ServicesImpl(UserRepository userRepository, GameRepository gameRepository, ConfigurationRepository configurationRepository) {
        this.userRepository = userRepository;
        this.gameRepository = gameRepository;
        this.configurationRepository = configurationRepository;
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
        Game playedGame = playGame(game);
        if(playedGame.getIsOver())
        {
            gameRepository.add(playedGame);
            notifyUsers(playedGame);
        }

        if(playedGame.getTries() > 4){
            game.setIsOver(true);
            gameRepository.add(playedGame);
            notifyUsers(playedGame);
        }

        return playedGame;
    }



    public Game playGame(Game game) throws ServicesException {
        if(game.getIsOver()){
            throw new ServicesException("Game is over! You won !");
        }


        List<Integer> flags = retrieveFlagsPositions(configurationRepository.findById(game.getConfigurationId()).getFlagConfiguration());

        if(flags.contains(game.getGuess()) && game.getTries()<4 && game.getGuessedPositions()==2) {
            game.setScore(game.getScore() + 10);
            game.setGuessedPositions(game.getGuessedPositions() + 1);
            game.setTries(game.getTries() + 1);
            StringBuffer sb = new StringBuffer(game.getWordStatus());
            sb.setCharAt(game.getGuess() - 1, 'S');
            game.setWordStatus(sb.toString());
            game.setIsOver(true);
            return game;
        }
        else if(flags.contains(game.getGuess()))
        {
            game.setScore(game.getScore() + 10);
            game.setGuessedPositions(game.getGuessedPositions() + 1);
            game.setTries(game.getTries() + 1);
            StringBuffer sb = new StringBuffer(game.getWordStatus());
            sb.setCharAt(game.getGuess() - 1, 'S');
            game.setWordStatus(sb.toString());
            return game;
        }
        else {
            //true-stanga, false-dreapta
            int dif = 100;
            boolean direction = false;
            int position = -1;
            String message = "";

            for(Integer i : flags){
                if(abs(game.getGuess()- i) < dif){
                    dif = abs(game.getGuess()- i);
                    if(game.getGuess() > i)
                        direction = true;
                    else
                        direction = false;
                    position = i;
                }
            }
            if (direction)
            {
                message = "Flag is to the left of your guess";
            }
            else
            {
                message = "Flag is to the right of your guess";
            }
            game.setMessage(message);
            game.setScore(game.getScore() - 1);
            game.setTries(game.getTries() + 1);
            return game;
        }
    }



    private ArrayList<Integer> retrieveFlagsPositions(String flagsPositions){
        //retrieve all the carachters from the string that are equal to S
        ArrayList<Integer> flags = new ArrayList<>();
        for(int i = 0; i < flagsPositions.length(); i++){
            if(flagsPositions.charAt(i) == 'S'){
                flags.add(i+1);
            }
        }
        return flags;
    }
    @Override
    public Collection<Configuration> getAllConf() throws ServicesException {
        return (Collection<Configuration>) configurationRepository.getAll();
    }

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
