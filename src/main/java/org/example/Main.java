package org.example;

import java.util.logging.Logger;

public class Main {
    private static final Logger logger = Logger.getLogger(Main.class.getName());
    public static void main(String[] args) {
        if (args.length == 0) {
            logger.severe("Ошибка запуска программы!");
        } else {
            new AppRunner(args).run();
        }
    }
}