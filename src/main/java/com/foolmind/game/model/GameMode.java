package com.foolmind.game.model;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "gamemodes")
public class GameMode extends Auditable {
    @NotBlank
    @Column(unique = true)
    @Getter @Setter
    private String name;

    @Getter @Setter
    private String picture;

    @Getter @Setter
    private String description;

    // default constructor for spring
    public GameMode() {
    }

    // parameterized constructor
    public GameMode(@NotBlank String name, @URL String picture, String description) {
        this.name = name;
        this.picture = picture;
        this.description = description;
    }
}
