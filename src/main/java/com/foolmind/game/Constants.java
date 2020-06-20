package com.foolmind.game;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final int MAX_QUESTIONS_TO_READ = 100;
    public static Map<String, String> qaFilesMap;

    static {
        qaFilesMap = new HashMap<>();
        qaFilesMap.put("classpath:data/facts.txt", "Is This A Fact?");
        qaFilesMap.put("classpath:data/unscramble.txt", "Un-Scramble");
        qaFilesMap.put("classpath:data/wordUp.txt", "Word Up");
    }
}
