package com.foolmind.game.auth;

import com.foolmind.game.model.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

public class customUserDetails implements UserDetails {
    public customUserDetails(Optional<User> user) {
    }
}
