package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class WordleTest {

    @Test
    public void testLoadDictionary() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(null);
        WordleDictionary dictionary = loader.loadFromFile("words_ru.txt", 5, StandardCharsets.UTF_8);
        assertNotNull(dictionary, "Словарь должен быть загружен и не должен быть пустым");
    }

    @Test
    public void testGetRandomWord() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(null);
        WordleDictionary dictionary = loader.loadFromFile("words_ru.txt", 5, StandardCharsets.UTF_8);

        String rndWord = dictionary.getRandomWord();

        assertNotNull(rndWord, "Случайное слово не должно быть пустым");
        assertEquals(5, rndWord.length());
        assertTrue(dictionary.containsWord(rndWord));
    }

    @Test
    public void testCompareWords() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(null);
        WordleDictionary dictionary = loader.loadFromFile("words_ru.txt", 5, StandardCharsets.UTF_8);

        String answer = "слово";

        WordleGame game = new WordleGame(dictionary, answer, 6, null);

        String[] result = game.compareWords("право", answer);

        assertEquals(Arrays.toString(result), Arrays.toString(new String[]{"-, -, -, +, +"}));
    }

    @Test
    public void testGameState() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(null);
        WordleDictionary dictionary = loader.loadFromFile("words_ru.txt", 5, StandardCharsets.UTF_8);

        String answer = "слово";

        WordleGame game = new WordleGame(dictionary, answer, 6, null);

        game.compareWords("ghfdb", answer);
        game.compareWords("ветка", answer);

        assertEquals(5, game.getSteps(), "Количество оставшихся шагов должно уменьшиться на 1");
        assertFalse(game.isGameOver("ветка"), "Игра не должна быть завершена после одного хода");
    }

    @Test
    public void testHints() {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(null);
        WordleDictionary dictionary = loader.loadFromFile("words_ru.txt", 5, StandardCharsets.UTF_8);
        String answer = "слово";
        WordleGame game = new WordleGame(dictionary, answer, 6, null);

        game.usedWords.add("ветка");
        game.usedResults.add(new String[]{"^", "-", "-", "-", "-"});

        String hint = game.suggestWord();
        assertNotNull(hint);
        assertNotEquals(answer, hint);
        assertTrue(hint.contains("в"));

    }
}
