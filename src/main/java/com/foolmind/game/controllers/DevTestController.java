package com.foolmind.game.controllers;

import com.foolmind.game.model.*;
import com.foolmind.game.services.DevTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/dev-test")
public class DevTestController {
    // injecting dependencies
    @Autowired
    private DevTestService devTestService;

    @GetMapping("/")
    public String hello() {
        return devTestService.hello();
    }

    // get request to populate the database
    @GetMapping("/populate")
    public String populateDB() {
        return devTestService.populateDB();
    }

    // shows the list of all the question
    @GetMapping("/questions")
    public List<Question> getAllQuestions() {
        return devTestService.getAllQuestions();
    }

    // shows the question which have a given id
    @GetMapping("/question/{id}")
    public Question getQuestionById(@PathVariable(name = "id") Long id) {
        return devTestService.getQuestionById(id);
    }

    // shows the list of all the player
    @GetMapping("/players")
    public List<Player> getAllPlayers() {
        return devTestService.getAllPlayers();
    }

    // shows the player which have a given id
    @GetMapping("/player/{id}")
    public Player getPlayerById(@PathVariable(name = "id") Long id) {
        return devTestService.getPlayerById(id);
    }

    // shows the list of all the users
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return devTestService.getAllUsers();
    }

    // shows the user which have a given id
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable(name = "id") Long id) {
        return devTestService.getUserById(id);
    }

    // shows the list of all the game
    @GetMapping("/games")
    public List<Game> getAllGames() {
        return devTestService.getAllGames();
    }

    // shows the game which have a given id
    @GetMapping("/game/{id}")
    public Game getGameById(@PathVariable(name = "id") Long id) {
        return devTestService.getGameById(id);
    }

    // shows the list of all the rounds
    @GetMapping("/rounds")
    public List<Round> getAllRounds() {
        return devTestService.getAllRounds();
    }

    // shows the round which have a given id
    @GetMapping("/round/{id}")
    public Round getRoundById(@PathVariable(name = "id") Long id) {
        return devTestService.getRoundById(id);
    }

    // shows the list of all the admins
    @GetMapping("/admins")
    public List<Admin> getAllAdmins() {
        return devTestService.getAllAdmins();
    }

    // shows the admin which have a given id
    @GetMapping("/admin/{id}")
    public Admin getAdminById(@PathVariable(name = "id") Long id) {
        return devTestService.getAdminById(id);
    }

    // shows the list of all the content writers
    @GetMapping("/contentwriters")
    public List<ContentWriter> getAllContentWriter() {
        return devTestService.getAllContentWriter();
    }

    // shows the content writer which have a given id
    @GetMapping("/contentwriter/{id}")
    public ContentWriter getContentWriterById(@PathVariable(name = "id") Long id) {
        return devTestService.getContentWriterById(id);
    }
}
