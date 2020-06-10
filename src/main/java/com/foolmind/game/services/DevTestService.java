package com.foolmind.game.services;

import com.foolmind.game.model.*;
import com.foolmind.game.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
import java.util.Optional;

@Service
public class DevTestService {
    // injecting dependencies
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private ContentWriterRepository contentWriterRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }

    // get request to populate the database
    public String populateDB() {
        for(Player player : playerRepository.findAll()) {
            player.getGames().clear();
            playerRepository.save(player);
        }

        gameRepository.deleteAll();
        playerRepository.deleteAll();
        questionRepository.deleteAll();
        roundRepository.deleteAll();
        adminRepository.deleteAll();
        Player reyaan = new Player.Builder()
                .alias("reyaan")
                .email("reyaan@gmail.com")
                .saltedHashedPassword("password")
                .build();
        playerRepository.save(reyaan);
        Player shriyan = new Player.Builder()
                .alias("shriyan")
                .email("shriyan@gmail.com")
                .saltedHashedPassword("password")
                .build();
        playerRepository.save(shriyan);

        Question q1 = new Question("what is the most important poneglyph",
                "Rio Poneglyph",
                GameMode.IS_THIS_A_FACT);
        questionRepository.save(q1);

        Game g1 = new Game(GameMode.IS_THIS_A_FACT,
                reyaan, GameStatus.PLAYERS_JOINING);
        gameRepository.save(g1);

        Round r1 = new Round(g1, q1, 1);
        roundRepository.save(r1);
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