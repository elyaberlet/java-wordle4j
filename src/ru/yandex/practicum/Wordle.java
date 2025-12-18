package ru.yandex.practicum;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {
    public static void main(String[] args) {
        File logFile = new File("wordle.log");
        PrintWriter logWriter = null;

        try {
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            logWriter = new PrintWriter(new FileWriter(logFile, true), true);

            logWriter.println();
            logWriter.println("=== Игра Wordle началась ===");
            System.out.println("=== Игра Wordle началась ===");

            WordleDictionaryLoader loader = new WordleDictionaryLoader(logWriter);
            WordleDictionary dictionary = loader.loadFromFile("words_ru.txt", 5, StandardCharsets.UTF_8);

            String answer = dictionary.getRandomWord();
            logWriter.println("Загаданное слово: " + answer);

            int steps = 6;
            WordleGame game = new WordleGame(dictionary, answer, steps, logWriter);
            Scanner scanner = new Scanner(System.in);

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
                    game.validate(userWord);
                    String[] result = game.compareWords(userWord, answer);

                    StringBuilder resultStr = new StringBuilder("Результат: ");
                    for (String s : result) {
                        resultStr.append(s);
                    }

                    System.out.println(" ".repeat(36) + resultStr.toString());

                    if (game.isGameOver(userWord)) {
                        if (answer.equalsIgnoreCase(userWord)) {
                            ;
                            System.out.println("Поздравляем, вы угадали слово!");
                        } else {
                            System.out.println("Вы проиграли, игра окончена. Загаданное слово: " + answer);
                        }
                        break;
                    }
                    System.out.println("Количество оставшихся ходов: " + game.getSteps());
                } catch (WordNotFoundInDictionary e) {
                    String errorMessage = "Ошибка: " + e.getMessage();
                    logWriter.println(errorMessage + ": " + userWord);
                    System.out.println(errorMessage);
                    System.out.println();
                }
            }
        } catch (IOException e) {
            System.out.println("Ошибка при работе с log-файлом: " + e.getMessage());
        } finally {
            if (logWriter != null) {
                logWriter.close();
            }
        }
    }
}