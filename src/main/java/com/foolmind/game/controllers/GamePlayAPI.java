package com.foolmind.game.controllers;

import com.foolmind.game.model.Player;
import com.foolmind.game.repositories.PlayerRepository;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/play")
public class GamePlayAPI {
    @Autowired
    private PlayerRepository playerRepository;

    private Player getCurrentPlayer(Authentication authentication) {
        Optional<Player> player = playerRepository.findByEmail(authentication.getName());
        return player.get();
    }

    @GetMapping("")
    public JSONObject play(Authentication authentication) {
        Player player = getCurrentPlayer(authentication);
        JSONObject obj = new JSONObject();
        obj.put("playerAlias", player.getAlias());
        return obj;
    }


}
