package com.foolmind.game.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stats")
public class Stat extends Auditable {
    @Getter @Setter
    private long gotFooledCount = 0;

    @Getter @Setter
    private long fooledOthersCount = 0;

    @Getter @Setter
    private long correctAnswerCount = 0;
}
