package com.foolmind.game.repositories;

import com.foolmind.game.model.PlayerAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerAnswerRepository extends JpaRepository<PlayerAnswer, Long> {
}
