package ru.yandex.practicum;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    private final PrintWriter log;

    public WordleDictionaryLoader(PrintWriter log) {
        this.log = log;
    }

    public WordleDictionary loadFromFile(String fileName, int wordLength, Charset charsetName) {
        List<String> words = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName, charsetName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String normalized = line.toLowerCase().replace("ё", "е");
                if (normalized.length() == wordLength) {
                    words.add(line);
                }
            }
        } catch (IOException e) {
            log.println("Ошибка работы с файлом: " + e.getMessage());
        }
        return new WordleDictionary(words);
    }
}
