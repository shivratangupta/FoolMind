package com.foolmind.game.exceptions;

import lombok.Getter;
import lombok.Setter;

public class FoolMindException extends Exception {
    @Getter @Setter
    private String message;

    public FoolMindException(String message) {
        super();
        this.message = message;
    }
}
