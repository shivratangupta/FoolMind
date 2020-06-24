package com.foolmind.game.controllers;

import com.foolmind.game.Utils;
import com.foolmind.game.exceptions.InvalidGameActionException;
import com.foolmind.game.exceptions.InvalidGameRoundActionException;
import com.foolmind.game.exceptions.InvalidSecretCodeException;
import com.foolmind.game.model.Game;
import com.foolmind.game.model.GameMode;
import com.foolmind.game.model.Player;
import com.foolmind.game.model.PlayerAnswer;
import com.foolmind.game.repositories.GameModeRepository;
import com.foolmind.game.repositories.GameRepository;
import com.foolmind.game.repositories.PlayerAnswerRepository;
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
    @Autowired
    private PlayerAnswerRepository playerAnswerRepository;
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
        data.put("status", game.getGameStatus());
        try {
            data.put("round", game.getRoundData());
        } catch (InvalidGameActionException ignored) {
        }
        return data;
    }

    @GetMapping("/leaderboard")
    public JSONArray leaderboard() {
        JSONArray data = new JSONArray();
        for(Player player : playerRepository.findAll()) {
            JSONObject stats = new JSONObject();
            stats.put("alias", player.getAlias());
            stats.put("picURL", player.getPicURL());
            stats.put("correctAnswerCount", player.getStat().getCorrectAnswerCount());
            stats.put("gotFooledCount", player.getStat().getGotFooledCount());
            stats.put("fooledOthersCount", player.getStat().getFooledOthersCount());
            data.add(stats);
        }

        return data;
    }

    @GetMapping("/update-profile")
    public JSONObject updateProfile(Authentication authentication,
                                    @RequestParam(name = "alias") String alias,
                                    @RequestParam(name = "email") String email,
                                    @RequestParam(name = "fooledFaceURL") String fooledFaceURL,
                                    @RequestParam(name = "picURL") String picURL) {
        Player player = getCurrentPlayer(authentication);
        player.setAlias(alias);
        player.setEmail(email);
        player.setFoolFaceURL(fooledFaceURL);
        player.setPicURL(picURL);
        return success;
    }

    @GetMapping("/create-game")
    public JSONObject createGame(Authentication authentication,
                           @RequestParam(name = "gameMode") String gameMode,
                           @RequestParam(name = "numRounds") Integer numRounds,
                           @RequestParam(name = "hasBot") Boolean hasBot) {
        Player leader = getCurrentPlayer(authentication);
        Optional<GameMode> mode = gameModeRepository.findByName(gameMode);
        gameRepository.save(new Game(mode.get(), numRounds, hasBot, leader));
        return success;
    }

    @GetMapping("/join-game")
    public JSONObject joinGame(Authentication authentication,
                               @RequestParam(name = "secretCode") String secretCode) throws InvalidSecretCodeException, InvalidGameActionException {
        Player player = getCurrentPlayer(authentication);
        Optional<Game> game = gameRepository.findById(Utils.getGameIdFromSecretCode(secretCode));
        if(!game.isPresent())
            throw new InvalidSecretCodeException("Secret code " + secretCode + " is invalid");
        game.get().addPlayer(player);
        return success;
    }

    @GetMapping("/leave-game")
    public JSONObject leaveGame(Authentication authentication) throws InvalidGameActionException {
        Player player = getCurrentPlayer(authentication);
        player.getCurrentGame().removePlayer(player);
        return success;
    }

    @GetMapping("/start-game")
    public JSONObject startGame(Authentication authentication) throws InvalidGameActionException {
        Player leader = getCurrentPlayer(authentication);
        leader.getCurrentGame().startGame(leader);
        return success;
    }

    @GetMapping("/end-game")
    public JSONObject endGame(Authentication authentication) throws InvalidGameActionException {
        Player leader = getCurrentPlayer(authentication);
        leader.getCurrentGame().endGame(leader);
        return success;
    }

    @GetMapping("/submit-answer")
    public JSONObject submitAnswer(Authentication authentication,
                                   @RequestParam(name = "answer") String answer) throws InvalidGameRoundActionException, InvalidGameActionException {
        Player player = getCurrentPlayer(authentication);
        Game game = player.getCurrentGame();
        game.submitAnswer(player, answer);
        return success;
    }

    @GetMapping("/select-answer")
    public JSONObject selectAnswer(Authentication authentication,
                                   @RequestParam(name = "playerAnswerId") Long playerAnswerId) throws InvalidGameRoundActionException, InvalidGameActionException {
        Player player = getCurrentPlayer(authentication);
        Game game = player.getCurrentGame();
        Optional<PlayerAnswer> playerAnswer = playerAnswerRepository.findById(playerAnswerId);
        game.selectAnswer(player, playerAnswer.get());
        return success;
    }

    @GetMapping("/player-ready")
    public JSONObject playerReady(Authentication authentication) throws InvalidGameActionException {
        Player player = getCurrentPlayer(authentication);
        Game game = player.getCurrentGame();
        game.playerIsReady(player);
        return success;
    }

    @GetMapping("/player-not-ready")
    public JSONObject playerNotReady(Authentication authentication) throws InvalidGameActionException {
        Player player = getCurrentPlayer(authentication);
        Game game = player.getCurrentGame();
        game.playerIsNotReady(player);
        return success;
    }
}
