package ru.geekbrains.core.lesson2;

import java.util.Random;
import java.util.Scanner;

public class Program {

    private static final char DOT_HUMAN = 'X';
    private static final char DOT_AI = '0';
    private static final char DOT_EMPTY = '*';

    private static final int WIN_COUNT = 4;

    private static final Scanner scanner = new Scanner(System.in);
    private static final Random random = new Random();

    private static char[][] field;
    private static int fieldSizeX;
    private static int fieldSizeY;

    public static void main(String[] args) {
        while (true) {
            initialize();
            printField();
            playGame();
            System.out.print("Желаете сыграть еще раз? (Y - да): ");
            if (!scanner.next().equalsIgnoreCase("Y"))
                break;
        }
    }

    static void initialize() {
        // Инициализация игрового поля
        fieldSizeY = 5;
        fieldSizeX = 5;

        field = new char[fieldSizeY][fieldSizeX];
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                field[y][x] = DOT_EMPTY;
            }
        }
    }

    private static void printField() {
        // Печать текущего состояния игрового поля
        System.out.print("+");
        for (int i = 0; i < fieldSizeX; i++) {
            System.out.print("-" + (i + 1));
        }
        System.out.println("-");

        for (int y = 0; y < fieldSizeY; y++) {
            System.out.print(y + 1 + "|");
            for (int x = 0; x < fieldSizeX; x++) {
                System.out.print(field[y][x] + "|");
            }
            System.out.println();
        }

        for (int i = 0; i < fieldSizeX * 2 + 2; i++) {
            System.out.print("-");
        }
        System.out.println();
    }

    static void playGame() {
        while (true) {
            humanTurn();
            printField();
            if (checkGameState(DOT_HUMAN, "Вы победили!"))
                break;
            aiTurn();
            printField();
            if (checkGameState(DOT_AI, "Победил компьютер!"))
                break;
        }
    }

    static void humanTurn() {
        int x;
        int y;

        do {
            System.out.print("Введите координаты хода X и Y (от 1 до 5)\nчерез пробел: ");
            x = scanner.nextInt() - 1;
            y = scanner.nextInt() - 1;
        } while (!isCellValid(x, y) || !isCellEmpty(x, y));

        field[y][x] = DOT_HUMAN;
    }

    static void aiTurn() {
        int x;
        int y;

        // Попробуем сначала выиграть
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isCellEmpty(j, i)) {
                    field[i][j] = DOT_AI;
                    if (checkWin(DOT_AI)) {
                        return;
                    }
                    field[i][j] = DOT_EMPTY;
                }
            }
        }

        // Затем блокируем ход игрока
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (isCellEmpty(j, i)) {
                    field[i][j] = DOT_HUMAN;
                    if (checkWin(DOT_HUMAN)) {
                        field[i][j] = DOT_AI;
                        return;
                    }
                    field[i][j] = DOT_EMPTY;
                }
            }
        }

        // Иначе делаем случайный ход
        do {
            x = random.nextInt(fieldSizeX);
            y = random.nextInt(fieldSizeY);
        } while (!isCellEmpty(x, y));

        field[y][x] = DOT_AI;
    }

    static boolean isCellEmpty(int x, int y) {
        return field[y][x] == DOT_EMPTY;
    }

    static boolean isCellValid(int x, int y) {
        return x >= 0 && x < fieldSizeX && y >= 0 && y < fieldSizeY;
    }

    static boolean checkGameState(char dot, String s) {
        if (checkWin(dot)) {
            System.out.println(s);
            return true;
        }
        if (checkDraw()) {
            System.out.println("Ничья!");
            return true;
        }
        return false; // Игра продолжается
    }

    static boolean checkDraw() {
        for (int y = 0; y < fieldSizeY; y++) {
            for (int x = 0; x < fieldSizeX; x++) {
                if (isCellEmpty(x, y))
                    return false;
            }
        }
        return true;
    }

    static boolean checkWin(char dot) {
        // Проверка победы для поля 5x5 и количества фишек WIN_COUNT
        for (int i = 0; i < fieldSizeY; i++) {
            for (int j = 0; j < fieldSizeX; j++) {
                if (checkLine(dot, i, j, 0, 1, WIN_COUNT) ||
                        checkLine(dot, i, j, 1, 0, WIN_COUNT) ||
                        checkLine(dot, i, j, 1, 1, WIN_COUNT) ||
                        checkLine(dot, i, j, -1, 1, WIN_COUNT)) {
                    return true;
                }
            }
        }
        return false;
    }

    static boolean checkLine(char dot, int y, int x, int yDirection, int xDirection, int len) {
        int endY = y + (len - 1) * yDirection;
        int endX = x + (len - 1) * xDirection;

        if (endY < 0 || endY >= fieldSizeY || endX < 0 || endX >= fieldSizeX) {
            return false;
        }

        for (int i = 0; i < len; i++) {
            if (field[y + i * yDirection][x + i * xDirection] != dot) {
                return false;
            }
        }
        return true;
    }
}
