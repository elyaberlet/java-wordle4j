package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {

    private String answer;
    private int steps;
    private final int wordLength;
    private final WordleDictionary dictionary;

    protected List<String> usedWords;
    protected List<String[]> usedResults;

    public WordleGame(WordleDictionary dictionary, int wordLength, int steps, PrintWriter logWriter) {
        this.dictionary = dictionary;
        this.wordLength = wordLength;
        this.answer = dictionary.getRandomWord();
        this.steps = steps;
        this.usedWords = new ArrayList<>();
        this.usedResults = new ArrayList<>();

        if (logWriter != null) {
            logWriter.println("Загаданное слово: " + answer);
        }
    }

    public void validate(String userWord)
            throws WordNotFoundInDictionary, WrongWordException, GameNoSuchWordException {
        if (userWord == null || userWord.isEmpty()) {
            throw new WrongWordException("Слово не может быть пустым");
        }

        if (userWord.length() != wordLength) {
            throw new WordNotFoundInDictionary("Слово должно состоять из " + wordLength + " букв");
        }

        if (!dictionary.containsWord(userWord.toLowerCase())) {
            throw new GameNoSuchWordException("Слово отсутствует в словаре");
        }
    }

    public String[] compareWords(String userWord)
            throws WordNotFoundInDictionary, WrongWordException, GameNoSuchWordException {

        validate(userWord);

        String[] result = new String[userWord.length()];
        char[] userChars = userWord.toCharArray();
        char[] answerChars = answer.toCharArray();

        for (int i = 0; i < userWord.length(); i++) {
            if (userChars[i] == answerChars[i]) {
                result[i] = "+";
            } else if (answer.indexOf(userChars[i]) >= 0) {
                result[i] = "^";
            } else {
                result[i] = "-";
            }
        }

        usedWords.add(userWord.toLowerCase());
        usedResults.add(result);
        steps--;

        return result;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswerForTests(String answer) {
       this.answer = answer;
    }

    public int getSteps() {
        return steps;
    }

    public boolean isGameOver(String userWord) {
        return steps <= 0 || answer.equalsIgnoreCase(userWord);
    }

    public String suggestWord() {
        List<String> availableVariants = new ArrayList<>(dictionary.getWords());
        availableVariants.removeAll(usedWords);

        List<Character> correct = new ArrayList<>();
        for (int i = 0; i < wordLength; i++) {
            correct.add(null);
        }

        Set<Character> present = new HashSet<>();
        Set<Character> absent = new HashSet<>();

        for (int i = 0; i < usedWords.size(); i++) {
            String guess = usedWords.get(i);
            String[] result = usedResults.get(i);

            for (int j = 0; j < guess.length(); j++) {
                char c = guess.charAt(j);
                switch (result[j]) {
                    case "+":
                        correct.set(j, c);
                        break;
                    case "-":
                        absent.add(c);
                        break;
                    case "^":
                        present.add(c);
                        break;
                }
            }
        }

        List<String> possible = new ArrayList<>();
        for (String word : availableVariants) {
            boolean ok = true;

            // проверка правильных букв
            for (int i = 0; i < correct.size(); i++) {
                Character expected = correct.get(i);
                if (expected != null && word.charAt(i) != expected) {
                    ok = false;
                    break;
                }
            }

            for (char c : present) {
                if (!word.contains(String.valueOf(c))) {
                    ok = false;
                    break;
                }
            }

            for (char c : absent) {
                if (word.contains(String.valueOf(c))) {
                    ok = false;
                    break;
                }
            }

            if (ok) {
                possible.add(word);
            }
        }
        return possible.isEmpty() ? null : possible.get(new Random().nextInt(possible.size()));
    }
}
