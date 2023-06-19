package org.example;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

        if(playedGame.getTries() >= 4){
            game.setOver(true);
            gameRepository.add(playedGame);
            notifyUsers(playedGame);
        }

        return playedGame;
    }



    public Game playGame(Game game) throws ServicesException {
        if(game.getOver()){
            throw new ServicesException("Game is over! You won !");
        }

        String configurationPattern = configurationRepository.findById(game.getConfigurationId()).getConfigurationPattern();
        Map<String, Integer> configurations = new HashMap<>();
        Map<String, Integer> configurationsUser = new HashMap<>();

        List<String> choices = new ArrayList<>();

        String [] splitChoices = game.getChoosedLetters().split(" ");
        if(game.getTries()!=0){
            for(int i = 0; i < splitChoices.length-1; i++){
                choices.add(splitChoices[i]);
            }
        }

        String [] splitConfigurationPattern = configurationPattern.split(" ");
        for(String s : splitConfigurationPattern){
            String [] split = s.split(",");
            configurations.put(split[0], Integer.parseInt(split[1]));
        }

        for(String s : splitConfigurationPattern){
            String [] split = s.split(",");
            if(!choices.contains(split[0]))
                configurationsUser.put(split[0], Integer.parseInt(split[1]));
        }
        int valuePlayer = 0;
        try{
            valuePlayer = configurationsUser.get(game.getChoice());
        }
        catch (NullPointerException e)
        {
            throw new ServicesException("You have to choose a letter from the configuration pattern that you didn't choose before!");
        }

        int valueServer = configurations.get(game.getServerChoice());

        if (valuePlayer>valueServer)
        {
            game.setTries(game.getTries()+1);
            game.setPoints(game.getPoints()+valueServer+valuePlayer);
        }
        else if (Objects.equals(valuePlayer, valueServer))
        {
            game.setTries(game.getTries()+1);
        }
        else if(valuePlayer<valueServer)
        {
            game.setTries(game.getTries()+1);
            game.setPoints(game.getPoints()-1);
        }


        return game;
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
