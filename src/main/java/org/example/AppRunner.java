package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

public class AppRunner {
    private final List<String> floats;
    private final List<String> integers;
    private final List<String> strings;
    private final String[] args;
    private static final String FLOATS_FILE_NAME = "floats.txt";
    private static final String INTS_FILE_NAME = "integers.txt";
    private static final String STRINGS_FILE_NAME = "strings.txt";
    private static final Logger logger = Logger.getLogger(AppRunner.class.getName());

    public AppRunner(String[] args) {
        this.floats = new ArrayList<>();
        this.integers = new ArrayList<>();
        this.strings = new ArrayList<>();
        this.args = args;
    }

    public void run() {
        Optional<String> resultsPath = resultPathDetecting();
        Optional<String> resultFileNamePrefix = resultFilesNamePrefix();

        String floatsFileName;
        String intsFileName;
        String stringsFileName;
        boolean appendingToFiles = resultAddingToFilesDetecting();
        try {
            for (String pathToFile : args) {
                if (pathToFile.contains(".txt")) {
                    List<String> mayBeText = FileReader.readFile(pathToFile);
                    contentDetecting(mayBeText);
                }
            }
        } catch (FileNotFoundException e) {
            logger.warning(e.getMessage());
        }
        if (resultsPath.isPresent() && resultFileNamePrefix.isPresent()) {
            floatsFileName = resultsPath.get() + File.pathSeparator + resultFileNamePrefix.get() + FLOATS_FILE_NAME;
            intsFileName = resultsPath.get() + File.pathSeparator + resultFileNamePrefix.get() + INTS_FILE_NAME;
            stringsFileName = resultsPath.get() + File.pathSeparator + resultFileNamePrefix.get() + STRINGS_FILE_NAME;
            writeTheFiles(floatsFileName, intsFileName, stringsFileName, appendingToFiles);
        } else if (resultsPath.isPresent()) {
            floatsFileName = resultsPath.get() + File.pathSeparator + FLOATS_FILE_NAME;
            intsFileName = resultsPath.get() + File.pathSeparator + INTS_FILE_NAME;
            stringsFileName = resultsPath.get() + File.pathSeparator + STRINGS_FILE_NAME;
            writeTheFiles(floatsFileName, intsFileName, stringsFileName, appendingToFiles);
        } else if (resultFileNamePrefix.isPresent()) {
            floatsFileName = resultFileNamePrefix.get() + FLOATS_FILE_NAME;
            intsFileName = resultFileNamePrefix.get() + INTS_FILE_NAME;
            stringsFileName = resultFileNamePrefix.get() + STRINGS_FILE_NAME;
            writeTheFiles(floatsFileName, intsFileName, stringsFileName, appendingToFiles);
        } else {
            writeTheFiles(FLOATS_FILE_NAME, INTS_FILE_NAME, STRINGS_FILE_NAME, appendingToFiles);
        }
        statisticsDetecting();
    }

    private void statisticsDetecting() {
        for (String s : args) {
            if (s.equals("-s")) {
                printShortStatistics();
            }
            if (s.equals("-f")) {
                if (!floats.isEmpty()) printFullStatisticsForFloats();
                if (!integers.isEmpty()) printFullStatisticsForIntegers();
                if (!strings.isEmpty()) printFullStatisticsForStrings();
            }
        }
    }

    private boolean resultAddingToFilesDetecting() {
        for (String arg : args) {
            if (arg.equals("-a")) {
                return true;
            }
        }
        return false;
    }

    private Optional<String> resultPathDetecting() {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-o")) {
                return Optional.of(args[i + 1]);
            }
        }
        return Optional.empty();
    }

    private Optional<String> resultFilesNamePrefix() {
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-p")) {
                return Optional.of(args[i + 1]);
            }
        }
        return Optional.empty();
    }

    private void contentDetecting(List<String> text) {
        for (String line : text) {
            if (floatDetecting(line)) {
                floats.add(line);
            } else if (integerDetecting(line)) {
                integers.add(line);
            } else {
                strings.add(line);
            }
        }
    }

    private boolean floatDetecting(String mayBeFloat) {
        try {
            Float.parseFloat(mayBeFloat);
            return (mayBeFloat.contains(".") || mayBeFloat.contains(","));
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    private boolean integerDetecting(String mayBeInt) {
        try {
            char[] chars = mayBeInt.toCharArray();
            for (char c : chars) {
                Integer.parseInt(String.valueOf(c));
            }
            return true;
        } catch (NumberFormatException | NullPointerException e) {
            return false;
        }
    }

    private void writeTheFiles(String floatsFileName, String integersFileName, String stringsFileName, boolean append) {
        if (!floats.isEmpty()) {
            FileWriter.writeTheFile(floatsFileName, append, floats);
        }
        if (!integers.isEmpty()) {
            FileWriter.writeTheFile(integersFileName, append, integers);
        }
        if (!strings.isEmpty()) {
            FileWriter.writeTheFile(stringsFileName, append, strings);
        }
    }

    private void printShortStatistics() {
        String tmp = "В файле %s содержится %s элемента(ов))";
        String tmpForInts = String.format(tmp, INTS_FILE_NAME, integers.size());
        String tmpForFloats = String.format(tmp, FLOATS_FILE_NAME, floats.size());
        String tmpForString = String.format(tmp, STRINGS_FILE_NAME, strings.size());
        if (!floats.isEmpty()) {
            logger.info(tmpForFloats);
        }
        if (!integers.isEmpty()) {
            logger.info(tmpForInts);
        }
        if (!strings.isEmpty()) {
            logger.info(tmpForString);
        }
    }

    private void printFullStatisticsForStrings() {
        logger.info("********************************* Статистика для strings ****************************");
        String tmp = String.format("В файле содержится %s элемента(ов)", strings.size());
        logger.info(tmp);
        try {
            int maxLength = strings.stream()
                    .map(String::length)
                    .max(Integer::compareTo)
                    .orElseThrow(NullPointerException::new);
            int minLength = strings.stream()
                    .map(String::length)
                    .min(Integer::compareTo)
                    .orElseThrow(NullPointerException::new);
            String minMaxValues = String.format("Размер самой короткой и самой длинной строки равны %s и %s соответственно", minLength, maxLength);
            logger.info(minMaxValues);
        } catch (NullPointerException e) {
            logger.severe(e.getMessage());
        }
    }

    private void printFullStatisticsForIntegers() {
        logger.info("********************************* Статистика для integers ****************************");
        List<BigInteger> bigIntegers = integers.stream()
                .map(BigInteger::new)
                .toList();
        Optional<BigInteger> min = bigIntegers.stream()
                .min(BigInteger::compareTo);
        try {
            String minValue = String.format("Миннимальное значение из целых чисел: %s", min.orElseThrow(NullPointerException::new));
            logger.info(minValue);
        } catch (NullPointerException e) {
            logger.severe(e.getMessage());
        }

        Optional<BigInteger> max = bigIntegers.stream()
                .max(BigInteger::compareTo);
        try {
            String maxValue = String.format("Максимальное значение из целых чисел: %s", max.orElseThrow(NullPointerException::new));
            logger.info(maxValue);
        } catch (NullPointerException e) {
            logger.severe(e.getMessage());
        }

        BigDecimal sum = bigIntegers.stream()
                .map(BigInteger::toString)
                .map(BigDecimal::new)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        String sumValue = String.format("Сумма целых чисел равна: %s", sum);
        logger.info(sumValue);
        try {
            BigDecimal average = bigIntegers.isEmpty() ? BigDecimal.ZERO : sum.divide(BigDecimal.valueOf(bigIntegers.size()));
            String averageValue = String.format("Среднее значение целых чисел: %s", average);
            logger.info(averageValue);
        } catch (ArithmeticException e) {
            logger.severe(e.getMessage());
        }
    }

    private void printFullStatisticsForFloats() {
        logger.info("********************************* Статистика для floats ****************************");
        List<Double> doublesList = floats.stream()
                .map(Double::parseDouble)
                .toList();

        Optional<Double> min = doublesList.stream()
                .min(Double::compareTo);
        try {
            String minValue = String.format("Миннимальное значение из дробных чисел: %s", min.orElseThrow(NullPointerException::new));
            logger.info(minValue);
        } catch (NullPointerException e) {
            logger.severe(e.getMessage());
        }
        Optional<Double> max = doublesList.stream()
                .max(Double::compareTo);
        try {
            String maxValue = String.format("Миннимальное значение из дробных чисел: %s", max.orElseThrow(NullPointerException::new));
            logger.info(maxValue);
        } catch (NullPointerException e) {
            logger.severe(e.getMessage());
        }

        double sum = doublesList.stream()
                .mapToDouble(Double::doubleValue)
                .sum();
        String sumValue = String.format("Сумма дробных чисел равна: %s %n", sum);
        logger.info(sumValue);

        double average = sum / floats.size();
        String averageValue = String.format("Среднее значение дробных чисел: %s", average);
        logger.info(averageValue);
    }
}
