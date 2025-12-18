package ru.yandex.practicum;

import java.io.PrintWriter;
import java.util.*;

/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private String answer;

    private int steps;

    private WordleDictionary dictionary;

    protected List<String> usedWords;

    protected List<String[]> usedResults;

    public WordleGame(WordleDictionary dictionary, String answer, int steps, PrintWriter logWriter) {
        this.dictionary = dictionary;
        this.answer = answer;
        this.steps = steps;
        this.usedWords = new ArrayList<>();
        this.usedResults = new ArrayList<>();
    }


    public void validate(String userWord) throws WordNotFoundInDictionary {
        if (userWord == null || userWord.isEmpty()) {
            throw new WordNotFoundInDictionary("Слово не может быть пустым");
        }

        if (userWord.length() != 5) {
            throw new WordNotFoundInDictionary("Слово должно состоять из 5 букв");
        }

        if (!dictionary.containsWord(userWord.toLowerCase())) {
            throw new WordNotFoundInDictionary("Слово отсутствует в словаре");
        }
    }


    public String[] compareWords(String userWord, String answer) {
        try {

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

        } catch (WordNotFoundInDictionary e) {
            return new String[]{e.getMessage()};
        }
    }


    public int getSteps() {
        return steps;
    }

    public boolean isGameOver(String userWord) {
        return steps <= 0 || answer.equalsIgnoreCase(userWord);
    }

    public String suggestWord() {
        List<String> availableVariants = dictionary.getWords();
        availableVariants.removeAll(usedWords);

        char[] correct = new char[answer.length()];
        Set<Character> present = new HashSet<>();
        Set<Character> absent = new HashSet<>();

        for (int i = 0; i < usedWords.size(); i++) {
            String guess = usedWords.get(i);
            String[] result = usedResults.get(i);

            for (int j = 0; j < guess.length(); j++) {
                char c = guess.charAt(j);
                switch (result[j]) {
                    case "+":
                        correct[j] = c;
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

            for (int i = 0; i < correct.length; i++) {
                if (correct[i] != 0 && word.charAt(i) != correct[i]) {
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
        if (possible.isEmpty()) return null;
        else return possible.get(new Random().nextInt(possible.size()));
    }
}
