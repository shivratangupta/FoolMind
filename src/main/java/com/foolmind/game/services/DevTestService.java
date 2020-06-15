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
    public String populateDB() {
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
        Player areana = new Player.Builder()
                .alias("areana")
                .email("areana@gmail.com")
                .saltedHashedPassword("password")
                .build();
        playerRepository.save(areana);

        GameMode isThisAFact = new GameMode("Is This A Fact?", "https://thumbor.forbes.com/thumbor/fit-in/1200x0/filters%3Aformat%28jpg%29/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F473329831%2F0x0.jpg%3Ffit%3Dscale", "is this a fact description");
        gameModeRepository.save(isThisAFact);
        gameModeRepository.save(new GameMode("Word Up", "https://thumbor.forbes.com/thumbor/fit-in/1200x0/filters%3Aformat%28jpg%29/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F473329831%2F0x0.jpg%3Ffit%3Dscale", "word up description"));
        gameModeRepository.save(new GameMode("Un-Scramble", "https://thumbor.forbes.com/thumbor/fit-in/1200x0/filters%3Aformat%28jpg%29/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F473329831%2F0x0.jpg%3Ffit%3Dscale", "unscramble description"));
        gameModeRepository.save(new GameMode("Movie Buff", "https://thumbor.forbes.com/thumbor/fit-in/1200x0/filters%3Aformat%28jpg%29/https%3A%2F%2Fspecials-images.forbesimg.com%2Fimageserve%2F473329831%2F0x0.jpg%3Ffit%3Dscale", "movie buff description"));

        Question q1 = new Question("what is the most important poneglyph",
                "Rio Poneglyph",
                isThisAFact);
        questionRepository.save(q1);

        Game game = new Game();
        game.setGameMode(isThisAFact);
        game.setLeader(reyaan);
        game.getPlayers().add(reyaan);
        gameRepository.save(game);
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
