package com.foolmind.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "rounds")
public class Round extends Auditable {
    @ManyToOne
    @Getter @Setter
    private Game game;
}
