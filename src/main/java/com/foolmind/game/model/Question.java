package com.foolmind.game.model;

import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "questions")
public class Question extends Auditable {
    @NotBlank
    @Getter @Setter
    private String questionText;

    @NotBlank
    @Getter @Setter
    private String correctAnswer;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "question")
    @JsonManagedReference
    @Getter @Setter
    private Set<BotAnswer> botAnswers = new HashSet<>();

    @ManyToOne
    @JsonIdentityReference
    @NotNull
    @Getter @Setter
    private GameMode gameMode;

    // default constructor for spring boot
    public Question() {
    }

    // parameterized constructor
    public Question(@NotBlank String questionText, @NotBlank String correctAnswer, @NotNull GameMode gameMode) {
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.gameMode = gameMode;
    }
}
