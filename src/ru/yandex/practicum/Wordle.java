package ru.yandex.practicum;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Wordle {

    public static final int WORD_LENGTH = 5;
    public static final int MAX_STEPS = 6;

    public static void main(String[] args) {
        File logFile = new File("wordle.log");

        try (PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true), true);
             Scanner scanner = new Scanner(System.in)) {

            if (!logFile.exists()) {
                logFile.createNewFile();
            }

            logWriter.println();
            logWriter.println("=== Игра Wordle началась ===");
            System.out.println("=== Игра Wordle началась ===");
            

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logWriter);
            WordleDictionary dictionary = loader.loadFromFile("words_ru.txt", WORD_LENGTH, StandardCharsets.UTF_8);

            WordleGame game = new WordleGame(dictionary, WORD_LENGTH, MAX_STEPS, logWriter);

            while (!game.isGameOver("")) {
                System.out.print("Введите слово или нажмите Enter для подсказки: ");
                String userWord = scanner.nextLine();

                if (userWord.isEmpty()) {
                    String hint = game.suggestWord();
                    if (hint != null) {
                        System.out.println("Подсказка: попробуйте слово \"" + hint + "\"");
                    } else {
                        System.out.println("Подсказок больше нет.");
                    }
                    System.out.println();
                    continue;
                }

                try {
                    String[] result = game.compareWords(userWord);

                    StringBuilder resultStr = new StringBuilder("Результат: ");
                    for (String s : result) {
                        resultStr.append(s);
                    }

                    System.out.println(" ".repeat(36) + resultStr);

                    if (game.getAnswer().equalsIgnoreCase(userWord)) {
                        System.out.println("Поздравляем, вы угадали слово!");
                        break;
                    }

                    System.out.println("Количество оставшихся ходов: " + game.getSteps());
                } catch (WordNotFoundInDictionary | WrongWordException | GameNoSuchWordException e) {
                    String errorMessage = "Ошибка: " + e.getMessage();
                    logWriter.println(errorMessage + ": " + userWord);
                    System.out.println(errorMessage);
                    System.out.println();
                }
            }

            if (game.getSteps() == 0 && !game.getAnswer().equalsIgnoreCase("")) {
                System.out.println("Вы проиграли, игра окончена. Загаданное слово: " + game.getAnswer());
            }

        } catch (IOException e) {
            System.out.println("Ошибка при работе с log-файлом: " + e.getMessage());
        }
    }

}
