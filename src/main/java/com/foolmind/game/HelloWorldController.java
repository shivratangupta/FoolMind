package com.foolmind.game;

import com.foolmind.game.model.GameMode;
import com.foolmind.game.model.Player;
import com.foolmind.game.model.Question;
import com.foolmind.game.repositories.PlayerRepository;
import com.foolmind.game.repositories.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/dev-test")
public class HelloWorldController {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/populate")
    public String populateDB() {
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

        questionRepository.save(new Question("what is the most important poneglyph",
                "Rio Poneglyph",
                GameMode.IS_THIS_A_FACT));
        return "populated";
    }

    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    @GetMapping("/question/{id}")
    public Optional<Question> getQuestionById(@PathVariable(name = "id") Long id) {
        return questionRepository.findById(id);
    }

    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    @GetMapping("/player/{id}")
    public Optional<Player> getPlayerById(@PathVariable(name = "id") Long id) {
        return playerRepository.findById(id);
    }

    // Games
    // Players
    // Admins
    // Questions
    // Rounds
    // ContentWriters
}
