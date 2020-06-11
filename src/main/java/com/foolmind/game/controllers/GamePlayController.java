package com.foolmind.game.controllers;

import com.foolmind.game.model.Game;
import com.foolmind.game.model.Player;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/play")
public class GamePlayController {
    @GetMapping("/")
    public String play(Authentication authentication) {
        return authentication.getName();
    }

    public void submitAnswer(Player player, String answer) {
        Game currentGame = new Game(); // figure out the current game of the player
        currentGame.submitAnswer(player, answer);
    }
}
