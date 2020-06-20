package com.foolmind.game.repositories;

import com.foolmind.game.model.BotAnswer;
import com.foolmind.game.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface BotAnswerRepository extends JpaRepository<BotAnswer, Long> {

    @Query(value = "SELECT * FROM botanswers WHERE question_id=:questionId ORDER BY RANDOM() LIMIT 1", nativeQuery = true)
    BotAnswer getRandomBotAnswer(Long questionId);
}
