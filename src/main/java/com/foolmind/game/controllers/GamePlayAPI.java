package com.foolmind.game.controllers;

import com.foolmind.game.exceptions.InvalidGameActionException;
import com.foolmind.game.exceptions.InvalidGameRoundActionException;
import com.foolmind.game.model.Game;
import com.foolmind.game.model.GameMode;
import com.foolmind.game.model.Player;
import com.foolmind.game.repositories.GameModeRepository;
import com.foolmind.game.repositories.GameRepository;
import com.foolmind.game.repositories.PlayerRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/play")
public class GamePlayAPI {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private GameModeRepository gameModeRepository;
    @Autowired
    private GameRepository gameRepository;

    private Player getCurrentPlayer(Authentication authentication) {
        Optional<Player> player = playerRepository.findByEmail(authentication.getName());
        return player.get();
    }

    private JSONObject getData(Player player) {
        Game currentGame = player.getCurrentGame();
        JSONObject response = new JSONObject();
        response.put("playerAlias", player.getAlias());
        response.put("currentGame", currentGame == null ? null : currentGame.getId());
        if(currentGame == null) {
            JSONArray gameModes = new JSONArray();
            for (GameMode gameMode : gameModeRepository.findAll()) {
                JSONObject mode = new JSONObject();
                mode.put("title", gameMode.getName());
                mode.put("image", gameMode.getPicture());
                mode.put("description", gameMode.getDescription());
                gameModes.add(mode);
            }
            response.put("gameModes", gameModes);
        }
        else {
            response.put("gameState", currentGame.getGameState());
        }
        return response;
    }

    @GetMapping("")
    public JSONObject play(Authentication authentication) {
        Player player = getCurrentPlayer(authentication);
        return getData(player);
    }

    @GetMapping("/create-game")
    public JSONObject createGame(Authentication authentication,
                           @RequestParam(name = "mode") String gameMode,
                           @RequestParam(name = "rounds") Integer numRounds,
                           @RequestParam(name = "bot") Boolean hasBot) {
        Player leader = getCurrentPlayer(authentication);
        Optional<GameMode> mode = gameModeRepository.findByName(gameMode);
        gameRepository.save(new Game(mode.get(), numRounds, hasBot, leader));
        return getData(leader);
    }

    @GetMapping("/reyaan-submit")
    public String reyaanSubmit() throws InvalidGameRoundActionException, InvalidGameActionException {
        Optional<Player> reyaan = playerRepository.findByEmail("reyaan@gmail.com");
        Game game = reyaan.get().getCurrentGame();
        game.submitAnswer(reyaan.get(), "answer");
        gameRepository.save(game);
        return "done";
    }

    @GetMapping("/shriyan-submit")
    public String shriyanSubmit() throws InvalidGameRoundActionException, InvalidGameActionException {
        Optional<Player> shriyan = playerRepository.findByEmail("shriyan@gmail.com");
        Game game = shriyan.get().getCurrentGame();
        game.submitAnswer(shriyan.get(), "answer");
        gameRepository.save(game);
        return "done";
    }
}
