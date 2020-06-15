package com.foolmind.game.repositories;

import com.foolmind.game.model.GameMode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GameModeRepository extends JpaRepository<GameMode, Long> {
    public Optional<GameMode> findByName(String gameMode);
}
