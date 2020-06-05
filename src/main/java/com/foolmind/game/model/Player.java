package com.foolmind.game.model;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Entity
@Table(name = "players")
public class Player extends Auditable {
    @NotBlank
    private String alias;

    @Email @NotBlank
    private String email;

    @NotBlank
    private String saltedHashedPassword;

    private String psychFaceURL;
    private String picURL;
}
