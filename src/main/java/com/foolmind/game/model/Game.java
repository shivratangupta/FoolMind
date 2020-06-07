package com.foolmind.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "games")
public class Game extends Auditable {
    @ManyToMany
    @Getter @Setter
    private Set<Player> players = new HashSet<>();

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @NotNull
    private GameMode gameMode;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @Getter @Setter
    private List<Round> rounds = new ArrayList<>();

    @Getter @Setter
    private int numRounds = 10;

    @Getter @Setter
    private Boolean hasBot = false;

    @ManyToOne
    @NotNull
    @Getter @Setter
    private Player leader;

    @ManyToMany(cascade = CascadeType.ALL)
    @Getter @Setter
    private Map<Player, Stat> playerStats = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private GameStatus gameStatus;

    @ManyToMany
    @Getter @Setter
    private Set<Player> readyPlayers = new HashSet<>();

    // default constructor for spring boot
    public Game() {
    }

    // parameterized constructor
    public Game(@NotNull GameMode gameMode, @NotNull Player leader, GameStatus gameStatus) {
        this.gameMode = gameMode;
        this.leader = leader;
        this.gameStatus = gameStatus;
    }
}
