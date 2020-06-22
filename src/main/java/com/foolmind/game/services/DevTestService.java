package com.foolmind.game.services;

import com.foolmind.game.Constants;
import com.foolmind.game.Pair;
import com.foolmind.game.Utils;
import com.foolmind.game.exceptions.InvalidGameActionException;
import com.foolmind.game.model.*;
import com.foolmind.game.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class DevTestService {
    // injecting dependencies
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameModeRepository gameModeRepository;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ContentWriterRepository contentWriterRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }

    // get request to populate the database
    public String populateDB() throws InvalidGameActionException {
        for(Player player : playerRepository.findAll()) {
            player.getGames().clear();
            player.setCurrentGame(null);
            playerRepository.save(player);
        }

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        questionRepository.deleteAll();
        gameModeRepository.deleteAll();

        Player reyaan = new Player.Builder()
                .alias("reyaan")
                .picURL("https://www.anime-planet.com/images/characters/tanjirou-kamado-145804.jpg")
                .foolFaceURL("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSYxu5_eX0q1LCdfuioGV5lZ4xUz0hAgUI4BiIOx2-Z0Lra8k7G")
                .email("reyaan@foolmind.com")
                .saltedHashedPassword("reyaan")
                .build();
        playerRepository.save(reyaan);
        Player shriyan = new Player.Builder()
                .alias("shriyan")
                .picURL("https://i.pinimg.com/originals/85/dd/17/85dd1702345997e97d3660ee677cb551.png")
                .foolFaceURL("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSYxu5_eX0q1LCdfuioGV5lZ4xUz0hAgUI4BiIOx2-Z0Lra8k7G")
                .email("shriyan@foolmind.com")
                .saltedHashedPassword("shriyan")
                .build();
        playerRepository.save(shriyan);
        Player areana = new Player.Builder()
                .alias("areana")
                .picURL("https://images.discordapp.net/avatars/568083171455795200/17e3e43a6072802a0f351e5c3e5d9fc2.png?size=512")
                .foolFaceURL("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSYxu5_eX0q1LCdfuioGV5lZ4xUz0hAgUI4BiIOx2-Z0Lra8k7G")
                .email("areana@foolmind.com")
                .saltedHashedPassword("areana")
                .build();
        playerRepository.save(shriyan);
        Player ajayesh = new Player.Builder()
                .alias("ajayesh")
                .picURL("https://www.anime-planet.com/images/characters/zenitsu-agatsuma-145870.jpg")
                .foolFaceURL("https://encrypted-tbn0.gstatic.com/images?q=tbn%3AANd9GcSYxu5_eX0q1LCdfuioGV5lZ4xUz0hAgUI4BiIOx2-Z0Lra8k7G")
                .email("ajayesh@foolmind.com")
                .saltedHashedPassword("ajayesh")
                .build();
        playerRepository.save(ajayesh);

        GameMode isThisAFact = new GameMode("Is This A Fact?", "/images/is_this_a_fact.jpg", "These remarkable facts may surprise you, but so will your friends' fake answers! Come up with your own answers, and win a point for every player you fool into thinking your false fact is right. Then try to pick the real fact!");
        gameModeRepository.save(isThisAFact);
        gameModeRepository.save(new GameMode("Proverbs", "/images/proverb.jpeg", "A half truth is a whole lie. Take these proverbs and write your own endings. Win a point for every player you fool into thinking your ending is right, then try to pick the real one!"));
        gameModeRepository.save(new GameMode("Animals", "/images/animal.png", "You won't believe these animal facts, because most of them will be made up by you and your friends! Write a fake answer to these real animal facts."));
        gameModeRepository.save(new GameMode("And Truth Comes Out", "/images/truth.jpeg", "\"If David were arrested tomorrow, it would probably be for this.\" In this mode, you and your friends become the game! Write the best answer about your friends, then choose your favorite answers."));
        gameModeRepository.save(new GameMode("Word Up", "/images/word_up.png", "This mode is packed with real definitions to unusual words. Make up a word for each definition and win a point for every player you fool into thinking that your word is correct. Then try to pick the real one!"));
        gameModeRepository.save(new GameMode("Un-Scramble", "/images/unscramble.png", "unscramble description"));
        gameModeRepository.save(new GameMode("Movie Buff", "/images/movie_buff.jpeg", "These movie titles all belong to very real movies. Make up your own plot and win a point for every player you fool into thinking your plot is the correct one. Then try to pick the real movie plot!"));

        List<Question> questions = new ArrayList<>();
        for(Map.Entry<String, String> fileMode : Constants.qaFilesMap.entrySet()) {
            String filename = fileMode.getKey();
            Optional<GameMode> gameMode = gameModeRepository.findByName(fileMode.getValue());
            for(Pair<String, String> questionAnswer : Utils.readQAFile(filename)) {
                questions.add(new Question(questionAnswer.getFirst(), questionAnswer.getSecond(), gameMode.get()));
            }
        }
        questionRepository.saveAll(questions);

        return "populated";
    }

    // shows the list of all the question
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // shows the question which have a given id
    public Question getQuestionById(Long id) {
        Optional<Question> question = questionRepository.findById(id);

        if(!question.isPresent())
            return null;
        return question.get();
    }

    // shows the list of all the player
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    // shows the player which have a given id
    public Player getPlayerById(Long id) {
        Optional<Player> player = playerRepository.findById(id);

        if(!player.isPresent())
            return null;
        return player.get();
    }

    // shows the list of all the users
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // shows the user which have a given id
    public User getUserById(Long id) {
        Optional<User> user = userRepository.findById(id);

        if(!user.isPresent())
            return null;
        return user.get();
    }

    // shows the list of all the game
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // shows the game which have a given id
    public Game getGameById(Long id) {
        Optional<Game> game = gameRepository.findById(id);

        if(!game.isPresent())
            return null;
        return game.get();
    }

    // shows the list of all the rounds
    public List<Round> getAllRounds() {
        return roundRepository.findAll();
    }

    // shows the round which have a given id
    public Round getRoundById(Long id) {
        Optional<Round> round = roundRepository.findById(id);

        if(!round.isPresent())
            return null;
        return round.get();
    }

    // shows the list of all the admins
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // shows the admin which have a given id
    public Admin getAdminById(Long id) {
        Optional<Admin> admin = adminRepository.findById(id);

        if(!admin.isPresent())
            return null;
        return admin.get();
    }

    // shows the list of all the content writers
    public List<ContentWriter> getAllContentWriter() {
        return contentWriterRepository.findAll();
    }

    // shows the content writer which have a given id
    public ContentWriter getContentWriterById(Long id) {
        Optional<ContentWriter> contentWriter = contentWriterRepository.findById(id);

        if(!contentWriter.isPresent())
            return null;
        return contentWriter.get();
    }
}
