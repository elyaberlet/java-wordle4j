package ru.yandex.practicum;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;

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
    public void testCompareWords() throws Exception {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(null);
        WordleDictionary dictionary = loader.loadFromFile("words_ru.txt", 5, StandardCharsets.UTF_8);

        WordleGame game = new WordleGame(dictionary, 5, 6, null);
        game.setAnswerForTests("слово"); // теперь без рефлексии

        String[] result = game.compareWords("право");

        assertArrayEquals(new String[]{"-", "-", "-", "+", "+"}, result);
    }

    @Test
    public void testGameState() throws Exception {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(null);
        WordleDictionary dictionary = loader.loadFromFile("words_ru.txt", 5, StandardCharsets.UTF_8);

        WordleGame game = new WordleGame(dictionary, 5, 6, null);
        game.setAnswerForTests("слово");

        game.compareWords("слово");
        game.compareWords("право");

        assertEquals(4, game.getSteps(), "Количество оставшихся шагов должно уменьшиться на 2");
        assertFalse(game.isGameOver("право"), "Игра не должна быть завершена после двух ходов");
    }

    @Test
    public void testHints() throws Exception {
        WordleDictionaryLoader loader = new WordleDictionaryLoader(null);
        WordleDictionary dictionary = loader.loadFromFile("words_ru.txt", 5, StandardCharsets.UTF_8);

        WordleGame game = new WordleGame(dictionary, 5, 6, null);
        game.setAnswerForTests("слово");

        game.usedWords.add("ветка");
        game.usedResults.add(new String[]{"^", "-", "-", "-", "-"});

        String hint = game.suggestWord();
        assertNotNull(hint);
        assertTrue(hint.contains("в"));
    }
}
