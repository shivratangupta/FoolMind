package com.foolmind.game.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.foolmind.game.Utils;
import com.foolmind.game.exceptions.InvalidGameActionException;
import com.foolmind.game.exceptions.InvalidGameRoundActionException;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "games")
public class Game extends Auditable {
    @ManyToMany
    @JsonIdentityReference
    @Getter @Setter
    private Set<Player> players = new HashSet<>();

    @Getter @Setter
    @Enumerated(EnumType.STRING)
    @NotNull
    private GameMode gameMode;

    @OneToMany(mappedBy = "game", cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter @Setter
    private List<Round> rounds = new ArrayList<>();

    @Getter @Setter
    private int numRounds = 10;

    @Getter @Setter
    private Boolean hasBot = false;

    @ManyToOne
    @JsonIdentityReference
    @NotNull
    @Getter @Setter
    private Player leader;

    @ManyToMany(cascade = CascadeType.ALL)
    @JsonManagedReference
    @Getter @Setter
    private Map<Player, Stat> playerStats = new HashMap<>();

    @Enumerated(EnumType.STRING)
    @Getter @Setter
    private GameStatus gameStatus;

    @ManyToMany
    @JsonIdentityReference
    @Getter @Setter
    private Set<Player> readyPlayers = new HashSet<>();

    // default constructor for spring boot
    public Game() {
    }

    // parameterized constructor
    public Game(@NotNull GameMode gameMode, int numRounds, Boolean hasBot, @NotNull Player leader) {
        this.gameMode = gameMode;
        this.numRounds = numRounds;
        this.hasBot = hasBot;
        this.leader = leader;
        this.players.add(leader);
    }

    public void addPlayer(Player player) throws InvalidGameActionException {
        if(!gameStatus.equals(GameStatus.PLAYERS_JOINING))
            throw new InvalidGameActionException("Can't join after the game has started");
        players.add(player);
    }

    public void removePlayer(Player player) throws InvalidGameActionException {
        // player has to be in the game to remove from the game
        if(!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game.");
        players.remove(player);

        // if all the player is removed from the game or
        // if only one player in the game and game status is other then joining then
        // game is going to end
        if(players.size() == 0 || (players.size() == 1 && !gameStatus.equals(GameStatus.PLAYERS_JOINING)))
            endGame();
    }

    public void startGame(Player player) throws InvalidGameActionException {
        // player has to be leader to start the game
        if(!player.equals(leader))
            throw new InvalidGameActionException("Only user can start the game");
        startNewRound();
    }

    private void startNewRound() {
        gameStatus = GameStatus.SUBMITTING_ANSWERS;
        Question question = Utils.getRandomQuestion(gameMode);
        Round round = new Round(this, question, rounds.size() + 1);
        if(hasBot)
            round.setBotAnswer(Utils.getRandomBotAnswer(question));
        rounds.add(round);
    }

    public void submitAnswer(Player player, String answer) throws InvalidGameActionException,
            InvalidGameRoundActionException {
        // if answer can not be empty
        if(answer.length() == 0)
            throw new InvalidGameActionException("Answer can not be empty");

        // player has to be present in the game
        if(!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game.");

        // if game status is not submitting answers then player can't submit the answer
        if(!gameStatus.equals(GameStatus.SUBMITTING_ANSWERS))
            throw new InvalidGameActionException("Game is not accepting answers at present");
        Round currentRound = getCurrentRound();
        currentRound.submitAnswer(player, answer);

        // if all the answers are submitted then change the status of the game
        if(currentRound.allAnswersSubmitted(players.size()))
            gameStatus = GameStatus.SELECTING_ANSWERS;
    }

    public void selectAnswer(Player player, PlayerAnswer selectedAnswer) throws InvalidGameActionException,
            InvalidGameRoundActionException {
        // player has to be present in the game
        if(!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game.");

        // if game status is not submitting answers then player can't select the answer
        if(!gameStatus.equals(GameStatus.SELECTING_ANSWERS))
            throw new InvalidGameActionException("Game is not selecting answers at present");
        Round currentRound = getCurrentRound();
        currentRound.selectAnswer(player, selectedAnswer);

        // if all the answers are submitted then change the status of the game
        if(currentRound.allAnswersSelected(players.size())) {
            // if this round is not a last round then
            // change game status to waiting for ready otherwise
            // end the game
            if(rounds.size() < numRounds)
                gameStatus = GameStatus.WAITING_FOR_READY;
            else
                endGame();
        }
    }

    public void playerIsReady(Player player) throws InvalidGameActionException {
        // player has to be present in the game
        if(!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game.");

        // if game status is not waiting for ready then player can't be added to ready players
        if(!gameStatus.equals(GameStatus.WAITING_FOR_READY))
            throw new InvalidGameActionException("Game is not waiting players to be ready");
        readyPlayers.add(player);

        // if all players are ready then start a new round
        if(readyPlayers.size() == players.size())
            startNewRound();
    }

    public void playerIsNotReady(Player player) throws InvalidGameActionException {
        // player has to be present in the game
        if(!players.contains(player))
            throw new InvalidGameActionException("No such player was in the game.");

        // if game status is not waiting for ready then player can't be added to ready players
        if(!gameStatus.equals(GameStatus.WAITING_FOR_READY))
            throw new InvalidGameActionException("Game is not waiting players to be ready");
        readyPlayers.remove(player);
    }

    private Round getCurrentRound() throws InvalidGameActionException {
        // to get the current round game has to be start
        if(rounds.size() == 0)
            throw new InvalidGameActionException("The game has not started");
        return rounds.get(rounds.size() - 1);
    }

    private void endGame() {
        gameStatus = GameStatus.ENDED;
    }

    public String getGameState() {
        // todo
        return "String which contains data which frontend needs";
    }
}
