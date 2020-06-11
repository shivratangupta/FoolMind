package com.foolmind.game;

import com.foolmind.game.config.ApplicationContextProvider;
import com.foolmind.game.model.BotAnswer;
import com.foolmind.game.model.GameMode;
import com.foolmind.game.model.Question;
import com.foolmind.game.repositories.BotAnswerRepository;
import com.foolmind.game.repositories.QuestionRepository;

public class Utils {
    private static QuestionRepository questionRepository;
    private static BotAnswerRepository botAnswerRepository;

    static {
        questionRepository = (QuestionRepository) ApplicationContextProvider
                .getApplicationContext()
                .getBean("questionRepository");
        botAnswerRepository = (BotAnswerRepository) ApplicationContextProvider
                .getApplicationContext()
                .getBean("botAnswerRepository");
    }

    public static Question getRandomQuestion(GameMode gameMode) {
        return questionRepository.getRandomQuestion(gameMode);
    }

    public static BotAnswer getRandomBotAnswer(Question question) {
        return botAnswerRepository.getRandomBotAnswer(question);
    }
}
