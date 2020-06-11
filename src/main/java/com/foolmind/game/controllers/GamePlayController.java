package com.foolmind.game.controllers;

import com.foolmind.game.exceptions.InvalidGameActionException;
import com.foolmind.game.exceptions.InvalidGameRoundActionException;
import com.foolmind.game.model.Game;
import com.foolmind.game.model.Player;
import com.foolmind.game.repositories.PlayerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/play")
public class GamePlayController {
    @Autowired
    private PlayerRepository playerRepository;

    @GetMapping("/")
    public String play(Authentication authentication) {
        return authentication.getName();
    }

    @GetMapping("/submit-answer/{answer}")
    public void submitAnswer(Authentication authentication, @PathVariable(name = "answer") String answer) throws InvalidGameRoundActionException,
            InvalidGameActionException {
        Optional<Player> player = playerRepository.findByEmail(authentication.getName());
        player.get().getCurrentGame().submitAnswer(player.get(), answer);
    }
}
