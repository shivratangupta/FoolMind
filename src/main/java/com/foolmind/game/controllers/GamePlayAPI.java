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
    private static JSONObject success;

    static {
        success = new JSONObject();
        success.put("status", "success");
    }

    public JSONObject handleCustomException(Exception exception) {
        JSONObject error = new JSONObject();
        error.put("status", "error");
        error.put("errorText", exception.getMessage());
        return error;
    }

    private Player getCurrentPlayer(Authentication authentication) {
        Optional<Player> player = playerRepository.findByEmail(authentication.getName());
        return player.get();
    }
    
    @GetMapping("/game-modes")
    public JSONArray gameModes() {
        JSONArray gameModes = new JSONArray();
        for (GameMode gameMode : gameModeRepository.findAll()) {
            JSONObject mode = new JSONObject();
            mode.put("title", gameMode.getName());
            mode.put("image", gameMode.getPicture());
            mode.put("description", gameMode.getDescription());
            gameModes.add(mode);
        }
        return gameModes;
    }

    @GetMapping("/player-data")
    private JSONObject playerData(Authentication authentication) {
        return playerData(getCurrentPlayer(authentication));
    }

    private JSONObject playerData(Player player) {
        JSONObject data = new JSONObject();
        data.put("alias", player.getAlias());
        data.put("picURL", player.getPicURL());
        data.put("foolFaceURL", player.getFoolFaceURL());
        data.put("email", player.getEmail());
        data.put("currentGameId", player.getCurrentGame() == null ? null : player.getCurrentGame().getId());
        JSONObject stats = new JSONObject();
        stats.put("correctAnswerCount", player.getStat().getCorrectAnswerCount());
        stats.put("gotFooledCount", player.getStat().getGotFooledCount());
        stats.put("fooledOthersCount", player.getStat().getFooledOthersCount());
        data.put("stats", stats);
        return data;
    }

    @GetMapping("/game-state")
    public JSONObject gameState(Authentication authentication) {
        return gameState(getCurrentPlayer(authentication).getCurrentGame());
    }

    public JSONObject gameState(Game game) {
        JSONObject data = new JSONObject();
        if(game == null)
            return data;

        data.put("id", game.getId());
        data.put("secretCode", game.getSecretCode());
        data.put("numRounds", game.getNumRounds());
        data.put("gameMode", game.getGameMode());
        data.put("hasBot", game.getHasBot());
        data.put("status", game.getGameState());
        try {
            data.put("round", game.getRoundData());
        } catch (InvalidGameActionException ignored) {
        }
        return data;
    }

    @GetMapping("/create-game")
    public JSONObject createGame(Authentication authentication,
                           @RequestParam(name = "mode") String gameMode,
                           @RequestParam(name = "rounds") Integer numRounds,
                           @RequestParam(name = "bot") Boolean hasBot) {
        Player leader = getCurrentPlayer(authentication);
        Optional<GameMode> mode = gameModeRepository.findByName(gameMode);
        gameRepository.save(new Game(mode.get(), numRounds, hasBot, leader));
        return playerData(leader);
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
