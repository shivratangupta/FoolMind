package com.foolmind.game.repositories;

import com.foolmind.game.model.BotAnswer;
import com.foolmind.game.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BotAnswerRepository extends JpaRepository<BotAnswer, Long> {

    // todo
    @Query(value = "SELECT * FROM botanswers WHERE question=:question ORDER BY RAND() LIMIT 1", nativeQuery = true)
    BotAnswer getRandomBotAnswer(Question question);
}
