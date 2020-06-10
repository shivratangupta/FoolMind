package com.foolmind.game.controllers;

import com.foolmind.game.model.*;
import com.foolmind.game.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dev-test")
public class DevTestController {
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
    @GetMapping("/populate")
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
    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    // shows the question which have a given id
    @GetMapping("/question/{id}")
    public Optional<Question> getQuestionById(@PathVariable(name = "id") Long id) {
        return questionRepository.findById(id);
    }

    // shows the list of all the player
    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    // shows the player which have a given id
    @GetMapping("/player/{id}")
    public Optional<Player> getPlayerById(@PathVariable(name = "id") Long id) {
        return playerRepository.findById(id);
    }

    // shows the list of all the users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // shows the user which have a given id
    @GetMapping("/user/{id}")
    public Optional<User> getUserById(@PathVariable(name = "id") Long id) {
        return userRepository.findById(id);
    }

    // shows the list of all the game
    @GetMapping("/games")
    public List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    // shows the game which have a given id
    @GetMapping("/game/{id}")
    public Optional<Game> getGameById(@PathVariable(name = "id") Long id) {
        return gameRepository.findById(id);
    }

    // shows the list of all the rounds
    @GetMapping("/rounds")
    public List<Round> getAllRounds() {
        return roundRepository.findAll();
    }

    // shows the round which have a given id
    @GetMapping("/round/{id}")
    public Optional<Round> getRoundById(@PathVariable(name = "id") Long id) {
        return roundRepository.findById(id);
    }

    // shows the list of all the admins
    @GetMapping("/admins")
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    // shows the admin which have a given id
    @GetMapping("/admin/{id}")
    public Optional<Admin> getAdminById(@PathVariable(name = "id") Long id) {
        return adminRepository.findById(id);
    }

    // shows the list of all the contentwriters
    @GetMapping("/contentwriters")
    public List<ContentWriter> getAllContentWriter() {
        return contentWriterRepository.findAll();
    }

    // shows the contewriter which have a given id
    @GetMapping("/contentwriter/{id}")
    public Optional<ContentWriter> getcontentWriterById(@PathVariable(name = "id") Long id) {
        return contentWriterRepository.findById(id);
    }
}
