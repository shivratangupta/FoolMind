package com.foolmind.game.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.foolmind.game.exceptions.InvalidGameRoundActionException;
import lombok.Getter;
import lombok.Setter;
import net.minidev.json.JSONObject;

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

    public void submitAnswer(Player player, String answer) throws InvalidGameRoundActionException {
        // if player has already submitted answer, then reject
        if(submittedAnswers.containsKey(player))
            throw new InvalidGameRoundActionException("Player has already submitted an answer for this round");

        // if duplicate answer, then reject
        for(PlayerAnswer existingAnswer : submittedAnswers.values()) {
            if(answer.equals(existingAnswer.getAnswer()))
                throw new InvalidGameRoundActionException("Duplicate Answer!");
        }
        submittedAnswers.put(player, new PlayerAnswer(this, player, answer));
    }

    public boolean allAnswersSubmitted(int numPlayers) {
        return submittedAnswers.size() == numPlayers;
    }

    public void selectAnswer(Player player, PlayerAnswer selectedAnswer) throws InvalidGameRoundActionException {
        // if player has already submitted answer, then reject
        if(selectedAnswers.containsKey(player))
            throw new InvalidGameRoundActionException("Player has already selected an answer for this round");

        // player can't select his own answer
        if(selectedAnswer.getPlayer().equals(player))
            throw new InvalidGameRoundActionException("Can't select your own answer");

        // if submitted answer is not for this round
        if(!selectedAnswer.getRound().equals(this))
            throw new InvalidGameRoundActionException("No such answer was submitted in this round");

        // selected answer has to be present in the submitted Answer
        boolean flag = false;
        for(PlayerAnswer existingAnswer : submittedAnswers.values()) {
            if(selectedAnswer.equals(existingAnswer))
                flag = true;
        }
        if(!flag)
            throw new InvalidGameRoundActionException("selected answer is not present in submitted answer");
        selectedAnswers.put(player, selectedAnswer);
    }

    public boolean allAnswersSelected(int numPlayers) {
        return selectedAnswers.size() == numPlayers;
    }

    public JSONObject getRoundData() {
        // todo
        return null;
    }
}
