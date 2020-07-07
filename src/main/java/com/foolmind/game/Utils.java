package com.foolmind.game;

import com.foolmind.game.config.ApplicationContextProvider;
import com.foolmind.game.model.BotAnswer;
import com.foolmind.game.model.GameMode;
import com.foolmind.game.model.Question;
import com.foolmind.game.repositories.BotAnswerRepository;
import com.foolmind.game.repositories.QuestionRepository;
import org.springframework.util.ResourceUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Utils {
    private static QuestionRepository questionRepository;
    private static BotAnswerRepository botAnswerRepository;
    private static List<String> wordsList;
    private static Map<String, Integer> wordsIndices;

    static {
        questionRepository = (QuestionRepository) ApplicationContextProvider
                .getApplicationContext()
                .getBean("questionRepository");
        botAnswerRepository = (BotAnswerRepository) ApplicationContextProvider
                .getApplicationContext()
                .getBean("botAnswerRepository");

        try {
            BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile("classpath:data/words.txt")));
            wordsList = new ArrayList<>();
            wordsIndices = new HashMap<>();
            String word = br.readLine();
            int index = 0;
            while(word != null) {
                word = word.trim();
                wordsList.add(word);
                wordsIndices.put(word, index);
                word = br.readLine();
                index++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static Question getRandomQuestion(GameMode gameMode) {
        return questionRepository.getRandomQuestion(gameMode.getId());
    }

    public static BotAnswer getRandomBotAnswer(Question question) {
        return botAnswerRepository.getRandomBotAnswer(question.getId());
    }

    public static String getSecretCodeFromGameId(Long id) {
        String code = "";
        int base = wordsList.size();
        while(id > 0) {
            code = code + " " + wordsList.get((int) (id % base));
            id /= base;
        }
        return code.trim();
    }

    public static Long getGameIdFromSecretCode(String code) {
        String[] words = code.split(" ");
        long gameId = 0;
        int base = wordsList.size();
        for(int i=words.length-1; i>=0; i--) {
            gameId = gameId * base + wordsIndices.get(words[i]);
        }
        return gameId;
    }

    public static List<Pair<String, String>> readQAFile(String filename) {
        List<Pair<String, String>> questionAnswers = new ArrayList<>();
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(ResourceUtils.getFile(filename)));
            String questionText = br.readLine();
            String correctAnswer = br.readLine();
            while (questionText != null && count < Constants.MAX_QUESTIONS_TO_READ) {
                if(questionText == null || correctAnswer == null) break;
                questionAnswers.add(new Pair(questionText.trim(), correctAnswer.trim()));
                questionText = br.readLine();
                correctAnswer = br.readLine();
                count++;
            }
        } catch (IOException ignored) {
        }
        return questionAnswers;
    }
}
