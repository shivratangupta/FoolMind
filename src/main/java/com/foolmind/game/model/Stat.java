package com.foolmind.game.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "stats")
public class Stat extends Auditable {
    private long gotPsychedCount;
    private long psychedOthersCount;
    private long correctAnswerCount;
}
