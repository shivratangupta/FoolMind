package com.foolmind.game.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "rounds")
public class Round extends Auditable {
    @ManyToOne
    @JsonBackReference
    @NotNull
    @Getter @Setter
    private Game game;

    @ManyToOne
    @JsonIdentityReference
    @NotNull
    @Getter @Setter
    private Question question;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonBackReference
    @Getter @Setter
    private Map<Player, PlayerAnswer> submittedAnswers = new HashMap<>();

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter @Setter
    private Map<Player, PlayerAnswer> selectedAnswers = new HashMap<>();

    @ManyToOne
    @JsonIdentityReference
    @Getter @Setter
    private BotAnswer botAnswer;

    @NotNull
    @Getter @Setter
    private int roundNumber;

    // default constructor for spring boot
    public Round() {
    }

    // parameterized constructor
    public Round(@NotNull Game game, @NotNull Question question, @NotNull int roundNumber) {
        this.game = game;
        this.question = question;
        this.roundNumber = roundNumber;
    }
}
