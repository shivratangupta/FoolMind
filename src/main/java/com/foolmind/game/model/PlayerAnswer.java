package com.foolmind.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "playeranswers")
public class PlayerAnswer extends Auditable {
    @ManyToOne
    @NotNull
    @Getter @Setter
    private Round round;

    @ManyToOne
    @NotNull
    @Getter @Setter
    private Player player;

    @NotBlank
    @Getter @Setter
    private String answer;
}
