package com.foolmind.game.repositories;

import com.foolmind.game.model.BotAnswer;
import com.foolmind.game.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BotAnswerRepository extends JpaRepository<BotAnswer, Long> {

    @Query(value = "", nativeQuery = true) // todo
    BotAnswer getRandomBotAnswer(Question question);
}
